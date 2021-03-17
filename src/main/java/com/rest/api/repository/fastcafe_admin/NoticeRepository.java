package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    @Query(value = "select n from Notice n where (n.title like %:searchValue% or n.content like %:searchValue%) and n.stat=:stat ")
    Page<Notice> findByStat(String searchValue, String stat, Pageable pageable);
}
