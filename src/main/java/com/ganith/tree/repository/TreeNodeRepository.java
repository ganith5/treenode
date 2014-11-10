package com.ganith.tree.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.entity.TreeNode.NodeType;

/**
 * Repository class for used to store and retrieve {@link TreeNode} documents
 * @author Pavithra
 *
 */
public interface TreeNodeRepository extends MongoRepository<TreeNode, String>, TreeNodeRepositoryCustom{
	
	public List<TreeNode> findByParent(String parent);
	
	public List<TreeNode> findByType(NodeType nodeType);
	
}
