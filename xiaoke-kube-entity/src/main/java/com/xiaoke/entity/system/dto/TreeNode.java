

package com.xiaoke.entity.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoke
 * @date 2017年11月9日23:33:45
 */
@Data
@ApiModel(value = "树形节点")
public class TreeNode {
	@ApiModelProperty(value = "当前节点id")
	protected Integer id;
	@ApiModelProperty(value = "父节点id")
	protected Integer parentId;
	@ApiModelProperty(value = "子节点列表")
	protected List<TreeNode> children = new ArrayList<TreeNode>();
	@ApiModelProperty(value = "层级")
	protected Integer level;
	public void add(TreeNode node) {
		children.add(node);
	}
}
