package cn.com.mfish.code.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：qiufeng
 * @description：代码相关属性
 * @date ：2022/8/25 16:39
 */
@Data
@ApiModel("代码相关属性")
public class CodeProperties {
    @ApiModelProperty("包名")
    private String packageName;
    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("表描述")
    private String tableDesc;
}
