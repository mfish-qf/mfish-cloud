package cn.com.mfish.oauth.core.cache.temp;

import cn.com.mfish.oauth.core.dao.SSOUserDao;
import cn.com.mfish.oauth.core.model.SSOUser;
import cn.com.mfish.oauth.core.cache.redis.RedisPrefix;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiufeng
 * @date 2020/2/14 17:46
 */
@Component
public class UserTempCache extends BaseTempCache<SSOUser> {
    @Resource
    SSOUserDao ssoUserDao;

    @Override
    protected String buildKey(String key) {
        return RedisPrefix.buildUserDetailKey(key);
    }

    @Override
    protected SSOUser getFromDB(String key) {
        return ssoUserDao.getUserByAccount(key);
    }

}
