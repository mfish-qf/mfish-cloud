package cn.com.mfish.oauth.remote;

import cn.com.mfish.common.core.constants.Constants;
import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.constants.ServiceConstants;
import cn.com.mfish.oauth.fallback.RemoteUserFallbackFactory;
import cn.com.mfish.oauth.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ：qiufeng
 * @description：RPC用户服务
 * @date ：2021/12/1 17:10
 */
@FeignClient(contextId = "remoteUserService",value = ServiceConstants.OAUTH_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    @GetMapping("/user/info")
    UserInfo getUserInfo(@RequestHeader(Constants.AUTHENTICATION) String token, @RequestHeader(CredentialConstants.FROM_SOURCE) String origin);
}