package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findByStat(String stat, Pageable pageable);
}
