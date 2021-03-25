package com.rest.api.entity.fastcafe_admin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "branch_manage_url")
public class BranchManageUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "manage_url_id")
    private int manageUrlId;

    @ManyToOne
    @JoinColumn(name = "manage_url_id", insertable = false, updatable = false)
    private ManageUrl manageUrl;
}
