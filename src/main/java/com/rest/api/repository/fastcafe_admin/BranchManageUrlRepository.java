package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.BranchManageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchManageUrlRepository extends JpaRepository<BranchManageUrl, Integer> {
    List<BranchManageUrl> findByBranchId(int branchId);
}
