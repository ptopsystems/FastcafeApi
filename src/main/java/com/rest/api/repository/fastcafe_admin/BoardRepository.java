package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByBranchIdAndAdminIdAndStat(int branch_id, int admin_id, String stat, Pageable pageable);

    Page<Board> findByBranchIdAndAdminIdAndStatNot(int branch_id, int admin_id, String stat, Pageable pageable);

    Optional<Board> findByIdAndAdminId(int id, int admin_id);
}
