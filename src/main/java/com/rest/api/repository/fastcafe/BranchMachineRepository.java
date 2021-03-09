package com.rest.api.repository.fastcafe;

import com.rest.api.entity.fastcafe.Admin;
import com.rest.api.entity.fastcafe.BranchMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchMachineRepository extends JpaRepository<BranchMachine, Integer> {

    List<BranchMachine> findByBranchIdAndStat(int branch_id, String stat);
}
