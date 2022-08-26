package cn.com.mfish.code.test;

import cn.com.mfish.code.common.FreemarkerUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author ：qiufeng
 * @description：代码生成测试
 * @date ：2022/8/24 16:02
 */
@Slf4j
@SpringBootTest
@ComponentScan(basePackages = "cn.com.mfish.code")
public class CodeTest {
    @Resource
    FreemarkerUtils freemarkerUtils;

    @Test
    public void testLoadTemplate() {
        HashMap<String,Object> param = new HashMap();
        param.put("bussiPackage", "cn.com.mfish");
        param.put("entityPackage", "");
        param.put("entityName", "ha_ho");
        param.put("tableName", "test_table");
        String aaa = freemarkerUtils.buildCode(freemarkerUtils.getTemplate(),param);
        System.out.println(aaa);
    }
}
