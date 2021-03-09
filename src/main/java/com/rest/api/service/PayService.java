package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.TotalPayDaily;
import com.rest.api.repository.fastcafe_admin.TotalPayDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class PayService {

    private final TotalPayDailyRepository totalPayDailyRepository;

    public Page<TotalPayDaily> totalPayDailes(int branch_id, Date startdate, Date enddate, int page, int size) {
        return totalPayDailyRepository.findByBranchIdAndIndexRegdateBetween(branch_id, startdate, enddate, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "indexRegdate")));
    }
}
