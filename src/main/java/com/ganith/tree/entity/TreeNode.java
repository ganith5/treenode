package com.ganith.tree.entity;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 
 * @author Pavithra
 *
 */
@Data
@Document(collection = "node")
public class TreeNode {
	
	@Id
	private String id;
	
	private String parent;
	
	private String text;
	
	private int minRange;
	
	private int maxRange;
	
	private int sequence;
	
	public enum NodeType {ROOT, FACTORY, CHILDREN};
	
	private NodeType type;
	
	private State state = new State();
	
	@Data
	class State {
		private boolean opened = false;
		private boolean selected = false;
	}
	
}


