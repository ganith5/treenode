package com.ganith.tree.entity;

import com.ganith.tree.entity.TreeNode.NodeType;
import com.ganith.tree.service.RandomNumberGenerator;
import com.ganith.tree.util.Constants;

/**
 *  Factory class for creation of {@link TreeNode} entities
 *  @author Pavithra
 *
 */
public class TreeNodeFactory {
	
	public static TreeNode createRootNode() {
		TreeNode rootNode = new TreeNode();
		rootNode.setParent(Constants.ROOT_PARENT_ID);
		rootNode.setText(Constants.ROOT_NAME);
		rootNode.setId(Constants.ROOT_ID);
		rootNode.setType(NodeType.ROOT);
		return rootNode;
	}
	
	public static TreeNode createfactoryNode(String parentID, int sequence) {
		TreeNode factoryNode = new TreeNode();
		RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
		int[] range = randomNumberGenerator.generateRandomRange();
		factoryNode.setParent(parentID);
		factoryNode.setSequence(sequence);
		factoryNode.setMinRange(range[0]);
		factoryNode.setMaxRange(range[1]);
		factoryNode.setText(String.format(Constants.FACTORY_TEXT, 
				sequence, factoryNode.getMinRange(), factoryNode.getMaxRange()));
		factoryNode.setType(NodeType.FACTORY);
		return factoryNode;
	}
	
	public static TreeNode createChildNode(String parentID, int minRange, int maxRange) {
		RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(minRange, maxRange);
		TreeNode childNode = new TreeNode();
		childNode.setParent(parentID);
		childNode.setMinRange(minRange);
		childNode.setMaxRange(maxRange);
		childNode.setText(String.format("%s", randomNumberGenerator.generateRandomNumber()+""));
		childNode.setType(NodeType.CHILDREN);
		return childNode;
	}

}
