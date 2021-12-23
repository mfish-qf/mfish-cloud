package cn.com.mfish.gateway.filter;

import cn.com.mfish.common.core.utils.ServletUtils;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.gateway.config.properties.CaptchaProperties;
import cn.com.mfish.gateway.service.CheckCodeService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qiufeng
 * @date 2021/8/12 9:59
 */
@Component
public class CheckCodeFilter extends AbstractGatewayFilterFactory<Object> {
    private static final String CAPTCHA_VALUE = "captchaValue";
    private static final String CAPTCHA_KEY = "captchaKey";

    @Resource
    CheckCodeService checkCodeService;
    @Resource
    CaptchaProperties captchaProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
//            getFormData(exchange);
            Map<String, String> queryParams = exchange.getRequest().getQueryParams().toSingleValueMap();
            System.out.println(queryParams);

            // 不需要校验验证码的地址、验证码关闭、get请求，不处理校验
            if (!StringUtils.containsAnyIgnoreCase(request.getURI().getPath(), captchaProperties.getCheckUrls())
                    || !captchaProperties.getEnabled() || request.getMethod() == HttpMethod.GET) {
                return chain.filter(exchange);
            }
            String key =  ServletUtils.getParameter(CAPTCHA_KEY);
            try {
                String rspStr = resolveBodyFromRequest(request);
                JSONObject obj = JSONObject.parseObject(rspStr);
                checkCodeService.checkCaptcha(obj.getString(CAPTCHA_VALUE), obj.getString(CAPTCHA_KEY));
            } catch (Exception e) {
                return ServletUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }
            return chain.filter(exchange);
        };
    }

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }


}
