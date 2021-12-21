package cn.com.mfish.gateway.handler;

import cn.com.mfish.common.core.web.AjaxTResult;
import cn.com.mfish.gateway.service.impl.CheckCodeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author ：qiufeng
 * @description：获取验证码
 * @date ：2021/12/21 14:58
 */
@Component
public class CheckCodeHandler implements HandlerFunction<ServerResponse> {
    @Resource
    CheckCodeServiceImpl checkCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        try {
            AjaxTResult result = checkCodeService.createCaptcha();
            return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(result));
        } catch (Exception ex) {
            return Mono.error(ex);
        }
    }
}
