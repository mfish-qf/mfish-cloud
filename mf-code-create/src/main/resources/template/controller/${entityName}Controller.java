package ${bussiPackage}.${entityPackage}.controller;

import java.util.Arrays;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import ${bussiPackage}.common.annotation.Log;
import ${bussiPackage}.common.core.domain.AjaxTResult;
import ${bussiPackage}.common.enums.BusinessType;

import ${bussiPackage}.${entityPackage}.entity.${entityName};
import ${bussiPackage}.${entityPackage}.req.Req${entityName};
import ${bussiPackage}.${entityPackage}.service.I${entityName}Service;

/**
 * @Description: ${tableVo.ftlDescription}
 * @Author: mfish
 * @Date: ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "${tableVo.ftlDescription}")
@RestController
@RequestMapping("/${entityName?uncap_first}")
public class ${entityName}Controller {
	@Resource
	private I${entityName}Service ${entityName?uncap_first}Service;

	/**
	 * 分页列表查询
	 *
	 * @param req${entityName}
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value = "${tableVo.ftlDescription}-分页列表查询", notes = "${tableVo.ftlDescription}-分页列表查询")
	@GetMapping
	public AjaxTResult<IPage<${entityName}>> queryPageList(Req${entityName} req${entityName},
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		IPage<${entityName}> pageList = ${entityName?uncap_first}Service.page(new Page<>(pageNo, pageSize));
		return new AjaxTResult().success(pageList);
	}

	/**
	 * 添加
	 *
	 * @param ${entityName?uncap_first}
	 * @return
	 */
	@Log(title = "${tableVo.ftlDescription}-添加", businessType = BusinessType.INSERT)
	@ApiOperation(value = "${tableVo.ftlDescription}-添加", notes = "${tableVo.ftlDescription}-添加")
	@PostMapping
	public AjaxTResult<${entityName}> add(@RequestBody ${entityName} ${entityName?uncap_first}) {
		if (${entityName?uncap_first}Service.save(${entityName?uncap_first})){
		   return new AjaxTResult().success("添加成功！", ${entityName?uncap_first});
		}
		return new AjaxTResult().error("添加失败!");
	}

	/**
	 * 编辑
	 *
	 * @param ${entityName?uncap_first}
	 * @return
	 */
	@Log(title = "${tableVo.ftlDescription}-编辑", businessType = BusinessType.UPDATE)
	@ApiOperation(value = "${tableVo.ftlDescription}-编辑", notes = "${tableVo.ftlDescription}-编辑")
	@PutMapping
	public AjaxTResult<?> edit(@RequestBody ${entityName} ${entityName?uncap_first}) {
		if (${entityName?uncap_first}Service.updateById(${entityName?uncap_first})){
		    return new AjaxTResult().success("编辑成功!");
		}
		return new AjaxTResult().error("编辑失败!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@Log(title = "${tableVo.ftlDescription}-通过id删除", businessType = BusinessType.DELETE)
	@ApiOperation(value = "${tableVo.ftlDescription}-通过id删除", notes = "${tableVo.ftlDescription}-通过id删除")
	@DeleteMapping("/{id}")
	public AjaxTResult<?> delete(@ApiParam(name = "id", value = "唯一性ID") @PathVariable String id) {
		if (${entityName?uncap_first}Service.removeById(id)){
		    return new AjaxTResult().success("删除成功!");
		}
		return new AjaxTResult().error("删除失败!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@Log(title = "${tableVo.ftlDescription}-批量删除", businessType = BusinessType.DELETE)
	@ApiOperation(value = "${tableVo.ftlDescription}-批量删除", notes = "${tableVo.ftlDescription}-批量删除")
	@DeleteMapping("/batch")
	public AjaxTResult<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		if (this.${entityName?uncap_first}Service.removeByIds(Arrays.asList(ids.split(",")))){
		    return new AjaxTResult().success("批量删除成功！");
		}
		return new AjaxTResult().error("批量删除失败!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "${tableVo.ftlDescription}-通过id查询", notes = "${tableVo.ftlDescription}-通过id查询")
	@GetMapping("/{id}")
	public AjaxTResult<${entityName}> queryById(@ApiParam(name = "id", value = "唯一性ID") @PathVariable String id) {
		${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}Service.getById(id);
		return new AjaxTResult().success("查询成功",${entityName?uncap_first});
	}
}