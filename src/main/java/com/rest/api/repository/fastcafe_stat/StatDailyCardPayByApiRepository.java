package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatDailyCardPayByApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StatDailyCardPayByApiRepository extends JpaRepository<StatDailyCardPayByApi, Integer> {

    List<StatDailyCardPayByApi> findByBranchIdAndIndexRegdateBetweenOrderByIndexRegdateDesc(int branchId, Date startdate, Date enddate);

    @Query(value = "select max(d.indexRegdate) from StatDailyCardPayByApi d where d.branchId=:branch_id")
    Date getMaxIndexRegdateByBranchId(@Param(value = "branch_id") int branchId);

    StatDailyCardPayByApi findByBranchIdAndIndexRegdate(int branchId, Date basedate);
}
