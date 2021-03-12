package com.rest.api.entity.fastcafe_admin.dto;

import com.rest.api.entity.fastcafe_admin.Manual;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManualDetailDTO extends ManualDTO {

    private String content;

    public ManualDetailDTO(Manual manual) {
        super(manual);
        this.content = manual.getContent();
    }
}
