package com.rest.api.service;

import com.rest.api.entity.fastcafe_log.LogNoticeRead;
import com.rest.api.repository.fastcafe_log.LogNoticeReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogNoticeReadRepository logNoticeReadRepository;
    @Transactional
    public Optional<LogNoticeRead> findByNoticeIdAndAdminId(Integer notice_id, Integer admin_id) {
        return logNoticeReadRepository.findByNoticeIdAndAdminId(notice_id, admin_id);
    }

    @Transactional
    public LogNoticeRead saveLogNoticeRead(LogNoticeRead logNoticeRead) {
        return logNoticeReadRepository.save(logNoticeRead);
    }
}
