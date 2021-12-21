package cn.com.mfish.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiufeng
 * @description：验证码属性
 * @date 2021/8/12 15:40
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.captcha")
@Data
public class CaptchaProperties {
    /**
     * 验证码开关
     */
    private Boolean enabled = true;

    /**
     * 验证码类型（math 数组计算 char 字符）
     */
    private String type;
}
