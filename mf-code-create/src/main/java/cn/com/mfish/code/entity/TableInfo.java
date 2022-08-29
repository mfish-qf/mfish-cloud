package cn.com.mfish.code.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：qiufeng
 * @description：表信息
 * @date ：2022/8/29 16:46
 */
@Data
@ApiModel("表信息")
public class TableInfo {
    @ApiModelProperty("表名称")
    private String tableName;
    @ApiModelProperty("表描述信息")
    private String tableDesc;
}
