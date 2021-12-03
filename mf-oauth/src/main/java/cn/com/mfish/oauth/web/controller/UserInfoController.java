package cn.com.mfish.oauth.web.controller;

import cn.com.mfish.oauth.annotation.InnerUser;
import cn.com.mfish.oauth.annotation.LogAnnotation;
import cn.com.mfish.oauth.cache.UserTokenCache;
import cn.com.mfish.oauth.common.CheckWithResult;
import cn.com.mfish.oauth.common.SerConstant;
import cn.com.mfish.oauth.exception.OAuthValidateException;
import cn.com.mfish.oauth.model.RedisAccessToken;
import cn.com.mfish.oauth.model.UserInfo;
import cn.com.mfish.oauth.service.OAuth2Service;
import cn.com.mfish.oauth.validator.AccessTokenValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * @author qiufeng
 * @date 2020/2/17 18:49
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    AccessTokenValidator accessTokenValidator;
    @Resource
    OAuth2Service oAuth2Service;
    @Resource
    UserTokenCache userTokenCache;

    @InnerUser
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = OAuth.HeaderType.AUTHORIZATION, value = "认证token，header和access_token参数两种方式任意一种即可，格式为Bearer+token组合，例如Bearer39a5304bc77c655afbda6b967e5346fa", paramType = "header"),
            @ApiImplicitParam(name = OAuth.OAUTH_ACCESS_TOKEN, value = "token值 header和access_token参数两种方式任意一种即可", paramType = "query")
    })
    @LogAnnotation("getUser")
    public UserInfo getUserInfo(HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
        CheckWithResult<RedisAccessToken> result = accessTokenValidator.validate(request, null);
        if (!result.isSuccess()) {
            throw new OAuthValidateException(result.getMsg());
        }
        return oAuth2Service.getUserInfo(result.getResult().getUserId());
    }

    @ApiOperation("用户登出")
    @GetMapping("/revoke")
    public CheckWithResult<String> revoke() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            String error = "错误:未获取到用户登录状态!";
            return new CheckWithResult<String>().setSuccess(false).setMsg(error).setResult(error);
        }
        String userId = (String) subject.getPrincipal();
        userTokenCache.delUserDevice(SerConstant.DeviceType.Web, userId);
        subject.logout();
        return new CheckWithResult<String>().setMsg("成功登出!").setResult("登出成功!");
    }
}
