package cn.com.mfish.oauth.remote;

import cn.com.mfish.common.core.constants.Constants;
import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.constants.ServiceConstants;
import cn.com.mfish.common.core.web.AjaxTResult;
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
@FeignClient(contextId = "remoteUserService", value = ServiceConstants.OAUTH_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 根据token获取用户信息
     *
     * @param token
     * @param origin
     * @return
     */
    @GetMapping("/user/info")
    AjaxTResult<UserInfo> getUserInfo(@RequestHeader(CredentialConstants.FROM_SOURCE) String origin, @RequestHeader(Constants.AUTHENTICATION) String token);

    /**
     * 获取当前用户信息 web浏览器通过oauth2登录可以直接请求
     *
     * @param origin
     * @return
     */
    @GetMapping("/user/current")
    AjaxTResult<UserInfo> getUserInfo(@RequestHeader(CredentialConstants.FROM_SOURCE) String origin);
}