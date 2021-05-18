package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface CardPayByApiRepository extends JpaRepository<CardPayByApi, Integer>, JpaSpecificationExecutor<CardPayByApi> {
    @Query(value = "select max(c.transDate) from CardPayByApi c where c.branchId=:branch_id")
    Date getMaxTransDate(@Param(value = "branch_id") int branch_id);

    CardPayByApi findByBranchIdAndTransDateAndTransTimeAndCardNmAndCardNoAndAppNoAndAppClassNm(int branchId, Date transDate, String transTime, String cardNm, String cardNo, String appNo, String appClassNm);
}
