package com.rest.api.entity.fastcafe_admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.entity.fastcafe_admin.Board;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class BoardDTO {
    private int id;
    private String title;
    private String stat;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp regdate;

    public BoardDTO(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.stat = board.getStat();
        this.regdate = board.getRegdate();
    }
}
