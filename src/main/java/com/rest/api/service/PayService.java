package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.VanPay;
import com.rest.api.repository.fastcafe_admin.VanPayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class PayService {

    private final VanPayRepository vanPayRepository;

    public Date getMaxIndexRegdate(int branch_id) {
        return vanPayRepository.getMaxIndexRegdate(branch_id);
    }

    public Page<VanPay> listVanPay(int branch_id, Date startdate, Date enddate, String machineType, int branch_machine_id, int page, int size) {
        Page<VanPay> vanPays = null;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "paydate"));
        if(branch_machine_id == 0) {
            if(StringUtils.hasText(machineType)){
                vanPays = vanPayRepository.list(branch_id, startdate, enddate, machineType, pageable);
            } else {
                vanPays = vanPayRepository.list(branch_id, startdate, enddate, pageable);
            }
        } else {
            vanPays = vanPayRepository.list(branch_id, startdate, enddate, branch_machine_id, pageable);
        }

        return vanPays;
    }

    public Long getSumPayMoney(int branch_id, Date startdate, Date enddate, String machineType, int branch_machine_id) {
        if(branch_machine_id == 0) {
            if(StringUtils.hasText(machineType)){
                return vanPayRepository.sumPayMoney(branch_id, startdate, enddate, machineType);
            } else {
                return vanPayRepository.sumPayMoney(branch_id, startdate, enddate);
            }
        } else {
            return vanPayRepository.sumPayMoney(branch_id, startdate, enddate, branch_machine_id);
        }
    }
}
