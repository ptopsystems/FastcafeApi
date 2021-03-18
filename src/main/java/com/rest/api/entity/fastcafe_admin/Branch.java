package com.rest.api.entity.fastcafe_admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String branchName;
    private String joinType;
    private String operateType;
    private Date opendate;
    private Date closedate;
    private String ownerName;
    private String ownerTel;
    private String businessLicense;
    private String address;
    private String addressDetail;
    private String stat;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp regdate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp moddate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    @Where(clause = "stat = '1000'")
    private List<BranchOperateDetail> branchOperateDetails;
}
