package com.rest.api.entity.fastcafe_admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.entity.fastcafe_admin.Notice;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class NoticeDTO {
    private int id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp regdate;
    private boolean isRead;

    public NoticeDTO(Notice notice){
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.regdate = notice.getRegdate();
        this.isRead = notice.getIsRead();
    }
}
