package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Branch findByBusinessLicenseAndStat(String memListNo, String stat);
}
