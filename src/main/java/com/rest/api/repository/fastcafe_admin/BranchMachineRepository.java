package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.BranchMachine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchMachineRepository extends JpaRepository<BranchMachine, Integer> {

    List<BranchMachine> findByBranchIdAndStat(int branch_id, String stat);
}
