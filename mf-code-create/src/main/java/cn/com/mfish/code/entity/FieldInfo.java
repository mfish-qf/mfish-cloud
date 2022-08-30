package cn.com.mfish.code.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ：qiufeng
 * @description：字段信息
 * @date ：2022/8/29 16:47
 */
@Data
@ApiModel("字段信息")
@Accessors(chain = true)
public class FieldInfo implements Serializable {
    @ApiModelProperty("字段名称")
    private String fieldName;
    @ApiModelProperty("是否主键")
    private Boolean isPrimary = false;
    @ApiModelProperty("字段类型")
    private String type = "String";
    @ApiModelProperty("数据库字段类型")
    private String dbType = "VARCHAR";
    @ApiModelProperty("字段描述")
    private String comment="";
}
