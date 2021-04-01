package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@With
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "manual")
public class Manual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String manualType;
    private String machineModel;
    private String version;
    private String title;
    private String attachFileUrl;
    private String originFileName;
    private String stat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp regdate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp moddate;
}
