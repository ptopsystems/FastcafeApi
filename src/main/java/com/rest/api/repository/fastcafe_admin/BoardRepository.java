package com.rest.api.repository.fastcafe_admin;

import com.rest.api.entity.fastcafe_admin.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
