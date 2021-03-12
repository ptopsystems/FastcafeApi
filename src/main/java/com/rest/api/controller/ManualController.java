package com.rest.api.controller;

import com.rest.api.entity.fastcafe_admin.Manual;
import com.rest.api.entity.fastcafe_admin.dto.ManualDTO;
import com.rest.api.entity.fastcafe_admin.dto.ManualDetailDTO;
import com.rest.api.exception.ManualNotFoundException;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.ManualService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class ManualController {

    private final ManualService manualService;

    @GetMapping("/manual")
    public CommonResult manual(){
        List<Manual> manuals = manualService.list();
        return DataResult.Success("manuals", manuals.stream().map(ManualDTO::new));
    }


    @GetMapping("/manual/{id}")
    public CommonResult manualDetail(
            @PathVariable int id
    ){
        Manual manual = manualService.get(id).orElseThrow(ManualNotFoundException::new);
        return DataResult.Success("manual", new ManualDetailDTO(manual));
    }

}
