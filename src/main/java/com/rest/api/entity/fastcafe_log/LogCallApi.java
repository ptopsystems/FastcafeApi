package com.rest.api.entity.fastcafe_log;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_call_api")
public class LogCallApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "admin_id")
    private int adminId;
    @Column(name = "branch_id")
    private int branchId;
    private String callUri;
    private String controllerName;
    private String params;
    private int status;
    private int excuteTime;
    private String memo;
    private Timestamp regdate;
}
