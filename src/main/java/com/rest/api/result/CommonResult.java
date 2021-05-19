package com.rest.api.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    private boolean status;
    private int code;
    private String msg;

   public CommonResult(){
       this.status = true;
       this.code = 200;
       this.msg = "성공하였습니다.";
   }

   public CommonResult(int code, String msg){
       this.status = code == 200;
       this.code = code;
       this.msg = msg;
   }

   public static CommonResult Success(){
       return new CommonResult();
   }
   public static CommonResult Success(int code, String msg){ return new CommonResult(code, msg); }
   public static CommonResult Fail(int code, String msg){
       return new CommonResult(code, msg);
   }
}
