package com.rest.api.service;

import com.rest.api.entity.fastcafe.Branch;
import com.rest.api.entity.fastcafe.BranchMachine;
import com.rest.api.repository.fastcafe.BranchMachineRepository;
import com.rest.api.repository.fastcafe.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
