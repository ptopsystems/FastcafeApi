package com.rest.api.entity.fastcafe_admin.dto;

import com.rest.api.entity.fastcafe_admin.Manual;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManualDTO {
    private int id;
    private String title;
    private String version;
    private String attachFileUrl;
    private String originFileName;

    public ManualDTO(Manual manual){
        this.id = manual.getId();
        this.title = manual.getTitle();
        this.version = manual.getVersion();
        this.attachFileUrl = manual.getAttachFileUrl();
        this.originFileName = manual.getOriginFileName();
    }
}
