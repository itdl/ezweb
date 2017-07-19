package com.pegasus.ezweb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EZwebServlet extends HttpServlet {
	
	/**
	 * Destroy servlet
	 */
	public void destroy(){
		super.destroy();
	}
	
	/**
	 * Initiate servlet
	 * read parameters in configuration file
	 */
	public void init(){
		
	}
	
	/**
	 * 
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		//System.out.println(request.getRequestURL());
		//System.out.println(request.getRequestURI());
		//System.out.println(request.getQueryString());
		
		//Get Command name from JSP page name
		//System.out.println(getJSPPageName(request));
		//System.out.println(buildCommandName(getJSPPageName(request)));
		//String commandName="com.pegasus.ezweb.servlet."+buildCommandName(getJSPPageName(request));
		String commandName=request.getParameter("commandName");
		//System.out.println(commandName);
		
		PrintWriter out=response.getWriter();
		
		try{
			Class commandClass=Class.forName(commandName);
			Method commandMethod=commandClass.getMethod("process", new Class[] {HttpServletRequest.class});
			StringBuffer jsonString=new StringBuffer();
			
			if("jsonp".equals(request.getParameter("responseType")) && request.getParameter("callback")!=null && !request.getParameter("callback").isEmpty()){
				jsonString.append(request.getParameter("callback")+"(");
				jsonString.append(commandMethod.invoke(commandClass.newInstance(), new Object[] {request}).toString());
				jsonString.append(")");
			}else{
				jsonString.append(commandMethod.invoke(commandClass.newInstance(), new Object[] {request}).toString());
			}
			out.println(jsonString);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
	
	/**
	 * Build Command Name, uppercase the first character
	 * @param jspPageName
	 * @return
	 */
	private String buildCommandName(String jspPageName) {
		
		StringBuffer aa=new StringBuffer(jspPageName.substring(0,1).toUpperCase());
		aa.append(jspPageName.substring(1));
		aa.append("Command");
		return aa.toString();
	}

	/**
	 * Get the Jsp page name
	 * @param request
	 * @return String
	 */
	private String getJSPPageName(HttpServletRequest request){
		
		String uri=request.getRequestURI();
		int a1=uri.lastIndexOf("/");
		int a2=uri.lastIndexOf(".jsp");
		
		return uri.substring(a1+1, a2);
	}

}
