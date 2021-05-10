package com.rest.api.repository.fastcafe_log;

import com.rest.api.entity.fastcafe_log.LogCardPayByApiData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface LogCardPayByApiDataRepository extends JpaRepository<LogCardPayByApiData, Integer> {
    LogCardPayByApiData findFirstByBranchIdAndBasedateOrderByIdDesc(int branchId, Date basedate);
}
