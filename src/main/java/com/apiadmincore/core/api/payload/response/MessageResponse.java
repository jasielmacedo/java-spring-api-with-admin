package com.apiadmincore.core.api.payload.response;


public class MessageResponse 
{
	private String message;
	
	private int error;
	
	public MessageResponse() {}
	
	public MessageResponse(String message)
	{
		this.message = message;
		this.error = 1;
	}
	
	public MessageResponse(String message, int error)
	{
		this.error = error;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
	
	
	
}
