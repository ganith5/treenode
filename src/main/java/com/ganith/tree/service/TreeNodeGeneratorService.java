package com.ganith.tree.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;

import com.ganith.tree.aspects.NotifyClient;
import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.entity.TreeNodeFactory;
import com.ganith.tree.entity.TreeNode.NodeType;
import com.ganith.tree.exception.CustomException;
import com.ganith.tree.repository.TreeNodeRepository;
import com.ganith.tree.util.Constants;

/**
 * Class holding service methods performing CRUD operations of {@link TreeNode}
 * @author Pavithra
 * 
 */
@Named
@Slf4j
public class TreeNodeGeneratorService {

	@Inject
	private TreeNodeRepository treeNodeRepository;

	/**
	 * Method queries all nodes in a tree
	 * 
	 * @return nodeList
	 */
	public List<TreeNode> getAllNodes() {
		List<TreeNode> nodeList = treeNodeRepository.findAll(new Sort(
				Sort.Direction.ASC, "sequence"));
		if (nodeList.size() == 0) {
			nodeList = createRootNode();
		}
		return nodeList;
	}

	/**
	 * Method creates factory node and persists to database. The annotation
	 * {@link NotifyClient} identifies that the creation event should be queued
	 * 
	 * @param parentId
	 * @return factoryNode
	 */
	@NotifyClient
	public TreeNode createFactoryNode(String parentId) {
		log.debug("Creating factory node with parentId {} ", parentId);
		int currentFactoryCount = getMaxSequence();
		AtomicInteger integer = new AtomicInteger(currentFactoryCount);
		int nextFactoryCount = integer.incrementAndGet();
		TreeNode factoryNode = TreeNodeFactory.createfactoryNode(parentId,
				nextFactoryCount);
		factoryNode = treeNodeRepository.save(factoryNode);
		return factoryNode;

	}

	/**
	 * Method generates children nodes for factories based on the user input
	 * count and persists to database. The annotation {@link NotifyClient}
	 * identifies that the creation event should be queued
	 * 
	 * @param parentId
	 * @param numOfChildren
	 * @return nodes
	 */
	@NotifyClient
	public List<TreeNode> generateChildren(String parentId, int numOfChildren) throws Exception{
		log.debug("Create child node = {} , {} ", parentId, numOfChildren);

		TreeNode parentNode = treeNodeRepository.findOne(parentId);
		if(parentNode == null) {
			throw new CustomException("Parent Node not found");
		}

		deleteChildren(parentId);

		List<TreeNode> childNodesList = new ArrayList<TreeNode>();
		for (int i = 0; i < numOfChildren; i++) {
			TreeNode childNode = TreeNodeFactory.createChildNode(parentId,
					parentNode.getMinRange(), parentNode.getMaxRange());
			childNodesList.add(childNode);
		}
		treeNodeRepository.save(childNodesList);
		return childNodesList;
	}

	/**
	 * Method deletes the factory and child nodes from the tree The annotation
	 * {@link NotifyClient} identifies that the delete event should be queued
	 * 
	 * @param nodeId
	 */
	@NotifyClient
	public void deleteFactoryAndChildren(String nodeId) {
		treeNodeRepository.delete(nodeId);
		treeNodeRepository.removeChildrenByParentId(nodeId);
	}

	/**
	 * Method constructs root node and persists to database
	 * 
	 * @return nodes
	 */
	public List<TreeNode> createRootNode() {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		TreeNode rootNode = treeNodeRepository.findOne(Constants.ROOT_ID);
		if (rootNode == null) {
			rootNode = treeNodeRepository
					.save(TreeNodeFactory.createRootNode());
		}
		nodes.add(rootNode);
		return nodes;

	}

	/**
	 * Query child nodes by parent id
	 * 
	 * @param parentId
	 * @return list
	 */
	public List<TreeNode> queryChildrenByParentId(String parentId) {
		return treeNodeRepository.findByParent(parentId);
	}

	public void deleteChildren(String parentId) {
		treeNodeRepository.removeChildrenByParentId(parentId);
	}

	public int getMaxSequence() {
		List<TreeNode> factories = treeNodeRepository
				.findByNodeTypeOrderedBySeq(NodeType.FACTORY);
		TreeNode nodeConfiguration;
		if (factories.size() != 0) {
			nodeConfiguration = factories.get(factories.size() - 1);
			log.debug("Get max sequence {} ", nodeConfiguration.getSequence());
			return nodeConfiguration.getSequence();
		}
		return 0;
	}

}
