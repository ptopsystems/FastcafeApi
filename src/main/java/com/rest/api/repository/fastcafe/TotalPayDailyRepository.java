package com.rest.api.repository.fastcafe;

import com.rest.api.entity.fastcafe.TotalPayDaily;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface TotalPayDailyRepository extends JpaRepository<TotalPayDaily, Integer> {


    Page<TotalPayDaily> findByBranchIdAndIndexRegdateBetween(int branch_id, Date startdate, Date enddate, Pageable pageable);
}
