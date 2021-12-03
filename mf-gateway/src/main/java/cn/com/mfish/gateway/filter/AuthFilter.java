package cn.com.mfish.gateway.filter;

import cn.com.mfish.common.core.constants.Constants;
import cn.com.mfish.common.core.constants.CredentialConstants;
import cn.com.mfish.common.core.constants.HttpStatus;
import cn.com.mfish.common.core.utils.ServletUtils;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.common.redis.service.RedisService;
import cn.com.mfish.gateway.config.properties.IgnoreWhiteProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：qiufeng
 * @description：认证过滤器
 * @date ：2021/11/18 17:59
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Resource
    private IgnoreWhiteProperties ignoreWhite;
    @Resource
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
//        Claims claims = JwtUtils.parseToken(token);
//        if (claims == null) {
//            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
//        }
//        String userkey = JwtUtils.getUserKey(claims);
//        boolean islogin = redisService.hasKey(getTokenKey(userkey));
//        if (!islogin) {
//            return unauthorizedResponse(exchange, "登录状态已过期");
//        }
//        String userid = JwtUtils.getUserId(claims);
//        String username = JwtUtils.getUserName(claims);
//        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
//            return unauthorizedResponse(exchange, "令牌验证失败");
//        }
//        UserInfo userInfo = new UserInfo();
        // 设置用户信息到请求
//        addHeader(mutate, CredentialConstants.DETAILS_USER_ID, userInfo.getId());
//        addHeader(mutate, CredentialConstants.DETAILS_USERNAME, userInfo.getAccount());
        // 内部请求来源参数清除
        removeHeader(mutate, CredentialConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return Constants.ACCESS_TOKEN + ":" + token;
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(Constants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.PREFIX)) {
            token = token.replaceFirst(Constants.PREFIX, StringUtils.EMPTY);
        } else {
            List<String> list = request.getQueryParams().get(Constants.ACCESS_TOKEN);
            if (list != null && list.size() > 0) {
                token = list.get(0);
            }
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
