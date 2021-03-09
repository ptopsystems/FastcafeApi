package com.rest.api.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DataResult extends CommonResult{
    private Map<String, Object> data = new HashMap<>();
    private static String DEAFULT_KEY = "result";

    public DataResult(Object result){
        this.data.put(DEAFULT_KEY, result);
    }

    public DataResult addResult(String key, Object result){
        if(!StringUtils.hasText(key)) key = DEAFULT_KEY;
        this.data.put(key, result);
        return this;
    }

    public static DataResult Success(Object result){
        return new DataResult(result);
    }

    public static DataResult Success(String key, Object result){
        DataResult returnValue = new DataResult();
        returnValue.addResult(key, result);
        return returnValue;
    }

}
