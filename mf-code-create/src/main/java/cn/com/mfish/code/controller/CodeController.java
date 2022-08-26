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

    @ApiOperation("代码生成")
    @GetMapping("generate")
    public String createEntity() {
        return "";
    }
}
