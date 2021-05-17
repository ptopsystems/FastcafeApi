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
public class ApiGetManageFranchiseResult {
    private String errCode;
    private String errMsg;
    private String result;
    private ApiGetManageFranchiseResultData data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApiGetManageFranchiseResultData {
        @JsonAlias("RESULT")
        private String result;
        @JsonAlias("ERRMSG")
        private String errMsg;
        @JsonAlias("ERRDOC")
        private String errDoc;
        @JsonAlias("ECODE")
        private String eCode;
        @JsonAlias("ETRACK")
        private String eTrack;
        @JsonAlias("MEMLIST")
        private List<ApiGetManageFranchiseResultDataMem> memList;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ApiGetManageFranchiseResultDataMem {
            @JsonAlias("MEMLISTNO")
            private String memListNo;
            @JsonAlias("MEMLISTNAME")
            private String memListName;
            @JsonAlias("MEMLISTVALUE")
            private String memListValue;
            @JsonAlias("MEMLISTJUMIN")
            private String memListJumin;
        }
    }
}
