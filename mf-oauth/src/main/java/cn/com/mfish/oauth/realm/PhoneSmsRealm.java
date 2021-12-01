package cn.com.mfish.oauth.realm;

import cn.com.mfish.oauth.common.SerConstant;
import cn.com.mfish.oauth.model.SSOUser;
import cn.com.mfish.oauth.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;

import javax.annotation.Resource;


/**
 * @author qiufeng
 * @date 2020/2/11 9:41
 */
public class PhoneSmsRealm extends BaseRealm {
    @Resource
    LoginService loginService;

    @Override
    protected AuthenticationInfo buildAuthenticationInfo(SSOUser user, AuthenticationToken authenticationToken, boolean newUser) {
        String code = loginService.getSmsCode(user.getPhone());
        if (StringUtils.isEmpty(code)) {
            throw new IncorrectCredentialsException(SerConstant.INVALID_USER_SECRET_DESCRIPTION);
        }
        AuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(
                user.getId(), code, getName());
        return authorizationInfo;
    }
}
