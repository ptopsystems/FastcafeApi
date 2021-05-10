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
public class ApiManageFranchiseResult {
    private String errCode;
    private String errMsg;
    private String result;
    private ApiManageFranchiseResultData data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApiManageFranchiseResultData {
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
        private List<ApiManageFranchiseResultDataMem> memList;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ApiManageFranchiseResultDataMem {
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
