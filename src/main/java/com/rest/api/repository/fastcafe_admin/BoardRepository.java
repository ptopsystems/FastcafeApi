package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByBranchIdAndAdminIdAndStat(int branch_id, int admin_id, String stat, Pageable pageable);
    @Query(value = "select b from Board b where b.branchId=:branch_id and b.adminId=:admin_id " +
            "and (b.title like %:searchValue% or b.contents like %:searchValue% or b.answer like %:searchValue%) " +
            "and b.stat=:stat ")
    Page<Board> findByBranchIdAndAdminIdAndStat(
            @Param("branch_id") int branch_id
            , @Param("admin_id") int admin_id
            , @Param("searchValue") String searchValue
            , @Param("stat") String stat, Pageable pageable);

    Page<Board> findByBranchIdAndAdminIdAndStatNot(int branch_id, int admin_id, String stat, Pageable pageable);
    @Query(value = "select b from Board b where b.branchId=:branch_id and b.adminId=:admin_id " +
            "and (b.title like %:searchValue% or b.contents like %:searchValue% or b.answer like %:searchValue%) " +
            "and b.stat != :stat ")
    Page<Board> findByBranchIdAndAdminIdAndStatNot(
            @Param("branch_id") int branch_id
            , @Param("admin_id") int admin_id
            , @Param("searchValue") String searchValue
            , @Param("stat") String stat, Pageable pageable);

    Optional<Board> findByIdAndAdminIdAndStatNot(int id, int admin_id, String stat);
}
