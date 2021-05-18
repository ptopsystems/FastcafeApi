package com.rest.api.entity.fastcafe_stat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatMonthlyCardPayByApiDTO {
    private String baseYear;
    private String baseMonth;
    private int currTotal;
    private int currCnt;
    private int pastTotal;
    private int pastCnt;
}
