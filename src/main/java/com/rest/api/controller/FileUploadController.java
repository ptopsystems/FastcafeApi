package com.rest.api.controller;

import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.utils.S3Uploader;
import com.rest.api.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class FileUploadController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload/file")
    public CommonResult uploadFile(
            @RequestPart(name = "file") MultipartFile file
    ){
        try {
            String fileUrl = "";
            String fileName = file.getOriginalFilename();
            if(Utils.uploadFileCheck(file)){
                fileUrl = s3Uploader.upload(file);
            }

            if(StringUtils.hasText(fileUrl)){
                HashMap<String, String> result = new HashMap<>();
                result.put("fileUrl", fileUrl);
                result.put("fileName", fileName);
                return DataResult.Success("file", result);
            } else {
                return CommonResult.Fail(500, "업로드 중 오류가 발생되었습니다.");
            }
        } catch(Exception e){
            e.printStackTrace();
            return CommonResult.Fail(500, "업로드 중 오류가 발생되었습니다.");
        }
    }

}
