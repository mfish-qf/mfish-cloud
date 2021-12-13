package cn.com.mfish.oauth.interceptor;

import cn.com.mfish.common.core.constants.Constants;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.oauth.common.Utils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author ：qiufeng
 * @description：Feign请求令牌中继，防止令牌丢失
 * @date ：2021/12/13 10:19
 */
@Component
public class BearerTokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String token = Utils.getAccessToken(requestAttributes.getRequest());
            if (!StringUtils.isEmpty(token)) {
                // 清除token头 避免传染
                requestTemplate.removeHeader(Constants.AUTHENTICATION);
                requestTemplate.header(Constants.AUTHENTICATION, Constants.OAUTH_HEADER_NAME + token);
            }
        }
    }
}
