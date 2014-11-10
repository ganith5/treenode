package com.ganith.tree.repository;

import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.entity.TreeNode.NodeType;

/**
 * Implementation class for custom TreeNode repository interface
 * @author Pavithra
 *
 */
@Slf4j
public class TreeNodeRepositoryImpl implements TreeNodeRepositoryCustom {

	@Inject
	private MongoTemplate mongoTemplate;

	public void removeChildrenByParentId(String parentId) {
		log.debug("Removing nodes with parent id {} ", parentId);
		Query query = new Query();
		query.addCriteria(Criteria.where("parent").is(parentId));
		mongoTemplate.remove(query, TreeNode.class);
	}

	public List<TreeNode> findByNodeTypeOrderedBySeq(NodeType nodeType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(nodeType));
		query.with(new Sort(Sort.Direction.ASC, "sequence"));
		List<TreeNode> treeNodeList = mongoTemplate.find(query, TreeNode.class);
		return treeNodeList;
	}

}
