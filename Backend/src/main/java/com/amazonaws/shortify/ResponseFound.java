package com.amazonaws.shortify;

public class ResponseFound extends RuntimeException{
	public ResponseFound(String uri) {
		super(uri);
	}
}
