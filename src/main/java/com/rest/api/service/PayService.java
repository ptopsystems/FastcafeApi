package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.CardPayByApi;
import com.rest.api.entity.fastcafe_admin.VanPay;
import com.rest.api.repository.fastcafe_admin.CardPayByApiRepository;
import com.rest.api.repository.fastcafe_admin.VanPayRepository;
import com.rest.api.repository.fastcafe_admin.specification.SpecificationCardPayByApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class PayService {

    private final VanPayRepository vanPayRepository;
    private final CardPayByApiRepository cardPayByApiRepository;

    public Date getMaxIndexRegdate(int branch_id) {
        return vanPayRepository.getMaxIndexRegdate(branch_id);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public CardPayByApi insertCardPayByApi(CardPayByApi entity) {
        return cardPayByApiRepository.save(entity);
    }

    @Transactional
    public Date getMaxTransDateForCardPayByApi(int branchId) {
        return cardPayByApiRepository.getMaxTransDate(branchId);
    }

    @Transactional
    public Page<CardPayByApi> listCardPayByApi(int branchId, Date startdate, Date enddate, String payType, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "transDate", "transTime"));
        return cardPayByApiRepository.findAll(
                Specification.where(SpecificationCardPayByApi.eqaulBranchId(branchId))
                .and(SpecificationCardPayByApi.betweenTransDate(startdate, enddate)
                .and(SpecificationCardPayByApi.checkPayType(payType)))
                , pageable);
    }

    @Transactional
    public CardPayByApi getCardPayByApi(int branchId, Date transDate, String transTime, String cardNm, String cardNo, String appNo) {
        return cardPayByApiRepository.findByBranchIdAndTransDateAndTransTimeAndCardNmAndCardNoAndAppNo(branchId, transDate, transTime, cardNm, cardNo, appNo);
    }
}
