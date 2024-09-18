package com.xiaoke.utils;


import com.xiaoke.entity.system.dto.MenuTree;
import com.xiaoke.entity.system.dto.TreeNode;
import com.xiaoke.entity.system.entity.Menu;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoke
 * @date 2020-02-09
 */
@UtilityClass
public class TreeUtil {
    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes, 0));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes, Integer level) {
        for (T it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes, level + 1));
            }
        }
        treeNode.setLevel(level);
        return treeNode;
    }

    /**
     * 通过sysMenu创建树形节点
     *
     * @param menus
     * @param root
     * @return
     */
    public List<MenuTree> buildTree(List<Menu> menus, int root) {
        List<MenuTree> trees = new ArrayList<>();
        MenuTree node;
        for (Menu menu : menus) {
            node = new MenuTree();
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setName(menu.getName());
            node.setPath(menu.getPath());
            node.setCode(menu.getPermission());
            node.setLabel(menu.getName());
            node.setIcon(menu.getIcon());
            node.setType(menu.getType());
            node.setSort(menu.getSort());
            node.setHasChildren(true);
            node.setKeepAlive(menu.getKeepAlive());
            trees.add(node);
        }
        return TreeUtil.build(trees, root);
    }
}
