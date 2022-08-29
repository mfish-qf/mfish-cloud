package cn.com.mfish.code.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：qiufeng
 * @description：freemark配置类
 * @date ：2022/8/24 15:22
 */
@Configuration
public class FreemarkerConfig {
    private static final String templatePath = "classpath:template";
    private static final Version version = freemarker.template.Configuration.VERSION_2_3_31;

    public String getTemplate(File file) {
        StringBuffer sb = new StringBuffer();
        try (FileInputStream stream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));// 保持原有换行格式
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取模版目录下所有模版文件
     *
     * @return
     * @throws FileNotFoundException
     */
    private List<File> getFiles() throws FileNotFoundException {
        List<File> list = new ArrayList<>();
        getFiles(ResourceUtils.getFile(templatePath), list);
        return list;
    }

    /**
     * 递归获取目录下所有文件
     *
     * @param file
     * @param list
     */
    private void getFiles(File file, List<File> list) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            list.add(file);
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                getFiles(f, list);
            }
        }
    }

    @Lazy
    @Bean(name = "fmConfig")
    public freemarker.template.Configuration initConfig() throws IOException {
        freemarker.template.Configuration config = new freemarker.template.Configuration(version);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        List<File> list = getFiles();
        for (File file : list) {
            String path = file.getPath();
            String parent = file.getParent();
            String pName = parent.substring(parent.replace("\\", "/").lastIndexOf("/")+1);
            stringTemplateLoader.putTemplate(pName, getTemplate(file));
        }
        config.setTemplateLoader(stringTemplateLoader);
        return config;
    }
}
