package com.ganith.tree.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class holding event attributes
 * @author Pavithra
 *
 */
@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	
	/**
	 * Name of the TreeNode
	 */
	private String nodeName;
	
	/**
	 * Type of tree node [Factory, Child]
	 */
	private String nodeType;
	
	/**
	 * Type of event performed
	 */
	private Type eventType;
	
	/**
	 * Time at which the event occured
	 */
	private long timestamp;
	
	public enum Type {
        CREATED,
        DELETED
    }

}
