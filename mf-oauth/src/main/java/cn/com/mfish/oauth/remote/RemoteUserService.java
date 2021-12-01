package cn.com.mfish.oauth.remote;

import cn.com.mfish.common.core.constants.ServiceConstants;
import cn.com.mfish.oauth.fallback.RemoteUserFallbackFactory;
import cn.com.mfish.oauth.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：RPC用户服务
 * @author     ：qiufeng
 * @date       ：2021/12/1 17:10
 */
@FeignClient(contextId = "remoteUserService", value = ServiceConstants.OAUTH_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    @GetMapping("/user/info")
    UserInfo getUserInfo();
}
