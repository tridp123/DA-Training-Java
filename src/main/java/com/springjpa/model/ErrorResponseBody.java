package com.springjpa.model;


import org.joda.time.DateTime;

public class ErrorResponseBody {

	private DateTime timestamp;
	private int status;
	private String error;
	private String message;

	public ErrorResponseBody(int status, String error, String message) {
		this.timestamp = DateTime.now();
		this.message = message;
		this.error = error;
		this.status = status;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
