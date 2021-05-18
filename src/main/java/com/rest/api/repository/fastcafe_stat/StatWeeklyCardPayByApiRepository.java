package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatWeeklyCardPayByApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StatWeeklyCardPayByApiRepository extends JpaRepository<StatWeeklyCardPayByApi, Integer> {

    List<StatWeeklyCardPayByApi> findByBranchIdAndBaseYearAndBaseMonth(int branchId, String baseYear, String baseMonth);

    @Query(value = "select s from StatWeeklyCardPayByApi s where s.branchId=:branch_id and :basedate between s.startdate and s.enddate")
    StatWeeklyCardPayByApi findByIdAndBasedateBetweenStartdateAndEnddate(@Param(value = "branch_id") int branchId, @Param(value = "basedate") Date basedate);
}
