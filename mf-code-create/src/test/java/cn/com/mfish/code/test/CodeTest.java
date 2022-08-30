package cn.com.mfish.code.test;

import cn.com.mfish.code.common.FreemarkerUtils;
import cn.com.mfish.code.entity.CodeInfo;
import cn.com.mfish.code.entity.TableInfo;
import cn.com.mfish.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

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
        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setPackageName("cn.com.mfish.code");
        codeInfo.setEntityName(StringUtils.toCamelBigCase("test_code"));
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("test_table");
        tableInfo.setTableDesc("测试");
        codeInfo.setTableInfo(tableInfo);
        String aaa = freemarkerUtils.buildCode("controller", codeInfo);
        System.out.println(aaa);
    }

}
