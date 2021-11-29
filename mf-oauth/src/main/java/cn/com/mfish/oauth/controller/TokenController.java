package cn.com.mfish.oauth.controller;

import cn.com.mfish.common.core.web.AjaxTResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：qiufeng
 * @description：token控制器
 * @date ：2021/11/15 14:55
 */
@RestController
@RequestMapping("/login")
@Api(tags = "token控制器")
public class TokenController {
    @PostMapping
    public AjaxTResult<?> login() {
        // 获取登录token
        return AjaxTResult.ok();
    }
    @GetMapping
    public AjaxTResult<?> getlogin() {
        // 获取登录token
        return AjaxTResult.ok();
    }
}
