package com.rest.api.entity.fastcafe_admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CardPayByApiDTO {
    private int payMoney;
    private String payType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paydate;
    private String cardNm;
    private String cardNo;
    private String appNo;

    public CardPayByApiDTO(CardPayByApi cardPayByApi) {
        this.payType = cardPayByApi.getAppClassNm();
        this.payMoney = cardPayByApi.getAppAmt();
        this.paydate = LocalDateTime.parse(cardPayByApi.getTransDate() + " " + cardPayByApi.getTransTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss"));
        this.cardNm = cardPayByApi.getCardNm();
        this.cardNo = cardPayByApi.getCardNo();
        this.appNo = cardPayByApi.getAppNo();
    }
}
