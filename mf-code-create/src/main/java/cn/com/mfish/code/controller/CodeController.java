package cn.com.mfish.code.controller;

import cn.com.mfish.code.common.FreeMarkerTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：qiufeng
 * @description：代码生成控制器
 * @date ：2022/8/18 16:45
 */
@Api(tags = "代码生成")
@RestController
public class CodeController {
    @Resource
    FreeMarkerTools freeMarkerTools;

    /**
     * 生成代码接口
     * @para tableName 表名
     * @para saveUrl 生成文件路径
     * @para basePackageUrl 生成上级包名
     */
    @ApiOperation("代码生成")
    @GetMapping("generate")
    public String createEntity(String tableName,String saveUrl,String basePackageUrl) throws Exception {

        //生成文件路径
        saveUrl = saveUrl == null ? "/Users/zjl/Desktop/xxfilesxx": saveUrl;
        //生成文件包名，根据实际情况修改即可
        basePackageUrl = basePackageUrl == null? "com.example.demo": basePackageUrl;
        //bean类名
        String entityName = freeMarkerTools.getEntityName(tableName);

        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map
        //封装参数
        Map<String, Object> root = new HashMap<>();
        // 添加包名
        root.put("basePackageUrl", basePackageUrl);
        //表参数
        root.put("tableName", tableName);
        // 实体类名
        root.put("entityName", entityName);
        // 实体类首字母小写
        root.put("entityNameLower", freeMarkerTools.getEntityNameLower(tableName));
        // 数据库信息
        root.put("columns", freeMarkerTools.getDataInfo(tableName));

        // 生成实体类
        freeMarkerTools.generate(root,"entity.ftl",saveUrl,entityName+".java");
        // 生成dao
        freeMarkerTools.generate(root,"dao.ftl",saveUrl,entityName+"Dao.java");
        // 生成mapper
        freeMarkerTools.generate(root,"mapper.ftl",saveUrl,entityName+"Mapper.xml");
        // 生成controller
        freeMarkerTools.generate(root,"controller.ftl",saveUrl,entityName+"Controller.java");
        //生成service
        freeMarkerTools.generate(root,"service.ftl",saveUrl,entityName+"Service.java");
        //生成serviceImpl
        freeMarkerTools.generate(root,"serviceImpl.ftl",saveUrl,entityName+"ServiceImpl.java");

        return "生成成功！！！！";
    }
}
