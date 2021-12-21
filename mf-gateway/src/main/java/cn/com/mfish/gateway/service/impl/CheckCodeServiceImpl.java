package cn.com.mfish.gateway.service.impl;

import cn.com.mfish.common.core.constants.Constants;
import cn.com.mfish.common.core.exception.CaptchaException;
import cn.com.mfish.common.core.utils.Base64;
import cn.com.mfish.common.core.utils.StringUtils;
import cn.com.mfish.common.core.web.AjaxTResult;
import cn.com.mfish.common.redis.service.RedisService;
import cn.com.mfish.gateway.config.properties.CaptchaProperties;
import cn.com.mfish.gateway.service.CheckCodeService;
import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author qiufeng
 * @description：验证码服务
 * @date 2021/8/12 11:41
 */
@Service
public class CheckCodeServiceImpl implements CheckCodeService {
    @Resource(name = "charCaptchaProducer")
    private Producer charCaptchaProducer;
    @Resource(name = "mathCaptchaProducer")
    private Producer mathCaptchaProducer;
    @Resource
    private RedisService redisService;
    @Resource
    private CaptchaProperties captchaProperties;

    @Override
    public AjaxTResult<Map<String, Object>> createCaptcha() {
        AjaxTResult<Map<String, Object>> ajax = AjaxTResult.ok();
        boolean captchaOnOff = captchaProperties.getEnabled();
        ajax.getData().put("captchaOnOff", captchaOnOff);
        if (!captchaOnOff) {
            return ajax;
        }
        String capStr;
        String code = null;
        BufferedImage image = null;
        // 生成验证码
        if ("math".equals(captchaProperties.getType())) {
            String capText = mathCaptchaProducer.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = mathCaptchaProducer.createImage(capStr);
        } else if ("char".equals(captchaProperties.getType())) {
            capStr = code = charCaptchaProducer.createText();
            image = charCaptchaProducer.createImage(capStr);
        } else {
            return AjaxTResult.fail("错误:未设置验证码类型");
        }
        String uuid = UUID.randomUUID().toString();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRE, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxTResult.fail(e.getMessage());
        }
        ajax.getData().put("uuid", uuid);
        ajax.getData().put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    @Override
    public void checkCaptcha(String code, String uuid) {
        if (StringUtils.isEmpty(code)) {
            throw new CaptchaException("错误:验证码不能为空");
        }
        if (StringUtils.isEmpty(uuid)) {
            throw new CaptchaException("错误:验证码已失效");
        }
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException("错误:验证码不正确");
        }
    }
}
