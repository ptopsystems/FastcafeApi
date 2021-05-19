package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatMonthlyCardPayByApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatMonthlyCardPayByApiRepository extends JpaRepository<StatMonthlyCardPayByApi, Integer> {

    @Query(value = "select m.* " +
            "from stat_monthly_cardpaybyapi m " +
            "where m.branch_id=:branch_id " +
            "   and concat(m.baseYear, m.baseMonth) between date_format(adddate(concat(:baseYear, :baseMonth, '01'), INTERVAL -2 MONTH), '%Y%m') and concat(:baseYear, :baseMonth) " +
            "group by m.baseYear, m.baseMonth", nativeQuery = true)
    List<StatMonthlyCardPayByApi> findByBranchIdAndBaseYearAndBaseMonth(@Param(value = "branch_id") int branchId, String baseYear, String baseMonth);

    StatMonthlyCardPayByApi findOneByBranchIdAndBaseYearAndBaseMonth(int branchId, String baseYear, String baseMonth);
}
