package com.rest.api.service;

import com.rest.api.entity.fastcafe_admin.Board;
import com.rest.api.repository.fastcafe_admin.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Page<Board> listWithPagable(int branch_id, int admin_id, String stat, int page, int size) {
        Page<Board> boards = null;
        if(StringUtils.hasText(stat)){
            boards = boardRepository.findByBranchIdAndAdminIdAndStat(branch_id, admin_id, stat, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")));
        } else {
            boards = boardRepository.findByBranchIdAndAdminIdAndStatNot(branch_id, admin_id, "9001", PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")));
        }
        return boards;
    }

    @Transactional
    public Optional<Board> find(int id, int admin_id) {
        return boardRepository.findByIdAndAdminId(id, admin_id);
    }

    @Transactional
    public Board save(Board board) {
        return boardRepository.save(board);
    }
}

