package com.rest.api.entity.fastcafe_stat.dto;

import java.sql.Date;

public interface IStatVanPayWeeklyGroupDTO {

    String getBaseWeek();
    Date getStartdate();
    Date getEnddate();
    Long getPayMoney();
    Long getPayCnt();
}
