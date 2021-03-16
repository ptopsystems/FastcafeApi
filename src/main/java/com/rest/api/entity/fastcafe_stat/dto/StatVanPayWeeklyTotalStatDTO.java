package com.rest.api.entity.fastcafe_stat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatVanPayWeeklyTotalStatDTO {
    private String baseWeek;
    private String periodDays;
    private Long currMoney;
    private Long currCnt;
    private Long pastMoney;
    private Long pastCnt;
}

