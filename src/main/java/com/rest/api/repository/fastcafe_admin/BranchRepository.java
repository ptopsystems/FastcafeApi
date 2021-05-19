package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Integer> {

    @Query(value = "select b.* from branch b where replace(b.businessLicense, '-', '')=:memListNo and b.stat=:stat", nativeQuery = true)
    Branch findByBusinessLicenseAndStat(@Param(value = "memListNo") String memListNo, @Param(value = "stat") String stat);

    List<Branch> findByRegisterCardPayApiAndStat(boolean registerCardPayApi, String stat);
}
