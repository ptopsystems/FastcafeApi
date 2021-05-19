package com.rest.api.result.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiPutDeleteManageFranchiseResult {
    private String errCode;
    private String errMsg;
    private String result;
    private Object data;
}
