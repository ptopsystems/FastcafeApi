package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.Branch;
import com.rest.api.entity.fastcafe_admin.BranchMachine;
import com.rest.api.repository.fastcafe_admin.BranchMachineRepository;
import com.rest.api.repository.fastcafe_admin.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMachineRepository branchMachineRepository;


    @Transactional
    public Optional<Branch> findById(Integer id) { return branchRepository.findById(id); }

    @Transactional
    public List<BranchMachine> branchMachineFindByBranchId(int branch_id) {
        return branchMachineRepository.findByBranchIdAndStat(branch_id, "1000");
    }

    @Transactional
    public Branch getBranchByBusinessLiscense(String memListNo) {
        return branchRepository.findByBusinessLicenseAndStat(memListNo, "1000");
    }

    @Transactional
    public void save(Branch branch) {
        branch.withModdate(new Timestamp(System.currentTimeMillis()));
        branchRepository.save(branch);
    }

    @Transactional
    public List<Branch> findByRegisterCardPayApi() {
        return branchRepository.findByRegisterCardPayApiAndStat(true, "1000");
    }
}

