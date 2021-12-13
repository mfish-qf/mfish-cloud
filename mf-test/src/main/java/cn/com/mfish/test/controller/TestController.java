package cn.com.mfish.test.controller;


import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.web.AjaxTResult;
import cn.com.mfish.oauth.common.Utils;
import cn.com.mfish.oauth.model.UserInfo;
import cn.com.mfish.oauth.remote.RemoteUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ：qiufeng
 * @description：摸鱼测试中心
 * @date ：2021/12/3 17:12
 */
@RestController
@Api(tags ="测试接口")
public class TestController {
    @Resource
    RemoteUserService remoteUserService;

    @GetMapping("/user")
    public AjaxTResult<UserInfo> getUserInfo(HttpServletRequest request) {
        String token = Utils.getAccessToken(request);
        return remoteUserService.getUserInfo(CredentialConstants.INNER, token);
    }

    @GetMapping("/curUser")
    public AjaxTResult<UserInfo> getCurUserInfo() {
        return remoteUserService.getUserInfo(CredentialConstants.INNER);
    }
}
