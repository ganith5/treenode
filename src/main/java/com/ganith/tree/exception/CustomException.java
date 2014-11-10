package com.ganith.tree.exception;

public class CustomException extends RuntimeException{
	private static final long serialVersionUID = -2340046533138781341L;
	
	public CustomException(String s) {
        super(s);
    }
}
