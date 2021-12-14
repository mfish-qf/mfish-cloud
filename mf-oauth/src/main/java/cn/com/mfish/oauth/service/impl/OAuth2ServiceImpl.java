package cn.com.mfish.oauth.service.impl;

import cn.com.mfish.oauth.service.TokenService;
import cn.com.mfish.oauth.common.RedisPrefix;
import cn.com.mfish.common.core.exception.OAuthValidateException;
import cn.com.mfish.oauth.model.AuthorizationCode;
import cn.com.mfish.oauth.model.RedisAccessToken;
import cn.com.mfish.oauth.model.SSOUser;
import cn.com.mfish.oauth.model.UserInfo;
import cn.com.mfish.oauth.service.OAuth2Service;
import cn.com.mfish.oauth.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @author qiufeng
 * @date 2020/2/15 16:07
 */
@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    UserService userService;
    @Resource
    TokenService webTokenService;

    @Value("${oauth2.expire.code}")
    private long codeExpire = 180;
    @Value("${oauth2.expire.token}")
    private long tokenExpire = 21600;
    @Value("${oauth2.expire.refreshToken}")
    private long reTokenExpire = 604800;

    @Override
    public AuthorizationCode buildCode(OAuthAuthzRequest request) throws OAuthSystemException {
        AuthorizationCode code = setProperty(request);
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        // 生成授权码
        String authorizationCode = oauthIssuerImpl.authorizationCode();
        code.setCode(authorizationCode);
        code.setState(request.getState());
        setCode(code);
        return code;
    }

    /**
     * 构建设置code,token公共属性
     *
     * @param request
     * @return
     */
    private AuthorizationCode setProperty(OAuthRequest request) {
        AuthorizationCode code = new AuthorizationCode();
        code.setClientId(request.getClientId());
        Subject subject = SecurityUtils.getSubject();
        code.setUserId((String) subject.getPrincipal());
        code.setCodeSessionId(subject.getSession().getId().toString());
        code.setScope(org.apache.shiro.util.StringUtils.join(request.getScopes().iterator(), ","));
        code.setRedirectUri(request.getRedirectURI());
        code.setParentToken(request.getParam(OAuth.OAUTH_ACCESS_TOKEN));
        return code;
    }

    @Override
    public void setCode(AuthorizationCode code) {
        redisTemplate.opsForValue().set(RedisPrefix.buildAuthCodeKey(code.getCode()), code, codeExpire, TimeUnit.SECONDS);
    }

    @Override
    public void delCode(String code) {
        redisTemplate.delete(RedisPrefix.buildAuthCodeKey(code));
    }

    @Override
    public AuthorizationCode getCode(String code) {
        return (AuthorizationCode) redisTemplate.opsForValue().get(RedisPrefix.buildAuthCodeKey(code));
    }

    /**
     * password方式登录 直接构造token
     *
     * @param oAuthTokenRequest
     * @return
     * @throws OAuthSystemException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public RedisAccessToken buildToken(OAuthTokenRequest oAuthTokenRequest) throws OAuthSystemException, InvocationTargetException, IllegalAccessException {
        AuthorizationCode code = setProperty(oAuthTokenRequest);
        return code2Token(oAuthTokenRequest, code);
    }

    @Override
    public RedisAccessToken code2Token(OAuthTokenRequest request, AuthorizationCode code) throws OAuthSystemException, InvocationTargetException, IllegalAccessException {
        RedisAccessToken accessToken = new RedisAccessToken();
        BeanUtils.copyProperties(accessToken, code);
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        accessToken.setAccessToken(oauthIssuerImpl.accessToken());
        accessToken.setRefreshToken(oauthIssuerImpl.refreshToken());
        accessToken.setTokenSessionId(SecurityUtils.getSubject().getSession().getId().toString());
        accessToken.setGrantType(request.getGrantType());
        accessToken.setClientSecret(request.getClientSecret());
        accessToken.setExpire(tokenExpire);
        accessToken.setReTokenExpire(reTokenExpire);
        webTokenService.setToken(accessToken);
        webTokenService.setRefreshToken(accessToken);
        delCode(code.getCode());
        return accessToken;
    }

    @Override
    public RedisAccessToken refresh2Token(RedisAccessToken token) throws OAuthSystemException {
        webTokenService.delToken(token.getAccessToken());
        token.setAccessToken(new OAuthIssuerImpl(new MD5Generator()).accessToken());
        webTokenService.setToken(token);
        webTokenService.updateRefreshToken(token);
        return token;
    }

    @Override
    public UserInfo getUserInfo(String userId) throws InvocationTargetException, IllegalAccessException {
        SSOUser user = userService.getUserById(userId);
        if (user == null) {
            throw new OAuthValidateException("错误:未获取到用户信息！");
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfo, user);
        return userInfo;
    }
}
