package com.rest.api.entity.fastcafe_stat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class StatVanPayWeeklyTotalStatDTO {
    private String baseWeek;
    private Date startdate;
    private Date enddate;
    private Long currMoney;
    private Long currCnt;
    private Date pastStartdate;
    private Date pastEnddate;
    private Long pastMoney;
    private Long pastCnt;
}

