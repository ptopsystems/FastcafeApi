package com.rest.api.entity.fastcafe_stat.dto;

import java.sql.Date;

public interface IStatWeeklyCardPayByApiDTO {

    Date getStartdate();
    Date getEnddate();
    int getTotal();
    int getTotalCnt();
}
