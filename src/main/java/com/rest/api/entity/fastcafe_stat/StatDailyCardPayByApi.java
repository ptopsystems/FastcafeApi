package com.rest.api.entity.fastcafe_stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stat_daily_cardpaybyapi")
public class StatDailyCardPayByApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Column(name = "branch_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int branchId;
    private Date indexRegdate;
    private int total;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int approve;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int cancel;
    private int totalCnt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int approveCnt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int cancelCnt;
}
