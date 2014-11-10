package com.ganith.tree.entity;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void toNodeConfigurationJson() throws JsonParseException,JsonMappingException,IOException{
		TreeNode nodeConfiguration = new TreeNode();
		nodeConfiguration.setId("id1");
		nodeConfiguration.setParent("#");
		nodeConfiguration.setText("Test Node");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(nodeConfiguration);
		TreeNode actualNode = objectMapper.readValue(jsonString, TreeNode.class);
		assertEquals(nodeConfiguration, actualNode);
	}
	
	@Test
	public void jsonToNodeConfiguration() throws JsonParseException,JsonMappingException,IOException{
		String json = "{\"id\":\"id1\",\"parent\":\"#\",\"text\":\"Test Node\"}";
		TreeNode nodeConfiguration = new ObjectMapper().readValue(json, TreeNode.class);
		assertEquals(nodeConfiguration.getId(), "id1");
		
	}
	
	@Test
	public void intToJson() throws JsonGenerationException,JsonMappingException,IOException{
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("insert", 2);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(map);
		Map<String,Integer> returnMap = objectMapper.readValue(jsonString, Map.class);
		assertEquals(map, returnMap);
	}

}
