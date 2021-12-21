package cn.com.mfish.oauth.cache.temp;

import cn.com.mfish.common.redis.temp.BaseTempCache;
import cn.com.mfish.oauth.common.RedisPrefix;
import cn.com.mfish.oauth.mapper.SSOUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiufeng
 * @date 2020/2/29 15:07
 */
@Component
public class OpenIdTempCache extends BaseTempCache<String> {
    @Resource
    SSOUserMapper ssoUserMapper;

    @Override
    protected String buildKey(String key) {
        return RedisPrefix.buildOpenId2userIdKey(key);
    }

    @Override
    protected String getFromDB(String key) {
        return ssoUserMapper.getUserIdByOpenId(key);
    }
}
