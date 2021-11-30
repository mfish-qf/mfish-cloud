package cn.com.mfish.oauth.core.cache.temp;

import cn.com.mfish.oauth.core.cache.redis.RedisPrefix;
import cn.com.mfish.oauth.core.dao.ClientDao;
import cn.com.mfish.oauth.core.model.OAuthClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiufeng
 * @date 2020/2/16 15:54
 */
@Component
public class ClientTempCache extends BaseTempCache<OAuthClient> {
    @Resource
    ClientDao clientDao;

    @Override
    protected String buildKey(String key) {
        return RedisPrefix.buildClientKey(key);
    }

    @Override
    protected OAuthClient getFromDB(String key) {
        return clientDao.getClientById(key);
    }
}
