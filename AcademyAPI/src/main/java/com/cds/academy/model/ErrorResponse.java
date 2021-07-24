package com.cds.academy.model;

public class ErrorResponse {

	public ErrorResponse(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}
	
	int code;
	String reason;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
