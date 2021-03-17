package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.VanPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface VanPayRepository extends JpaRepository<VanPay, Integer> {
    @Query(value = "select max(v.indexRegdate) from VanPay v where v.branchId=:branch_id ")
    Date getMaxIndexRegdate(@Param("branch_id") int branch_id);

    @Query(value = "select v from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate " +
            "   and v.branchMachine.machineType=:machineType ")
    Page<VanPay> list(
            @Param("branch_id") int branch_id
            , @Param("startdate") Date startdate
            , @Param("enddate") Date enddate
            , @Param("machineType") String machineType
            , Pageable pageable);

    @Query(value = "select v from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate ")
    Page<VanPay> list(
            @Param("branch_id") int branch_id
            , @Param("startdate") Date startdate
            , @Param("enddate") Date enddate
            , Pageable pageable);

    @Query(value = "select v from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate " +
            "   and v.branchMachineId=:branch_machine_id ")
    Page<VanPay> list(
            @Param("branch_id") int branch_id
            , @Param("startdate") Date startdate
            , @Param("enddate") Date enddate
            , @Param("branch_machine_id") int branch_machine_id
            , Pageable pageable);

    @Query(value = "select sum(v.payMoney) from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate " +
            "   and v.branchMachine.machineType=:machineType ")
    Long sumPayMoney(
            @Param("branch_id") int branch_id
            , @Param("startdate") Date startdate
            , @Param("enddate") Date enddate
            , @Param("machineType") String machineType);

    @Query(value = "select sum(v.payMoney) from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate ")
    Long sumPayMoney(int branch_id, Date startdate, Date enddate);

    @Query(value = "select sum(v.payMoney) from VanPay v " +
            "where v.branchId=:branch_id " +
            "   and v.indexRegdate between :startdate and :enddate " +
            "   and v.branchMachineId=:branch_machine_id ")
    Long sumPayMoney(int branch_id, Date startdate, Date enddate, int branch_machine_id);
}
