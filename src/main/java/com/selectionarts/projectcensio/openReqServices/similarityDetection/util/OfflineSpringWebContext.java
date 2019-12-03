package com.selectionarts.projectcensio.openReqServices.similarityDetection.util;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.context.IWebContext;

public class OfflineSpringWebContext implements IWebContext {

	private ServletContext servletContext;
	private HttpSession httpSession;
	private HttpServletResponse httpServletResponse;
	private HttpServletRequest httpServletRequest;
	private final Map<String,Object> variables;
	
	public OfflineSpringWebContext() {
		this.variables = new LinkedHashMap<String, Object>();
	}
	
	public OfflineSpringWebContext(ServletContext servletContext, HttpSession httpSession,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		super();
		this.servletContext = servletContext;
		this.httpSession = httpSession;
		this.httpServletResponse = httpServletResponse;
		this.httpServletRequest = httpServletRequest;
		this.variables = new LinkedHashMap<String, Object>();
	}

	@Override
	public Locale getLocale() {

		return Locale.GERMAN;
	}

	@Override
	public boolean containsVariable(String name) {
		
		return this.variables.containsKey(name);
	}

	@Override
	public Set<String> getVariableNames() {
		
		return this.variables.keySet();
	}

	@Override
	public Object getVariable(String name) {
		
		return this.variables.get(name);
	}
	
	public void setVariable(final String name, final Object value) {
        this.variables.put(name, value);
    }

	@Override
	public HttpServletRequest getRequest() {
		
		return this.httpServletRequest;
	}

	@Override
	public HttpServletResponse getResponse() {

		return this.httpServletResponse;
	}

	@Override
	public HttpSession getSession() {

		return this.httpSession;
	}

	@Override
	public ServletContext getServletContext() {

		return this.servletContext;
	}
    
    
}
