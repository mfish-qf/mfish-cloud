package cn.com.mfish.code.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author ：qiufeng
 * @description：freemark配置类
 * @date ：2022/8/24 15:22
 */
@Configuration
public class FreemarkerConfig {
    private static final String templatePath = "/template/";
    private static final Version version = freemarker.template.Configuration.VERSION_2_3_31;

    public String getTemplate() {
        InputStream inputStream = Resources.class.getResourceAsStream(templatePath+"controller/${entityName}Controller.java.ftl");
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));// 保持原有换行格式
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Lazy
    @Bean(name = "fmConfig")
    public freemarker.template.Configuration initConfig() throws IOException {
        freemarker.template.Configuration config = new freemarker.template.Configuration(version);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("controller",getTemplate());
        config.setTemplateLoader(stringTemplateLoader);
        return config;
    }
}
