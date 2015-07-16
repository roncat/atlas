package br.com.aexo.atlas.infraestructure.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScoped
public class ServletResourceProvider {

//	private HttpServletRequest request;
//	private HttpServletResponse response;
//	
//	
//	public void configure(ServletRequest request, ServletResponse response) {
//		this.request = (HttpServletRequest) request;
//		this.response = (HttpServletResponse) response;
//	}
//
//	@Produces
//	public HttpServletRequest proverRequest(){
//		return request;
//	}
//	
//	@Produces
//	public HttpServletResponse proverResponse(){
//		return response;
//	}
}
