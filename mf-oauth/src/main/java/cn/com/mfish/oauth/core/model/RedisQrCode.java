package cn.com.mfish.oauth.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qiufeng
 * @date 2020/3/5 17:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RedisQrCode extends QRCode {
    private String accessToken;
}
