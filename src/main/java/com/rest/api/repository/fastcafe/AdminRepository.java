package com.rest.api.repository.fastcafe;

import com.rest.api.entity.fastcafe.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findFisrtByAccountAndStat(String account, String stat);
}
