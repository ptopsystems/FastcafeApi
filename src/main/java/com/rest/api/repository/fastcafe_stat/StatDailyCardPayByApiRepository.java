package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatDailyCardPayByApi;
import com.rest.api.entity.fastcafe_stat.dto.IStatWeeklyCardPayByApiDTO;
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

    @Query(value = "select min(indexRegdate) as startdate, max(indexRegdate) as enddate, ifnull(sum(total), 0) as total, ifnull(sum(totalCnt), 0) as totalCnt " +
            "from stat_daily_cardpaybyapi " +
            "where branch_id=:branch_id and indexRegdate between date_add(:basedate, interval -6 day) and :basedate ", nativeQuery = true)
    IStatWeeklyCardPayByApiDTO findWeekSum(@Param(value = "branch_id") int branch_id,@Param(value = "basedate") Date basedate);
}
