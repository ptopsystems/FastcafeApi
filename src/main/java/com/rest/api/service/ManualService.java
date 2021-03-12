package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.Manual;
import com.rest.api.repository.fastcafe_admin.ManualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManualService {

    private final ManualRepository manualRepository;

    @Transactional
    public Optional<Manual> get(int id) {
        return manualRepository.findById(id);
    }

    @Transactional
    public List<Manual> list() {
        return manualRepository.findByStat("1000");
    }
}
