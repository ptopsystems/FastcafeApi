package com.rest.api.repository.fastcafe_log;

import com.rest.api.entity.fastcafe_log.LogNoticeRead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogNoticeReadRepository extends JpaRepository<LogNoticeRead, Integer> {

    Optional<LogNoticeRead> findByNoticeIdAndAdminId(Integer notice_id, Integer admin_id);
}
