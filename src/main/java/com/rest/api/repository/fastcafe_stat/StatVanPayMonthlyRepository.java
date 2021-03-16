package com.rest.api.repository.fastcafe_stat;

import com.rest.api.entity.fastcafe_stat.StatVanPayMonthly;
import com.rest.api.entity.fastcafe_stat.dto.IStatVanPayMonthlyGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatVanPayMonthlyRepository extends JpaRepository<StatVanPayMonthly, Integer> {
    @Query(value = "select m.baseYear, m.baseMonth, sum(m.payMoney) as payMoney, sum(m.payCnt) as payCnt " +
            "from stat_van_pay_monthly m " +
            "where m.branch_id=:branch_id " +
            "   and concat(m.baseYear, m.baseMonth) between date_format(adddate(concat(:baseYear, :baseMonth, '01'), INTERVAL -2 MONTH), '%Y%m') and concat(:baseYear, :baseMonth) " +
            "group by m.baseYear, m.baseMonth", nativeQuery = true)
    List<IStatVanPayMonthlyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth);

    @Query(value = "select baseYear, baseMonth, sum(payMoney) as payMoney, sum(payCnt) as payCnt " +
            "from stat_van_pay_monthly m " +
            "join fastcafe_admin.branch_machine bm on m.branch_machine_id=bm.id " +
            "where m.branch_id=:branch_id " +
            "   and concat(m.baseYear, m.baseMonth) between date_format(adddate(concat(:baseYear, :baseMonth, '01'), INTERVAL -2 MONTH), '%Y%m') and concat(:baseYear, :baseMonth) " +
            "   and bm.machineType=:machineType " +
            "group by m.baseYear, m.baseMonth ", nativeQuery = true)
    List<IStatVanPayMonthlyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("machineType") String machineType);

    @Query(value = "select m.baseYear, m.baseMonth, sum(m.payMoney) as payMoney, sum(m.payCnt) as payCnt " +
            "from stat_van_pay_monthly m " +
            "where m.branch_id=:branch_id " +
            "   and m.branch_machine_id=:branch_machine_id " +
            "   and concat(m.baseYear, m.baseMonth) between date_format(adddate(concat(:baseYear, :baseMonth, '01'), INTERVAL -2 MONTH), '%Y%m') and concat(:baseYear, :baseMonth) " +
            "group by m.baseYear, m.baseMonth", nativeQuery = true)
    List<IStatVanPayMonthlyGroupDTO> groupByBaseWeek(
            @Param("branch_id") int branch_id
            , @Param("baseYear") String baseYear
            , @Param("baseMonth") String baseMonth
            , @Param("branch_machine_id") int branch_machine_id);
}
