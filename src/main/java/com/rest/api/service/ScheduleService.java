package com.rest.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.entity.fastcafe_admin.Branch;
import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import com.rest.api.entity.fastcafe_log.LogCardPayByApiData;
import com.rest.api.result.api.ApiGetManageFranchiseResult;
import com.rest.api.result.api.ApiInquiryApprovalPeriodResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Value("${cardpay.api.url}")
    private String API_URL;

    @Value("${cardpay.api.token}")
    private String TOKEN;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PayService payService;
    private final BranchService branchService;
    private final LogService logService;


    @Transactional
    public void insertCardPayApi() throws JsonProcessingException {
        LocalDate today = LocalDate.now();
        LocalDate enddate = today.minusDays(1);
        LocalDate startdate = enddate;

        //
        List<Branch> branches = branchService.findByRegisterCardPayApi();
        for(Branch branch : branches) {
            long starttime = System.currentTimeMillis();
            // 오늘 데이터 조회기록이 있는지 로그 검색
            LogCardPayByApiData checkLog = logService.getLastLogCardPayByApiData(branch.getId(), Date.valueOf(today));
            if (checkLog != null) {
                // 조회 기록이 있다면 같은 검색기간으로 조회
                startdate = checkLog.getStartdate().toLocalDate();
                enddate = checkLog.getEnddate().toLocalDate();
            } else {
                // 마지막 데이터 날짜 조회
                Date maxDate = payService.getMaxTransDateForCardPayByApi(branch.getId());
                if (maxDate != null
                        && (enddate.isAfter(maxDate.toLocalDate().plusDays(1)) || enddate.isEqual(maxDate.toLocalDate().plusDays(1)))) {
                    // 마지막 데이터 날짜가 전일자 보다 이전이거나 같으면 검색 시작일 수정
                    startdate = maxDate.toLocalDate().plusDays(1);
                }
            }

            RestTemplate restTemplate = new RestTemplate();

            HashMap<String, String> body = new HashMap<>();
            body.put("COMPANY_NO", StringUtils.replace(branch.getBusinessLicense(), "-", ""));
            body.put("START_DATE", startdate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            body.put("END_DATE", enddate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Token " + TOKEN);
            headers.set("Content-Type", "application/json;charset=UTF-8");

            HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
            /*
                승인내역 조회
             */
            ResponseEntity<String> responseDetail = restTemplate.exchange(API_URL + "/inquiry/Approval/Period", HttpMethod.POST, httpEntity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            ApiInquiryApprovalPeriodResult apiInquiryApprovalPeriodResult = mapper.readValue(responseDetail.getBody(), ApiInquiryApprovalPeriodResult.class);
            ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData resultData = apiInquiryApprovalPeriodResult.getData();

            logger.info("[API] 검색기간: {} ~ {}, 지점명: {}, 총매출: {}"
                    , startdate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    , enddate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    , branch.getBranchName()
                    , resultData.getTotalTran());

            if (apiInquiryApprovalPeriodResult.getResult().equalsIgnoreCase("SUCCESS")) {
                // 마지막 스케줄 로그 기록과 전체 승인금액이 같으면 SKIP
                if ((checkLog != null
                        && !resultData.getTotalTran().equalsIgnoreCase("null")
                        && checkLog.getTotalTran() == Integer.parseInt(resultData.getTotalTran()))
                        && !resultData.getApproveCnt().equalsIgnoreCase("null")
                        && Integer.parseInt(resultData.getTranCnt()) == resultData.getApproveList().size()
                        && resultData.getResult().equalsIgnoreCase("SUCCESS")
                ) continue;

                        /*
                            승인 내역
                         */
                for (ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData.ApiInquiryApprovalPeriodResultDataApprove approve : resultData.getApproveList()) {
                    // 같은 데이터가 있는지 검색
                    CardPayByApi entity = payService.getCardPayByApi(
                            branch.getId()
                            , Date.valueOf(LocalDate.parse(approve.getTransDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                            , approve.getTransTime()
                            , approve.getCardNm()
                            , approve.getCardNo()
                            , approve.getAppNo()
                            , approve.getAppClassNm()
                    );

                    if (entity == null) {
                        // 같은 데이터가 없는 경우에만 저장
                        entity = CardPayByApi.builder()
                                .branchId(branch.getId())
                                .transDate(Date.valueOf(LocalDate.parse(approve.getTransDate(), DateTimeFormatter.ofPattern("yyyyMMdd"))))
                                .transTime(approve.getTransTime())
                                .appClassNm(approve.getAppClassNm().equalsIgnoreCase("null") ? "" : approve.getAppClassNm())
                                .cardNm(approve.getCardNm())
                                .cardNo(approve.getCardNo())
                                .appNo(approve.getAppNo())
                                .appAmt(Integer.parseInt(approve.getAppAmt()))
                                .instrmNm(approve.getInstrmNm().replaceAll(" ", ""))
                                .regdate(new Timestamp(System.currentTimeMillis()))
                                .build();

                        entity = payService.insertCardPayByApi(entity);
                    }
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
    }

    @Transactional
    public void insertCardPayApiPeriod(LocalDate startdate, LocalDate enddate) throws JsonProcessingException {
        long start = System.currentTimeMillis();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + TOKEN);
        headers.set("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(headers);

        /*
            가맹점 조회 API 호출
         */
        logger.info("[###API###] 가맹점 조회 호출 시작");
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/manage/franchise", HttpMethod.GET, httpEntity, String.class);
        logger.info("[###API###] 가맹점 조회 호출 종료 ({}ms)", (System.currentTimeMillis() - start));


        ObjectMapper mapper = new ObjectMapper();
        ApiGetManageFranchiseResult apiGetManageFranchiseResult = mapper.readValue(response.getBody(), ApiGetManageFranchiseResult.class);

        if(apiGetManageFranchiseResult.getResult().equalsIgnoreCase("SUCCESS")){
            if(apiGetManageFranchiseResult.getData().getResult().equalsIgnoreCase("SUCCESS")){
                /*
                    가맹점 목록
                 */
                for(ApiGetManageFranchiseResult.ApiGetManageFranchiseResultData.ApiGetManageFranchiseResultDataMem mem : apiGetManageFranchiseResult.getData().getMemList()){
                    // 가맹점 사업자 번호로 Branch 조회
                    start = System.currentTimeMillis();
                    logger.info("[###API###] 사업자번호 '{}' 로 매장 조회 시작", mem.getMemListNo());
                    Branch branch = branchService.getBranchByBusinessLiscense(mem.getMemListNo());
                    logger.info("[###API###] 사업자번호 '{}' 로 매장 조회 종료 ({}ms) - 매장명 : {}", mem.getMemListNo(), (System.currentTimeMillis() - start), branch == null ? "없음" : branch.getBranchName());
                    if(branch == null) continue;

                    // 오늘 데이터 조회기록이 있는지 로그 검색
                    HashMap<String, String> body = new HashMap<>();
                    body.put("COMPANY_NO", StringUtils.replace(branch.getBusinessLicense(), "-", ""));
                    body.put("START_DATE", startdate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    body.put("END_DATE", enddate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                    httpEntity = new HttpEntity<>(body, headers);

                    /*
                        승인내역 조회
                     */
                    start = System.currentTimeMillis();
                    logger.info("[###API###] 승인내역 조회 시작");
                    ResponseEntity<String> responseDetail = restTemplate.exchange(API_URL + "/inquiry/Approval/Period", HttpMethod.POST, httpEntity, String.class);
                    logger.info("[###API###] 승인내역 조회 종료 ({}ms)", (System.currentTimeMillis() - start));

                    mapper = new ObjectMapper();
                    ApiInquiryApprovalPeriodResult apiInquiryApprovalPeriodResult = mapper.readValue(responseDetail.getBody(), ApiInquiryApprovalPeriodResult.class);
                    ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData resultData = apiInquiryApprovalPeriodResult.getData();

                    logger.info("[API] 검색기간: {} ~ {}, 지점명: {}, 총매출: {}"
                            , startdate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            , enddate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            ,  branch.getBranchName()
                            , resultData.getTotalTran());

                    if(apiInquiryApprovalPeriodResult.getResult().equalsIgnoreCase("SUCCESS")){
                        /*
                            승인 내역
                         */
                        start = System.currentTimeMillis();
                        for(ApiInquiryApprovalPeriodResult.ApiInquiryApprovalPeriodResultData.ApiInquiryApprovalPeriodResultDataApprove approve : resultData.getApproveList()){
                            // 같은 데이터가 있는지 검색
                            CardPayByApi entity = payService.getCardPayByApi(
                                    branch.getId()
                                    , Date.valueOf(LocalDate.parse(approve.getTransDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                    , approve.getTransTime()
                                    , approve.getCardNm()
                                    , approve.getCardNo()
                                    , approve.getAppNo()
                                    , approve.getAppClassNm()
                            );

                            if(entity == null){
                                // 같은 데이터가 없는 경우에만 저장
                                entity = CardPayByApi.builder()
                                        .branchId(branch.getId())
                                        .transDate(Date.valueOf(LocalDate.parse(approve.getTransDate(), DateTimeFormatter.ofPattern("yyyyMMdd"))))
                                        .transTime(approve.getTransTime())
                                        .appClassNm(approve.getAppClassNm().equalsIgnoreCase("null") ? "" : approve.getAppClassNm())
                                        .cardNm(approve.getCardNm())
                                        .cardNo(approve.getCardNo())
                                        .appNo(approve.getAppNo())
                                        .appAmt(Integer.parseInt(approve.getAppAmt()))
                                        .instrmNm(approve.getInstrmNm().replaceAll(" ", ""))
                                        .regdate(new Timestamp(System.currentTimeMillis()))
                                        .build();

                                entity = payService.insertCardPayByApi(entity);
                            }
                        }
                        logger.info("[###API###] 매출내역 입력 종료 - {}ms", (System.currentTimeMillis() - start));
                    }
                }
            } else {
                logger.error("[API]가맹점 목록 데이터 조회 실패 - [{}] : {}", apiGetManageFranchiseResult.getData().getECode(), apiGetManageFranchiseResult.getData().getErrMsg());
            }
        } else {
            logger.error("[API]가맹점 목록 조회 실패 - [{}] : {}", apiGetManageFranchiseResult.getErrCode(), apiGetManageFranchiseResult.getErrMsg());
        }
    }
}
