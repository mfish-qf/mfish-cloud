package cn.com.mfish.oauth.core.validator;

import cn.com.mfish.oauth.core.common.CheckWithResult;
import cn.com.mfish.oauth.core.model.OAuthClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiufeng
 * @date 2020/2/13 13:58
 */
@Component
public class ClientIdExistValidator extends AbstractClientValidator {

    @Override
    public CheckWithResult<OAuthClient> validate(HttpServletRequest request, CheckWithResult<OAuthClient> result) {
        return getOAuthClient(request, result);
    }
}
