package cn.com.mfish.code.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：qiufeng
 * @description：代码生成控制器
 * @date ：2022/8/18 16:45
 */
@Api(tags = "代码生成")
@RestController
public class CodeController {

    /**
     * 生成代码接口
     * @para tableName 表名
     * @para saveUrl 生成文件路径
     * @para basePackageUrl 生成上级包名
     */
    @ApiOperation("代码生成")
    @GetMapping("generate")
    public String createEntity(String tableName,String saveUrl,String basePackageUrl) {

        return "";
    }
}
