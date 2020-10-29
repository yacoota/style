package com.itstyle.blog.common.exception;
/**
 *  自定义异常
 *  创建者  爪洼笔记
 *  博客 https://blog.52itstyle.vip
 *  创建时间	2019年8月15日
 */
public class RrException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    private String msg;
    
    private int code = 500;
    
    public RrException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RrException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public RrException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RrException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
