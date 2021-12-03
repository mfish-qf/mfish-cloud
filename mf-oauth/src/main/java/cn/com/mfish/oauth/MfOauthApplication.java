package cn.com.mfish.oauth;

import cn.com.mfish.common.datasource.annotation.ScanMapper;
import cn.com.mfish.common.swagger.annotation.AutoSwagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：qiufeng
 * @description：统一认证中心
 * @date ：2021/11/15 15:05
 */
@SpringBootApplication
@AutoSwagger
@ScanMapper
@Slf4j
public class MfOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MfOauthApplication.class, args);
        log.info("\n\t----------------------------------------------------------\n\t" +
                "\n\t--------------------摸鱼认证中心启动成功-----------------------\n\t");
    }
}
