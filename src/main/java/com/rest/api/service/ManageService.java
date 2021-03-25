package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.BranchManageUrl;
import com.rest.api.repository.fastcafe_admin.BranchManageUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ManageService {

    private final BranchManageUrlRepository branchManageUrlRepository;

    public List<BranchManageUrl> list(int branchId) {
        return branchManageUrlRepository.findByBranchId(branchId);
    }
}
