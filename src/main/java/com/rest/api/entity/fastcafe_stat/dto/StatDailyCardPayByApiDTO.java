package com.rest.api.entity.fastcafe_stat.dto;

import com.rest.api.entity.fastcafe_stat.StatDailyCardPayByApi;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class StatDailyCardPayByApiDTO {
    private Date indexRegdate;
    private int total;
    private int totalCnt;

    public StatDailyCardPayByApiDTO(StatDailyCardPayByApi cardPayByApi){
        this.indexRegdate = cardPayByApi.getIndexRegdate();
        this.total = cardPayByApi.getTotal();
        this.totalCnt = cardPayByApi.getTotalCnt();
    }
}
