package cn.com.mfish.oauth.fallback;

import cn.com.mfish.oauth.remote.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author ：qiufeng
 * @description：TODO
 * @date ：2021/12/4 0:27
 */
@Component
@Slf4j
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable cause) {
        log.error("token服务调用失败:" + cause.getMessage(), cause);
        throw new RuntimeException(cause);
    }
}
