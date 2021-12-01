package cn.com.mfish.oauth.fallback;

import cn.com.mfish.oauth.remote.RemoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author ：qiufeng
 * @description：远程用户服务处理失败降级处理
 * @date ：2021/12/1 17:17
 */
@Component
@Slf4j
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable cause) {
        log.error("token服务调用失败:" + cause.getMessage(), cause);
        return null;
    }
}
