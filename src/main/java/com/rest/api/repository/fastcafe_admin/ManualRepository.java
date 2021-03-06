package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Manual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManualRepository extends JpaRepository<Manual, Integer> {
    Optional<Manual> findByIdAndStat(int id, String stat);

    Manual findFirstByMachineModelAndStatOrderByVersionDesc(String machineModel, String stat);

    List<Manual> findByManualTypeAndStat(String manualType, String stat);
}
