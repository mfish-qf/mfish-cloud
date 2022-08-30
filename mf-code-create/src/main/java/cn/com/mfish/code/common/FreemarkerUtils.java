package cn.com.mfish.code.common;

import cn.com.mfish.code.config.FreemarkerProperties;
import cn.com.mfish.code.entity.CodeInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：qiufeng
 * @description：代码生成工具类
 * @date ：2022/8/24 15:30
 */
@Component
public class FreemarkerUtils {
    @Resource
    Configuration fmConfig;
    @Resource
    FreemarkerProperties freemarkerProperties;

    public Map<String, String> buildAllCode(CodeInfo codeInfo) {
        Map<String, String> map = new HashMap<>();
        for (String key : freemarkerProperties.getKeys()) {
            map.put(key, buildCode(key, codeInfo));
        }
        return map;
    }

    public String buildCode(String template, CodeInfo codeInfo) {
        StringWriter stringWriter = new StringWriter();
        try (Writer out = new BufferedWriter(stringWriter)) {
            Template temp = fmConfig.getTemplate(template, "utf-8");
            temp.process(codeInfo, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
