package com.pegasus.ezweb.servlet;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractCommand {

	public String process(HttpServletRequest request){
		return "";
	}
}
