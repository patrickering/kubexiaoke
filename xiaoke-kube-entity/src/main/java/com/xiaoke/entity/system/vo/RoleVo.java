

package com.xiaoke.entity.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiaoke
 * @date 2020/2/10
 */
@Data
@ApiModel(value = "前端角色展示对象")
public class RoleVo {
	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 菜单列表
	 */
	private String menuIds;
}
