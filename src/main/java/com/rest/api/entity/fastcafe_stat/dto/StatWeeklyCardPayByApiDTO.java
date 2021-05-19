package com.rest.api.entity.fastcafe_stat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class StatWeeklyCardPayByApiDTO {
    private String baseWeek;
    private Date startdate;
    private Date enddate;
    private int currTotal;
    private int currCnt;
    private Date pastStartdate;
    private Date pastEnddate;
    private int pastTotal;
    private int pastCnt;
}
