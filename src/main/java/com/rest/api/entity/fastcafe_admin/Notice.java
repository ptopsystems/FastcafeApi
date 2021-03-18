package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.entity.fastcafe_log.LogNoticeRead;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String contents;
    private String attachFileUrl;
    private String originFileName;
    private int readCnt;
    private String stat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regdate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp moddate;

    @Transient
    private Boolean isRead;

    public void setIsRead(LogNoticeRead logNoticeRead){
        if(logNoticeRead == null) this.isRead = false;
        else this.isRead = logNoticeRead.getId() != 0;
    }

}
