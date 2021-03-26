package com.rest.api.service;

import com.rest.api.entity.fastcafe_log.LogCallApi;
import com.rest.api.entity.fastcafe_log.LogNoticeRead;
import com.rest.api.repository.fastcafe_log.LogCallApiRepository;
import com.rest.api.repository.fastcafe_log.LogNoticeReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogNoticeReadRepository logNoticeReadRepository;
    private final LogCallApiRepository logCallApiRepository;
    @Transactional
    public Optional<LogNoticeRead> findByNoticeIdAndAdminId(Integer notice_id, Integer admin_id) {
        return logNoticeReadRepository.findByNoticeIdAndAdminId(notice_id, admin_id);
    }

    @Transactional
    public LogNoticeRead saveLogNoticeRead(LogNoticeRead logNoticeRead) {
        return logNoticeReadRepository.save(logNoticeRead);
    }

    @Transactional
    public void saveLogCallApi(String adminPk, String branchPk, String callUri, String controllerName, String params, int status, long excuteTime, String memo) {

        LogCallApi logCallApi = LogCallApi.builder()
                .adminId(StringUtils.hasText(adminPk) ? Integer.parseInt(adminPk) : 0)
                .branchId(StringUtils.hasText(branchPk) ? Integer.parseInt(branchPk) : 0)
                .callUri(callUri)
                .controllerName(controllerName)
                .params(params)
                .status(status)
                .excuteTime((int)excuteTime)
                .memo(memo)
                .regdate(new Timestamp(System.currentTimeMillis()))
                .build();

        logCallApiRepository.save(logCallApi);
    }

    @Transactional
    public void saveLogCallApi(LogCallApi logCallApi) {
        logCallApi.setRegdate(new Timestamp(System.currentTimeMillis()));
        logCallApiRepository.save(logCallApi);
    }
}
