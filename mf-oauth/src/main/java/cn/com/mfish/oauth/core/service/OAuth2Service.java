package cn.com.mfish.oauth.core.service;

import cn.com.mfish.oauth.core.model.AuthorizationCode;
import cn.com.mfish.oauth.core.model.RedisAccessToken;
import cn.com.mfish.oauth.core.model.UserInfo;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author qiufeng
 * @date 2020/2/13 12:51
 */
public interface OAuth2Service extends TokenService<RedisAccessToken>{
    AuthorizationCode buildCode(OAuthAuthzRequest request) throws OAuthSystemException;

    void setCode(AuthorizationCode code);

    void delCode(String code);

    AuthorizationCode getCode(String code);

    RedisAccessToken buildToken(OAuthTokenRequest request) throws OAuthSystemException, InvocationTargetException, IllegalAccessException;

    RedisAccessToken code2Token(OAuthTokenRequest request, AuthorizationCode code) throws OAuthSystemException, InvocationTargetException, IllegalAccessException;

    RedisAccessToken refresh2Token(RedisAccessToken token) throws OAuthSystemException;

    UserInfo getUserInfo(String userId) throws InvocationTargetException, IllegalAccessException;
}
