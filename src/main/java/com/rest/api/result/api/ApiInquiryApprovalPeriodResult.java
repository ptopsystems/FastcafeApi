package com.rest.api.result.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiInquiryApprovalPeriodResult {
    private String errCode;
    private String errMsg;
    private String result;
    private ApiInquiryApprovalPeriodResultData data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApiInquiryApprovalPeriodResultData {
        @JsonAlias("TOTALTRAN")
        private String totalTran;
        @JsonAlias("ETRACK")
        private String eTrack;
        @JsonAlias("ERRMSG")
        private String errMsg;
        @JsonAlias("CANCEL")
        private String cancel;
        @JsonAlias("ECODE")
        private String eCode;
        @JsonAlias("ERRDOC")
        private String errDoc;
        @JsonAlias("APPROVE")
        private String approve;
        @JsonAlias("APPROVECNT")
        private String approveCnt;
        @JsonAlias("RESULT")
        private String result;
        @JsonAlias("TRANCNT")
        private String tranCnt;
        @JsonAlias("CANCELCNT")
        private String cancelCnt;
        @JsonAlias("APPROVELIST")
        private List<ApiInquiryApprovalPeriodResultDataApprove> approveList;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ApiInquiryApprovalPeriodResultDataApprove {
            @JsonAlias("APPAMT")
            private String appAmt;
            @JsonAlias("NO")
            private String no;
            @JsonAlias("CARDNM")
            private String cardNm;
            @JsonAlias("TRANSDATE")
            private String transDate;
            @JsonAlias("INSTRMNM")
            private String instrmNm;
            @JsonAlias("AUTHCLASSNM")
            private String authClassNm;
            @JsonAlias("TRANSTIME")
            private String transTime;
            @JsonAlias("APPNO")
            private String appNo;
            @JsonAlias("CARDNO")
            private String cardMo;
        }
    }

}
