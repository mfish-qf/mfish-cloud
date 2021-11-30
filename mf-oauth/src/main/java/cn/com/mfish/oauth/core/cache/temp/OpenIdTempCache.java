package cn.com.mfish.oauth.core.cache.temp;

import cn.com.mfish.oauth.core.cache.redis.RedisPrefix;
import cn.com.mfish.oauth.core.dao.SSOUserDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiufeng
 * @date 2020/2/29 15:07
 */
@Component
public class OpenIdTempCache extends BaseTempCache<String> {
    @Resource
    SSOUserDao ssoUserDao;

    @Override
    protected String buildKey(String key) {
        return RedisPrefix.buildOpenId2userIdKey(key);
    }

    @Override
    protected String getFromDB(String key) {
        return ssoUserDao.getUserIdByOpenId(key);
    }
}
