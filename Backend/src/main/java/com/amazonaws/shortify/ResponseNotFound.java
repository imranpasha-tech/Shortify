package com.amazonaws.shortify;

public class ResponseNotFound extends RuntimeException{
	public ResponseNotFound(String msg) {
		super(msg);
	}
}
