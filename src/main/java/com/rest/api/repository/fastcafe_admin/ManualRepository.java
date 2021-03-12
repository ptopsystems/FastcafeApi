package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Manual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManualRepository extends JpaRepository<Manual, Integer> {
    List<Manual> findByStat(String stat);
}
