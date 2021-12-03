package cn.com.mfish.oauth.validator;

import cn.com.mfish.oauth.common.CheckWithResult;
import cn.com.mfish.oauth.model.RedisAccessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiufeng
 * @date 2020/2/18 19:02
 */
@Component
public class Refresh2TokenValidator extends MultipleValidator {
    List<Class<? extends IBaseValidator<RedisAccessToken>>> validateTokenList = new ArrayList<>();

    public Refresh2TokenValidator() {
        this.validateClientList.add(ClientIdExistValidator.class);
        this.validateClientList.add(ClientSecretExistValidator.class);
        this.validateClientList.add(GrantTypeExistValidator.class);
        this.validateClientList.add(RedirectUriExistValidator.class);
        this.validateTokenList.add(UriEqualValidator.class);
        this.validateTokenList.add(ClientIdEqualValidator.class);
        this.validateTokenList.add(ClientSecretEqualValidator.class);
    }

    /**
     * token 参数相关的组合校验
     *
     * @param request
     * @param result
     * @return
     */
    public CheckWithResult<RedisAccessToken> validateToken(HttpServletRequest request, CheckWithResult<RedisAccessToken> result) {
        return validate(request, result, validateTokenList);
    }

    /**
     * 校验refreshToken与之前获取token时传入的clientId是否一致
     */
    @Component
    public class ClientIdEqualValidator extends AbstractRefreshTokenValidator {
        @Override
        public CheckWithResult<RedisAccessToken> validate(HttpServletRequest request, CheckWithResult<RedisAccessToken> result) {
            CheckWithResult<RedisAccessToken> result1 = getRefreshToken(request, result);
            if (!result1.isSuccess()) {
                return result1;
            }
            String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
            if (!StringUtils.isEmpty(clientId) && clientId.equals(result1.getResult().getClientId())) {
                return result1;
            }
            return result1.setSuccess(false).setMsg("错误:token和refreshToken两次传入的clientId不一致");
        }
    }

    /**
     * 校验refreshToken与之前获取token时传入的clientSecret是否一致
     */
    @Component
    public class ClientSecretEqualValidator extends AbstractRefreshTokenValidator {
        @Override
        public CheckWithResult<RedisAccessToken> validate(HttpServletRequest request, CheckWithResult<RedisAccessToken> result) {
            CheckWithResult<RedisAccessToken> result1 = getRefreshToken(request, result);
            if (!result1.isSuccess()) {
                return result1;
            }
            String secret = request.getParameter(OAuth.OAUTH_CLIENT_SECRET);
            if (!StringUtils.isEmpty(secret) && secret.equals(result1.getResult().getClientSecret())) {
                return result1;
            }
            return result1.setSuccess(false).setMsg("错误:token和refreshToken两次传入的clientSecret不一致");
        }
    }

    /**
     * 校验refreshToken与之前获取token时传入的uri是否一致
     */
    @Component
    public class UriEqualValidator extends AbstractRefreshTokenValidator {
        @Override
        public CheckWithResult<RedisAccessToken> validate(HttpServletRequest request, CheckWithResult<RedisAccessToken> result) {
            CheckWithResult<RedisAccessToken> result1 = getRefreshToken(request, result);
            if (!result1.isSuccess()) {
                return result1;
            }
            String uri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
            if (!StringUtils.isEmpty(uri) && uri.equals(result1.getResult().getRedirectUri())) {
                return result1;
            }
            return result1.setSuccess(false).setMsg("错误:token和refreshToken两次传入的uri不一致");
        }
    }
}
