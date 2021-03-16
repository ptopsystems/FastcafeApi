package com.rest.api.entity.fastcafe_stat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatVanPayMonthlyTotalStatDTO {
    private String baseYear;
    private String baseMonth;
    private Long currMoney;
    private Long currCnt;
    private Long pastMoney;
    private Long pastCnt;
}
