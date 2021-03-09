package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.Admin;
import com.rest.api.exception.AdminNotFoundException;
import com.rest.api.repository.fastcafe_admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String adminPk) throws UsernameNotFoundException {
        return adminRepository.findById(Integer.valueOf(adminPk)).orElseThrow(AdminNotFoundException::new);
    }

    @Transactional
    public Optional<Admin> fintByAccount(String account) {
        return adminRepository.findFisrtByAccountAndStat(account, "1000");
    }

    @Transactional
    public Admin save(Admin admin) { return adminRepository.save(admin); }
}

