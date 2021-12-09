package cn.com.mfish.oauth.advice;

import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.utils.ServletUtils;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.oauth.annotation.InnerUser;
import cn.com.mfish.oauth.common.Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


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
        HttpServletRequest request = ServletUtils.getRequest();
        String source = request.getHeader(CredentialConstants.FROM_SOURCE);
        // 内部请求验证
        if (CredentialConstants.INNER.equals(source)&&!innerUser.validateUser()) {
            return point.proceed();
        }
        String token = Utils.getAccessToken(request);


        // 用户信息验证
//        if (innerUser.validateUser()) {
//            throw new RuntimeException("没有设置用户信息，不允许访问 ");
//        }
        return point.proceed();
    }
}
