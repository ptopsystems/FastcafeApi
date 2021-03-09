package com.rest.api.repository.fastcafe;

import com.rest.api.entity.fastcafe.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
}
