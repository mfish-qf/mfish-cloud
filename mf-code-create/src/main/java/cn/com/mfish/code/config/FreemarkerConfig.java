package cn.com.mfish.code.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author ：qiufeng
 * @description：freemark配置类
 * @date ：2022/8/24 15:22
 */
@Configuration
public class FreemarkerConfig {
    private static final String templatePath = "template/controller/";
    private static final Version version = freemarker.template.Configuration.VERSION_2_3_31;

    @Bean(name = "fmConfig")
    public freemarker.template.Configuration initConfig() throws IOException {
        freemarker.template.Configuration config = new freemarker.template.Configuration(version);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        config.setTemplateLoader(stringTemplateLoader);
        return config;
    }
}
