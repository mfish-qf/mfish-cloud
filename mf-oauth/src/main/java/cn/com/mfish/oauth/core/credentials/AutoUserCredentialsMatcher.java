package cn.com.mfish.oauth.core.credentials;

import cn.com.mfish.oauth.core.common.CheckWithResult;
import cn.com.mfish.oauth.core.common.SerConstant;
import cn.com.mfish.oauth.core.exception.OAuthValidateException;
import cn.com.mfish.oauth.core.model.SSOUser;
import cn.com.mfish.oauth.core.service.UserService;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import javax.annotation.Resource;

/**
 * @author qiufeng
 * @date 2021/10/26 17:57
 */
public class AutoUserCredentialsMatcher extends SimpleCredentialsMatcher {
    @Resource
    UserService userService;

    protected void insertNewUser(boolean newUser, SSOUser user) {
        if (newUser) {
            CheckWithResult<SSOUser> result = userService.insert(user);
            if (!result.isSuccess()) {
                throw new OAuthValidateException(SerConstant.INVALID_NEW_USER_DESCRIPTION);
            }
        }
    }
}
