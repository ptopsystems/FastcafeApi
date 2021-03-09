package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findFisrtByAccountAndStat(String account, String stat);
}
