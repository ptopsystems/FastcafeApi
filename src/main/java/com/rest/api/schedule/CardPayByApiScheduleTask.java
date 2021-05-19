package com.rest.api.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rest.api.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class CardPayByApiScheduleTask {

    @Value("${cardpay.server}")
    private String server;

    private final ScheduleService scheduleService;

    @Scheduled(cron = "0 10 6/3 * * *", zone = "Asia/Seoul")
    public void insertCardPayApi() throws JsonProcessingException {
        if(server.equals("real")) {
            scheduleService.insertCardPayApi();
        }
    }

}
