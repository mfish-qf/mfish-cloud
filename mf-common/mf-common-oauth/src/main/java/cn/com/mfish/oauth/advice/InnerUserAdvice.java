package cn.com.mfish.oauth.advice;

import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.utils.ServletUtils;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.oauth.annotation.InnerUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * @author ：qiufeng
 * @description：内部用户校验切面
 * @date ：2021/12/3 11:28
 */
@Aspect
@Component
public class InnerUserAdvice {
    @Around("@annotation(innerUser)")
    public Object innerAround(ProceedingJoinPoint point, InnerUser innerUser) throws Throwable {
        String source = ServletUtils.getRequest().getHeader(CredentialConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals("inner", source)) {
            throw new RuntimeException("没有内部访问权限，不允许访问");
        }

        // 用户信息验证
        if (innerUser.validateUser()) {
            throw new RuntimeException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }
}
