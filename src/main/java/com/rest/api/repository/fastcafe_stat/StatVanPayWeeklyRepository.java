package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatVanPayWeekly;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayWeeklyGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatVanPayWeeklyRepository extends JpaRepository<StatVanPayWeekly, Integer> {
    @Query(value = "select w.baseWeek, concat(w.startdate, ' ~ ', w.enddate) as periodDays, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from stat_van_pay_weekly w " +
            "where w.branch_id=:branch_id " +
            "   and w.branch_machine_id=:branch_machine_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "group by w.baseWeek ", nativeQuery = true)
    List<IStatVanPayWeeklyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("branch_machine_id") int branch_machine_id);

    @Query(value = "select w.baseWeek, concat(w.startdate, ' ~ ', w.enddate) as periodDays, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from stat_van_pay_weekly w " +
            "join fastcafe_admin.branch_machine m on w.branch_machine_id=m.id " +
            "where w.branch_id=:branch_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "   and m.machineType=:machineType " +
            "group by w.baseWeek ", nativeQuery = true)
    List<IStatVanPayWeeklyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("machineType") String machineType);

    @Query(value = "select w.baseWeek, concat(w.startdate, ' ~ ', w.enddate) as periodDays, sum(w.payMoney) as payMoney, sum(w.payCnt) as payCnt " +
            "from stat_van_pay_weekly w " +
            "where w.branch_id=:branch_id " +
            "   and w.baseYear=:baseYear " +
            "   and w.baseMonth=:baseMonth " +
            "group by w.baseWeek ", nativeQuery = true)
    List<IStatVanPayWeeklyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth);
}
