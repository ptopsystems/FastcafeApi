package com.rest.api.entity.fastcafe_stat.dto;

import java.sql.Date;

public interface IStatVanPayDailyGroupDTO {
    Date getIndexRegdate();
    Long getPayMoney();
    Long getPayCnt();
}
