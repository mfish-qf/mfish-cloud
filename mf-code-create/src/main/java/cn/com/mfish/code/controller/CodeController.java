package cn.com.mfish.code.controller;

import cn.com.mfish.code.common.FreemarkerUtils;
import cn.com.mfish.code.entity.CodeInfo;
import cn.com.mfish.common.core.web.AjaxTResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：qiufeng
 * @description：代码生成控制器
 * @date ：2022/8/18 16:45
 */
@Api(tags = "代码生成")
@RestController("code")
public class CodeController {
    @Resource
    FreemarkerUtils freemarkerUtils;

    @ApiOperation("代码生成")
    @GetMapping
    public AjaxTResult<Map<String, String>> getCode(CodeInfo codeInfo) {
        return AjaxTResult.ok(freemarkerUtils.buildAllCode(codeInfo), "生成代码成功");
    }
}
