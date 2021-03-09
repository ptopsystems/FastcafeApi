package com.rest.api.entity.fastcafe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "branch_machine")
public class BranchMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "branch_id")
    private int branchId;
    private String machineType;
    private String machineName;
    private String machinePk;
    private String deviceNo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String stat;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp regdate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp moddate;
}
