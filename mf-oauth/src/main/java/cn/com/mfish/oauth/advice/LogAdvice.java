package cn.com.mfish.oauth.advice;

import com.alibaba.fastjson.JSON;
import cn.com.mfish.oauth.annotation.LogAnnotation;
import cn.com.mfish.oauth.common.Utils;
import cn.com.mfish.oauth.mapper.SSOLogMapper;
import cn.com.mfish.oauth.model.SSOLog;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author qiufeng
 * @date 2020/2/21 19:17
 */
@Aspect
@Component
@Slf4j
public class LogAdvice {
    @Resource
    SSOLogMapper ssoLogMapper;

    ThreadLocal<SSOLog> ssoLogThreadLocal = new ThreadLocal<>();

    @Before("@annotation(cn.com.mfish.oauth.annotation.LogAnnotation)")
    public void doBefore(JoinPoint joinPoint) {
        SSOLog ssoLog = new SSOLog();
        ssoLog.setId(UUID.randomUUID().toString());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ssoLog.setClientId(request.getParameter(OAuth.OAUTH_CLIENT_ID));
        ssoLog.setIp(Utils.getRemoteIP(request));
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String method;
        LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
        method = logAnnotation.value();
        if (StringUtils.isEmpty(method)) {
            ApiOperation apiOperation = methodSignature.getMethod().getDeclaredAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                method = apiOperation.value();
            } else {
                method = methodSignature.getName();
            }
        }
        ssoLog.setInterfaceName(method);
        ssoLogThreadLocal.set(ssoLog);
    }

    @AfterReturning(value = "@annotation(cn.com.mfish.oauth.annotation.LogAnnotation)", returning = "returnValue")
    public void doAfterReturning(Object returnValue) {
        setReturn(0, JSON.toJSONString(returnValue));
    }

    @AfterThrowing(value = "@annotation(cn.com.mfish.oauth.annotation.LogAnnotation)", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        setReturn(1, e.getMessage());
    }

    private void setReturn(int state, String remark) {
        SSOLog ssoLog = ssoLogThreadLocal.get();
        Subject subject = SecurityUtils.getSubject();
        ssoLog.setSessionId(subject.getSession().getId().toString());
        ssoLog.setUserId((String) subject.getPrincipal());
        ssoLog.setState(state);
        ssoLog.setRemark(remark);
        ssoLogMapper.insert(ssoLog);
    }
}
