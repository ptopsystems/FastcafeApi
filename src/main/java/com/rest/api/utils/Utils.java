package com.rest.api.utils;

import com.rest.api.exception.MultipartFileNotAllowedTypeException;
import com.rest.api.exception.MultipartFileNotMathcedTypeException;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static boolean uploadFileCheck(MultipartFile file){
        //
        if(file == null || file.isEmpty()) return false;

        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring( fileName.lastIndexOf( "." ) ).toLowerCase();
        String fileContentType = file.getContentType();

        if("plain/text".equalsIgnoreCase(fileContentType)){
            throw new MultipartFileNotMathcedTypeException();
        } else if(fileExtensionName.contains("asp") || fileExtensionName.contains("php")
                || fileExtensionName.contains("jsp") || fileExtensionName.contains("htm")
                || fileExtensionName.contains("html") || fileExtensionName.contains("txt")
                || fileExtensionName.contains("cer") || fileExtensionName.contains("exe")
        ){
            throw new MultipartFileNotAllowedTypeException();
        } else if(fileContentType.startsWith("image/")
                && !fileExtensionName.contains("jpeg") && !fileExtensionName.contains("jpg")
                && !fileExtensionName.contains("gif") && !fileExtensionName.contains("png")
        ){
            throw new MultipartFileNotAllowedTypeException();
        } else if(fileName.lastIndexOf(".") < 0){
            throw new MultipartFileNotAllowedTypeException();
        } else {
            return true;
        }
    }
}
