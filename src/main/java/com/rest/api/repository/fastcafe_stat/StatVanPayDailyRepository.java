package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatVanPayDaily;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayDailyGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StatVanPayDailyRepository extends JpaRepository<StatVanPayDaily, Integer> {
    @Query(value = "select max(indexRegdate) from stat_van_pay_daily where branch_id=:branch_id", nativeQuery = true)
    Date getMaxIndexRegdate(@Param(value = "branch_id") int branch_id);


    @Query(value = "select d.indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from stat_van_pay_daily d " +
            "where d.branch_id=:branch_id and d.indexRegdate between :startdate and :enddate " +
            "group by d.indexRegdate " +
            "order by d.indexRegdate desc ", nativeQuery = true)
    List<IStatVanPayDailyGroupDTO> listGroupByBranchId(
            @Param(value = "branch_id") int branch_id
            , @Param(value = "startdate") Date startdate
            , @Param(value = "enddate") Date enddate);

    @Query(value = "select d.indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from stat_van_pay_daily d " +
            "where d.branch_id=:branch_id and d.indexRegdate between :startdate and :enddate ", nativeQuery = true)
    IStatVanPayDailyGroupDTO getGroupByBranchId(
            @Param(value = "branch_id") int branch_id
            , @Param(value = "startdate") Date startdate
            , @Param(value = "enddate") Date enddate);

    @Query(value = "select d.indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from stat_van_pay_daily d " +
            "where d.branch_id=:branch_id and d.indexRegdate=:indexRegdate ", nativeQuery = true)
    IStatVanPayDailyGroupDTO findByBranchIdAndIndexRegdate(int branch_id, Date indexRegdate);
}
