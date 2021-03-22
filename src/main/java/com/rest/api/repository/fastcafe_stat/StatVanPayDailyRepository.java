package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatVanPayDaily;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayDailyGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StatVanPayDailyRepository extends JpaRepository<StatVanPayDaily, Integer> {
    @Query(value = "select max(d.indexRegdate) from StatVanPayDaily d where d.branchId=:branch_id")
    Date getMaxIndexRegdate(@Param(value = "branch_id") int branch_id);


    @Query(value = "select d.indexRegdate as indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from StatVanPayDaily d " +
            "where d.branchId=:branch_id and d.indexRegdate between :startdate and :enddate " +
            "group by d.indexRegdate " +
            "order by d.indexRegdate desc ")
    List<IStatVanPayDailyGroupDTO> listGroupByBranchId(
            @Param(value = "branch_id") int branch_id
            , @Param(value = "startdate") Date startdate
            , @Param(value = "enddate") Date enddate);

    @Query(value = "select d.indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from StatVanPayDaily d " +
            "where d.branchId=:branch_id and d.indexRegdate between :startdate and :enddate ")
    IStatVanPayDailyGroupDTO getGroupByBranchId(
            @Param(value = "branch_id") int branch_id
            , @Param(value = "startdate") Date startdate
            , @Param(value = "enddate") Date enddate);

    @Query(value = "select d.indexRegdate, sum(d.payMoney) as payMoney, sum(d.payCnt) as payCnt " +
            "from StatVanPayDaily d " +
            "where d.branchId=:branch_id and d.indexRegdate=:indexRegdate ")
    IStatVanPayDailyGroupDTO findByBranchIdAndIndexRegdate(int branch_id, Date indexRegdate);
}
