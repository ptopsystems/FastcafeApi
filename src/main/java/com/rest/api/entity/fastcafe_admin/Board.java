package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@With
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "branch_id")
    private int branchId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "admin_id")
    private int adminId;
    private String title;
    private String contents;
    private String accidentTime;
    private String attachImgUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "answer_member_id")
    private int answerMemberId;
    private String answer;
    private String answerFileUrl;
    private String answerFileName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp answerdate;
    private String stat;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regdate;
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp moddate;
}
