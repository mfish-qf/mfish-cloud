package cn.com.mfish.gateway.service;

import cn.com.mfish.common.core.exception.CaptchaException;
import cn.com.mfish.common.core.web.AjaxResult;

import java.io.IOException;

/**
 * @author qiufeng
 * @date 2021/8/12 11:06
 */
public interface CheckCodeService {
    /**
     * 生成验证码
     */
    AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    void checkCaptcha(String key, String value) throws CaptchaException;
}
