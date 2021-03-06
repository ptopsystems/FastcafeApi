package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatVanPayWeekly;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayWeeklyGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StatVanPayWeeklyRepository extends JpaRepository<StatVanPayWeekly, Integer> {
    @Query(value = "select w.baseWeek as baseWeek, w.startdate as startdate, w.enddate as enddate, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from StatVanPayWeekly w " +
            "where w.branchId=:branch_id " +
            "   and w.branchMachineId=:branch_machine_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "group by w.baseWeek ")
    List<IStatVanPayWeeklyGroupDTO> listGroupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("branch_machine_id") int branch_machine_id);

    @Query(value = "select w.baseWeek as baseWeek, w.startdate as startdate, w.enddate as enddate, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from stat_van_pay_weekly w " +
            "join fastcafe_admin.branch_machine m on w.branch_machine_id=m.id " +
            "where w.branch_id=:branch_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "   and m.machineType=:machineType " +
            "group by w.baseWeek ", nativeQuery = true)
    List<IStatVanPayWeeklyGroupDTO> listGroupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("machineType") String machineType);

    @Query(value = "select w.baseWeek as baseWeek, w.startdate as startdate, w.enddate as enddate, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from StatVanPayWeekly w " +
            "where w.branchId=:branch_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "group by w.baseWeek ")
    List<IStatVanPayWeeklyGroupDTO> listGroupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth);

    @Query(value = "select w.baseWeek as baseWeek, w.startdate as startdate, w.enddate as enddate, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from StatVanPayWeekly w " +
            "where w.branchId=:branch_id " +
            "   and :basedate between w.startdate and w.enddate ")
    IStatVanPayWeeklyGroupDTO getGroupByBranchId(int branch_id, Date basedate);

    @Query(value = "select max(w.enddate) from StatVanPayWeekly w where w.branchId=:branch_id and w.baseYear=:baseYear and w.baseMonth=:baseMonth ")
    Date getMaxIndexRegdateByBranchIdAndBaseYearAndBaseMonth(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth);
}
