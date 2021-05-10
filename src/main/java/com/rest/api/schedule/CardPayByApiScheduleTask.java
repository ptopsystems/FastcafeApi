package com.rest.api.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.entity.fastcafe_admin.Branch;
import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import com.rest.api.entity.fastcafe_log.LogCardPayByApiData;
import com.rest.api.result.api.ApiInquiryApprovalPeriodResult;
import com.rest.api.result.api.ApiManageFranchiseResult;
import com.rest.api.service.BranchService;
import com.rest.api.service.LogService;
import com.rest.api.service.PayService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class CardPayByApiScheduleTask {

    @Value("${cardpay.api.url}")
    private String API_URL;

    @Value("${cardpay.api.token}")
    private String TOKEN;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PayService payService;
    private final BranchService branchService;
    private final LogService logService;

//    @Scheduled(cron = "0 0 */1 * * *", zone = "Asia/Seoul")
    public void insertPaymentInfo() throws JsonProcessingException {
        //
        LocalDate today = LocalDate.now();
        LocalDate enddate = today.minusDays(1);
        LocalDate startdate = enddate;

        //
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + TOKEN);
        headers.set("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(headers);

        /*
            가맹점 조회 API 호출
         */
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "manage/franchise", HttpMethod.GET, httpEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        ApiManageFranchiseResult apiManageFranchiseResult = mapper.readValue(response.getBody(), ApiManageFranchiseResult.class);

         if(apiManageFranchiseResult.getResult().equalsIgnoreCase("SUCCESS")){
            if(apiManageFranchiseResult.getData().getResult().equalsIgnoreCase("SUCCESS")){
                /*
                    가맹점 목록
                 */
                for(ApiManageFranchiseResult.ApiManageFranchiseResultData.ApiManageFranchiseResultDataMem mem : apiManageFranchiseResult.getData().getMemList()){
                    long starttime = System.currentTimeMillis();

                    // 가맹점 사업자 번호로 Branch 조회
                    Branch branch = branchService.getBranchByBusinessLiscense(mem.getMemListNo());
                    if(branch == null) continue;

                    // 오늘 데이터 조회기록이 있는지 로그 검색
                    LogCardPayByApiData checkLog = logService.getLastLogCardPayByApiData(branch.getId(), Date.valueOf(today));
                    if(checkLog != null) {
                        // 조회 기록이 있다면 같은 검색기간으로 조회
                        startdate = checkLog.getStartdate().toLocalDate();
                        enddate = checkLog.getEnddate().toLocalDate();
                    } else {
                        // 마지막 데이터 날짜 조회
                        Date maxDate = payService.getMaxTransDateForCardPayByApi(branch.getId());
                        if(maxDate != null && enddate.isAfter(maxDate.toLocalDate())) {
                            // 마지막 데이터 날짜가 전일자 보다 이전이면 검색 시작일 수정
                            startdate = maxDate.toLocalDate();
                        } else {
                            startdate = enddate.with(TemporalAdjusters.firstDayOfMonth());
                        }
                    }

                    HashMap<String, String> body = new HashMap<>();
                    body.put("COMPANY_NO", StringUtils.replace(branch.getBusinessLicense(), "-", ""));
                    body.put("START_DATE", startdate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    body.put("END_DATE", enddate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                    httpEntity = new HttpEntity<>(body, headers);

                    /*
                        승인내역 조회
                     */
                    ResponseEntity<String> responseDetail = restTemplate.exchange(API_URL + "inquiry/Approval/Period", HttpMethod.POST, httpEntity, String.class);

                    mapper = new ObjectMapper();
                    ApiInquiryApprovalPeriodResult apiInquiryApprovalPeriodResult = mapper.readValue(responseDetail.getBody(), ApiInquiryApprovalPeriodResult.class);
                    ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData resultData = apiInquiryApprovalPeriodResult.getData();


                    if(apiInquiryApprovalPeriodResult.getResult().equalsIgnoreCase("SUCCESS")){
                        // 마지막 스케줄 로그 기록과 전체 승인금액이 같으면 SKIP
                        if(checkLog != null && checkLog.getTotalTran() == Integer.parseInt(resultData.getTotalTran())) continue;

                        /*
                            승인 내역
                         */
                        for(ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData.ApiInquiryApprovalPeriodResultDataApprove approve : resultData.getApproveList()){
                            CardPayByApi entity =
                                    CardPayByApi.builder()
                                    .branchId(branch.getId())
                                    .transDate(Date.valueOf(LocalDate.parse(approve.getTransDate(), DateTimeFormatter.ofPattern("yyyyMMdd"))))
                                    .transTime(approve.getTransTime())
                                    .authClassName(!approve.getAuthClassNm().equalsIgnoreCase("null") ? approve.getAuthClassNm() : "")
                                    .cardNm(approve.getCardNm())
                                    .cardNo(approve.getCardMo())
                                    .appNo(approve.getAppNo())
                                    .appAmt(Integer.parseInt(approve.getAppAmt()))
                                    .instrmNm(approve.getInstrmNm().trim())
                                    .regdate(new Timestamp(System.currentTimeMillis()))
                                    .build();

                            entity = payService.insertCardPayByApi(entity);
                        }
                    }

                    // 결과 로그 저장
                    LogCardPayByApiData log
                            = LogCardPayByApiData.builder()
                            .branchId(branch.getId())
                            .basedate(Date.valueOf(today))
                            .startdate(Date.valueOf(startdate))
                            .enddate(Date.valueOf(enddate))
                            .totalTran(!resultData.getTotalTran().equals("null") ? Integer.parseInt(resultData.getTotalTran()) : 0)
                            .approve(!resultData.getApprove().equals("null") ? Integer.parseInt(resultData.getApprove()) : 0)
                            .cancel(!resultData.getCancel().equals("null") ? Integer.parseInt(resultData.getCancel()) : 0)
                            .tranCnt(!resultData.getTranCnt().equals("null") ? Integer.parseInt(resultData.getTranCnt()) : 0)
                            .approveCnt(!resultData.getApproveCnt().equals("null") ? Integer.parseInt(resultData.getApproveCnt()) : 0)
                            .cancelCnt(!resultData.getCancelCnt().equals("null") ? Integer.parseInt(resultData.getCancelCnt()) : 0)
                            .result(resultData.getResult())
                            .errMsg(resultData.getErrMsg())
                            .errDoc(resultData.getErrDoc())
                            .eCode(resultData.getECode())
                            .eTrack(resultData.getETrack())
                            .excuteTime(Math.toIntExact(System.currentTimeMillis() - starttime))
                            .regdate(new Timestamp(System.currentTimeMillis()))
                            .build();

                    log = logService.insertLogCardPayByApiData(log);
                }
            } else {
                logger.error("[API]가맹점 목록 데이터 조회 실패 - [{}] : {}", apiManageFranchiseResult.getData().getECode(), apiManageFranchiseResult.getData().getErrMsg());
            }
        } else {
            logger.error("[API]가맹점 목록 조회 실패 - [{}] : {}", apiManageFranchiseResult.getErrCode(), apiManageFranchiseResult.getErrMsg());
         }
    }
}
