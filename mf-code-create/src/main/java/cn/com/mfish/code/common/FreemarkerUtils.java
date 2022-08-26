package cn.com.mfish.code.common;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.io.*;
import java.util.Map;

/**
 * @author ：qiufeng
 * @description：代码生成工具类
 * @date ：2022/8/24 15:30
 */
@Component
public class FreemarkerUtils {
    @Resource
    freemarker.template.Configuration fmConfig;

    public String getTemplate() {
        InputStream inputStream = Resources.class.getResourceAsStream("/template/controller/${entityName}Controller.java");
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

    public String buildCode(String template, Map<String, Object> rootMap) {
        StringWriter stringWriter = new StringWriter();
        try (Writer out = new BufferedWriter(stringWriter)) {
            Template temp = fmConfig.getTemplate(template, "utf-8");
            temp.process(rootMap, out);
            out.flush();
            System.out.println("---" + stringWriter + "--- is already ok");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
