package com.rest.api.aop;

import com.rest.api.entity.fastcafe_log.LogCallApi;
import com.rest.api.jwt.JwtTokenProvider;
import com.rest.api.result.CommonResult;
import com.rest.api.result.DataResult;
import com.rest.api.service.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

//@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final LogService logService;
    private final JwtTokenProvider jwtTokenProvider;

    @Around("execution(* com.rest.api.controller.*.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        long startTime = System.currentTimeMillis();

        LogCallApi logCallApi = new LogCallApi();

        if(requestAttributes != null){
            HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();

            String token = jwtTokenProvider.getToken(req);
            if(StringUtils.hasText(token)){
                logCallApi.setAdminId(Integer.parseInt(jwtTokenProvider.getAdminPk(token)));
                logCallApi.setBranchId(Integer.parseInt(jwtTokenProvider.getBranchPk(token)));
            }
            logCallApi.setCallUri(req.getRequestURI());
            String controllerName = pjp.getSignature().getDeclaringTypeName();
            logCallApi.setControllerName(controllerName.substring(controllerName.lastIndexOf(".")+1));
            if(!logCallApi.getCallUri().contains("login")
                    && !logCallApi.getCallUri().contains("password")){
                String params = req.getParameterMap()
                        .entrySet()
                        .stream()
                        .map(p -> String.format("\"%s\":%s", p.getKey(), String.join(",", p.getValue())))
                        .collect(Collectors.joining(", "));
                if(StringUtils.hasText(params)){
                    logCallApi.setParams("{" + params + "}");
                }
            }
        }

        Object result = pjp.proceed();

        if(logCallApi.getCallUri().contains("login")){
            String token = String.valueOf(((DataResult)result).getData().get("accessToken"));
            logCallApi.setAdminId(Integer.parseInt(jwtTokenProvider.getAdminPk(token)));
            logCallApi.setBranchId(Integer.parseInt(jwtTokenProvider.getBranchPk(token)));
        }
        logCallApi.setStatus(((CommonResult)result).getCode());
        logCallApi.setExcuteTime((int)(System.currentTimeMillis() - startTime));
        logService.saveLogCallApi(logCallApi);
        return result;
    }


    @AfterThrowing(value = "execution(* com.rest.api.controller.*.*(..))", throwing = "e")
    public void afterThrowingException(JoinPoint jp, Exception e){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        LogCallApi logCallApi = new LogCallApi();

        if(requestAttributes != null){
            HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();

            String token = jwtTokenProvider.getToken(req);
            if(StringUtils.hasText(token)){
                logCallApi.setAdminId(Integer.parseInt(jwtTokenProvider.getAdminPk(token)));
                logCallApi.setBranchId(Integer.parseInt(jwtTokenProvider.getBranchPk(token)));
            }
            logCallApi.setCallUri(req.getRequestURI());
            String controllerName = jp.getSignature().getDeclaringTypeName();
            logCallApi.setControllerName(controllerName.substring(controllerName.lastIndexOf(".")+1));
            if(!logCallApi.getCallUri().contains("login")
                    && !logCallApi.getCallUri().contains("password")){
                String params = req.getParameterMap()
                        .entrySet()
                        .stream()
                        .map(p -> String.format("\"%s\":%s", p.getKey(), String.join(",", p.getValue())))
                        .collect(Collectors.joining(", "));
                if(StringUtils.hasText(params)){
                    logCallApi.setParams("{" + params + "}");
                }
            }
        }

        logCallApi.setStatus(500);
        String memo = e.toString();
        logCallApi.setMemo(memo.substring(memo.lastIndexOf(".")+1));

        logService.saveLogCallApi(logCallApi);
    }

}
