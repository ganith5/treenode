package com.ganith.tree.aspects;

import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;

import com.ganith.tree.entity.Event;
import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.service.TreeNodeEventService;

/**
 * Contains AOP methods that are responsible for adding events {@link Event} to a Queue.  
 * @author Pavithra
 *
 */

@Slf4j
@Aspect
public class NotifyAspects {
	
	@Inject
	private TreeNodeEventService treeNodeEventService;
	
	
	@Async
	@AfterReturning(pointcut = "@annotation(com.ganith.tree.aspects.NotifyClient) "
			+ "&& execution(* com.ganith.tree.service.TreeNodeGeneratorService.createFactoryNode(..))",
			returning = "factoryNode") 
    public void notifyClientForFactoryCreation(TreeNode factoryNode) throws Throwable {
		log.debug("Adding event for factory node creation = {} ", factoryNode.getText());
		treeNodeEventService.logEventMessages(new Event(factoryNode.getText(), factoryNode.getType().toString(), Event.Type.CREATED, getCurrentTime()));
    }
	
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	@Async
	@AfterReturning(pointcut = "@annotation(com.ganith.tree.aspects.NotifyClient) "
			+ "&& execution(* com.ganith.tree.service.TreeNodeGeneratorService.generateChildren(java.lang.String, int))"
			+ "&& args(parentId, numOfChildren)", returning="childNodesList") 
    public void notifyClientForChildCreation(String parentId, int numOfChildren, List<TreeNode> childNodesList) throws Throwable {
		log.debug("Adding event for childNode created [parent] = {}, [num] = {}", parentId, numOfChildren);
		for(TreeNode treeNode : childNodesList) {
			treeNodeEventService.logEventMessages(new Event(treeNode.getText(), treeNode.getType().toString(), Event.Type.CREATED, getCurrentTime()));
		}
    }
	
	@Async
	@AfterReturning(pointcut = "@annotation(com.ganith.tree.aspects.NotifyClient) "
			+ "&& execution(* com.ganith.tree.service.TreeNodeGeneratorService.deleteFactoryAndChildren(java.lang.String))"
			+ "&& args(nodeId)") 
    public void notifyClientForDeletion(String nodeId) throws Throwable {
		log.debug("Adding event for node deletion [nodeId] = {} ", nodeId);
		treeNodeEventService.logEventMessages(new Event(nodeId, "FACTORY", Event.Type.DELETED, getCurrentTime()));
    }
	
	
	

}
