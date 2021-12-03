package cn.com.mfish.test.controller;

import cn.com.mfish.oauth.common.Utils;
import cn.com.mfish.oauth.model.UserInfo;
import cn.com.mfish.oauth.remote.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/test")
public class TestController {
    @Resource
    RemoteUserService remoteUserService;

    @GetMapping("/user")
    public UserInfo getUserInfo(HttpServletRequest request) {
        String token = Utils.getAccessToken(request);
        UserInfo userInfo = remoteUserService.getUserInfo(token, "inner");
        return userInfo;
    }

}
