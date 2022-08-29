package ${packageName}.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import ${packageName}.req.Req${entityName};

/**
 * @Description: ${tableInfo.tableDesc}
 * @Author: mfish
 * @Date: ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
@Data
@TableName("${tableInfo.tableName}")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "${tableInfo.tableName}对象", description = "${tableInfo.tableDesc}")
public class ${entityName} extends Req${entityName} {

    <#list columns as po>
	/**
	 * ${po.filedComment}
	 */
	<#if po.fieldName == primaryKeyField>
	@TableId(type = IdType.AUTO)
	<#else>
    <#if po.fieldType =='java.util.Date'>
    <#if po.fieldDbType =='date'>
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    <#elseif po.fieldDbType =='datetime'>
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    </#if>
    </#if>
    @ApiModelProperty(value = "${po.filedComment}")
	private <#if po.fieldType=='java.sql.Blob'>byte[]<#else>${po.fieldType}</#if> ${po.fieldName};
	</#list>
}