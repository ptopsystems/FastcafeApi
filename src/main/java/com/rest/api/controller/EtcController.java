package com.rest.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.config.BankCodeConfig;
import com.rest.api.entity.fastcafe_admin.Branch;
import com.rest.api.exception.BranchNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.result.api.ApiPutDeleteManageFranchiseResult;
import com.rest.api.service.BranchService;
import com.rest.api.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class EtcController {

    @Value("${cardpay.api.url}")
    private String API_URL;

    @Value("${cardpay.api.token}")
    private String TOKEN;

    private final BankCodeConfig bankCodeConfig;

    private final BranchService branchService;
    private final ScheduleService scheduleService;

    @GetMapping("/etc/bankcode")
    public CommonResult bankcode(){
        return DataResult.Success("banks", bankCodeConfig.getBanks());
    }

    @PutMapping("/etc/manage/franchise")
    public CommonResult postManageFanchise(@RequestParam(name = "branch_id") int branch_id) throws JsonProcessingException {
        Branch branch = branchService.findById(branch_id).orElseThrow(BranchNotFoundException::new);

        HashMap<String, String> body = new HashMap<>();
        body.put("SAUPNO", StringUtils.replace(branch.getBusinessLicense(), "-", ""));
        body.put("RSVJUMINPRE", branch.getOwnerBirthday());
        body.put("RPSINSTCD", branch.getBankCode());
        body.put("STLACCTNO", StringUtils.replace(branch.getBankAccount(), "-", ""));
        body.put("RSVNM", branch.getOwnerName());
        body.put("BIGO", null);
        body.put("MERGRPNAME", branch.getBranchName());
        body.put("CDCODE", "C1");

        return this.call(branch, body, HttpMethod.PUT);
    }

    @DeleteMapping("/etc/manage/franchise")
    public CommonResult deleteManageFranchise(@RequestParam(name = "branch_id") int branch_id) throws JsonProcessingException {
        Branch branch = branchService.findById(branch_id).orElseThrow(BranchNotFoundException::new);

        HashMap<String, String> body = new HashMap<>();
        body.put("SAUPNO", StringUtils.replace(branch.getBusinessLicense(), "-", ""));
        body.put("MERGRPNAME", branch.getBranchName());

        return this.call(branch, body, HttpMethod.DELETE);
    }

    private CommonResult call(Branch branch, HashMap<String, String> body, HttpMethod method) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + TOKEN);
        headers.set("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/manage/franchise", method, httpEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        ApiPutDeleteManageFranchiseResult apiPutDeleteManageFranchiseResult = mapper.readValue(response.getBody(), ApiPutDeleteManageFranchiseResult.class);

        if(apiPutDeleteManageFranchiseResult.getResult().equalsIgnoreCase("SUCCESS")){
            branch = branch.withRegisterCardPayApi(method != HttpMethod.DELETE);
            branchService.save(branch);
            return CommonResult.Success(200, "처리되었습니다.");
        }
        return CommonResult.Fail(500, apiPutDeleteManageFranchiseResult.getErrMsg());
    }

    @PostMapping("/etc/paycard/period")
    public CommonResult apiCardPayPeriod(
            @RequestParam(name = "startdate") String strStartdate
            , @RequestParam(name = "enddate") String strEnddate) throws JsonProcessingException {

        LocalDate startdate = LocalDate.parse(strStartdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate enddate = LocalDate.parse(strEnddate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        scheduleService.insertCardPayApiPeriod(startdate, enddate);

        return CommonResult.Success();
    }
}
