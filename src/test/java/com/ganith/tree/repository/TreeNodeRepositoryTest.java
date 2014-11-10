package com.ganith.tree.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ganith.tree.entity.TreeNode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:mongo-config-test.xml")
public class TreeNodeRepositoryTest {

	@Inject
	private TreeNodeRepository nodeConfigurationRepository;

	@Inject
	private MongoTemplate mongoTemplate;


	@Test
	public void testSaveNodeConfiguration() {
		TreeNode treeNode = nodeConfigurationRepository
				.save(getNodeConfiguration());
		assertEquals("f-123", treeNode.getId());
		assertEquals("Test Node", treeNode.getText());
		cleanTreeNodes(treeNode);
	}

	@Test
	public void findNodeConfiguration() {
		TreeNode treeNode = getNodeConfiguration();
		nodeConfigurationRepository.save(treeNode);
		treeNode = nodeConfigurationRepository.findOne(treeNode.getId());
		assertNotNull(treeNode);
		cleanTreeNodes(treeNode);
	}

	@Test
	public void testCreationOfChildren() {
		String parentId = "parent-id";
		parentChildNode(parentId);

		Collection<TreeNode> childList = nodeConfigurationRepository
				.findByParent(parentId);
		assertEquals(1, childList.size());
		for (Iterator<TreeNode> iterator = childList.iterator(); iterator
				.hasNext();) {
			TreeNode nodeConfiguration = (TreeNode) iterator.next();
			nodeConfigurationRepository.delete(nodeConfiguration);
		}
		TreeNode childNode = new TreeNode();
		childNode.setId("child-id");
		childNode.setParent(parentId);
		childNode.setText("Rediff");
		nodeConfigurationRepository.save(childNode);
		childList = nodeConfigurationRepository.findByParent(parentId);
		assertEquals(1, childList.size());
		for (Iterator<TreeNode> iterator = childList.iterator(); iterator
				.hasNext();) {
			TreeNode nodeConfiguration = (TreeNode) iterator.next();
			assertEquals(childNode.getText(), nodeConfiguration.getText());
		}

		cleanParentChildTreeNodes(parentId);

	}

	@Test
	public void testQueryChildrenWithCache() {
		String parentId = "f-123";
		parentChildNode(parentId);
		List<TreeNode> nodeList = nodeConfigurationRepository
				.findByParent(parentId);
		assertEquals(1, nodeList.size());
		cleanParentChildTreeNodes(parentId);

	}

	@Test
	public void testDeleteNodesByParentId() {
		String parentId = "parent-id";
		parentChildNode(parentId);
		nodeConfigurationRepository.removeChildrenByParentId(parentId);
		int childrenCount = nodeConfigurationRepository.findByParent(parentId)
				.size();
		assertEquals(0, childrenCount);
		cleanParentChildTreeNodes(parentId);
	}

	private void cleanTreeNodes(TreeNode treeNode) {
		mongoTemplate.remove(treeNode);
		assertNull(nodeConfigurationRepository.findOne(treeNode.getId()));
	}

	private TreeNode getNodeConfiguration() {
		TreeNode nodeConfiguration = new TreeNode();
		nodeConfiguration.setId("f-123");
		nodeConfiguration.setParent("#");
		nodeConfiguration.setText("Test Node");
		return nodeConfiguration;
	}

	private void parentChildNode(String parentId) {
		TreeNode parentNode = new TreeNode();

		parentNode.setId(parentId);
		parentNode.setParent("#");
		parentNode.setText("Parent Node");
		nodeConfigurationRepository.save(parentNode);

		TreeNode childNodeConfiguration = new TreeNode();

		childNodeConfiguration.setId("c-123");
		childNodeConfiguration.setParent(parentId);
		childNodeConfiguration.setText("Child Node");
		nodeConfigurationRepository.save(childNodeConfiguration);
	}

	private void cleanParentChildTreeNodes(String id) {
		nodeConfigurationRepository.delete(id);
		nodeConfigurationRepository.removeChildrenByParentId(id);
		TreeNode parentNode = nodeConfigurationRepository.findOne(id);
		assertNull(parentNode);
		List<TreeNode> childList = nodeConfigurationRepository.findByParent(id);
		assertEquals(0, childList.size());
	}

}
