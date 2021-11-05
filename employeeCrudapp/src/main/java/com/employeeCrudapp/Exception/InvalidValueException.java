package com.employeeCrudapp.Exception;

public class InvalidValueException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	

	public InvalidValueException(String message) {

		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
  
  
 
 
	
}
