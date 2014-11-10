package com.ganith.tree.repository;

import java.util.List;

import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.entity.TreeNode.NodeType;

/**
 * Custom repository class used to retrieve {@link TreeNode} by parent id and type
 * @author Pavithra
 *
 */
public interface TreeNodeRepositoryCustom {
	
	public void removeChildrenByParentId(String parentId);
	
	public List<TreeNode> findByNodeTypeOrderedBySeq(NodeType nodeType);

}
