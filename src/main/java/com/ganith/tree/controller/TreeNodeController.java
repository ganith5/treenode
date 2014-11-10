package com.ganith.tree.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.ganith.tree.entity.Event;
import com.ganith.tree.entity.TreeNode;
import com.ganith.tree.service.TreeNodeEventService;
import com.ganith.tree.service.TreeNodeGeneratorService;

/**
 * Controller handling requests of Tree operations.
 * 
 * @author Pavithra
 * 
 */
@Controller
@RequestMapping("/")
@Slf4j
public class TreeNodeController {

	@Inject
	private TreeNodeGeneratorService treeNodeGeneratorService;

	@Inject
	private TreeNodeEventService treeNodeEventService;
	
	private Map<DeferredResult<List<Event>>, Integer> updateRequests = new ConcurrentHashMap<DeferredResult<List<Event>>, Integer>();

	@RequestMapping(value = "/", method = { RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public ModelAndView getNodes() throws Exception {
		log.debug("----- Inital state of Tree loaded ---- ");
		ObjectMapper mapper = new ObjectMapper();
		Collection<TreeNode> nodeList = treeNodeGeneratorService.getAllNodes();
		String returnNodesAsJson;
		Map<String, String> map = new HashMap<String, String>();
		try {
			returnNodesAsJson = mapper.writeValueAsString(nodeList);
			map.put("nodes", returnNodesAsJson);
			// treeNodeEventService.refresh(updatesCount);

		} catch (Exception e) {
			throw new Exception();
		}

		return new ModelAndView("index", map);
	}

	@RequestMapping(value = "/createFactory", method = { RequestMethod.POST }, produces = "application/json")
	public @ResponseBody
	Collection<TreeNode> createFactory(@RequestParam String parentId) {

		List<TreeNode> nodeList = new ArrayList<TreeNode>();

		TreeNode factoryNode = treeNodeGeneratorService
				.createFactoryNode(parentId);
		nodeList.add(factoryNode);
		return nodeList;
	}

	@RequestMapping(value = "/createChildren", method = { RequestMethod.POST }, produces = "application/json")
	public @ResponseBody
	Collection<TreeNode> generateChildren(@RequestParam String parentId,
			@RequestParam String parentText, @RequestParam int numOfChildren) throws Exception{
		log.debug(
				"Create children with parentId , parentText, numOfChildren = {} , {} , {} ",
				parentId, parentText, numOfChildren);
		return treeNodeGeneratorService.generateChildren(parentId,
				numOfChildren);
	}

	@RequestMapping(value = "/deleteNode", method = { RequestMethod.POST }, produces = "application/json")
	public @ResponseBody
	Collection<TreeNode> deleteNode(@RequestParam String nodeId,
			@RequestParam String nodeText) {
		treeNodeGeneratorService.deleteFactoryAndChildren(nodeId);
		return new ArrayList<TreeNode>();
	}


	@RequestMapping(value = "/poll", method = { RequestMethod.GET }, produces = "application/json")
	public @ResponseBody
	DeferredResult<List<Event>> poll() {
		log.debug("Polling for new updates ......" + updateRequests.size());
		final DeferredResult<List<Event>> result = new DeferredResult<List<Event>>(
				null, Collections.emptyList());
		Random random = new Random();
		this.updateRequests.put(result, random.nextInt());
		result.onCompletion(new Runnable() {
			public void run() {
				if (updateRequests.containsKey(result)) {
					updateRequests.remove(result);

				}
			}
		});

		List<Event> updates = getUpdatesList();
		if (!updates.isEmpty()) {
			result.setResult(updates);
		}

		return result;
	}

	private List<Event> getUpdatesList() {
		List<Event> updates = new ArrayList<Event>();
		Queue<Event> events = treeNodeEventService.getEventQueue();
		for (Event event : events) {
			updates.add(event);
		}
		Collections.reverse(updates);
		log.debug("Number of updates available to be published {} : ",
				updates.size());
		return updates;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleExceptions(Exception ex) {
		log.debug("Handle exception%%%%%%%%%%%%%%%%%%%%%%%%");
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", "Error processing request");
		return new ModelAndView("index", map);
	}

}
