package br.com.aexo.atlas.infraestructure.cdi;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.aexo.atlas.System;

/**
 * helper for mount url in swagger browser
 * 
 * @author carlosr
 *
 */
@WebServlet(urlPatterns = "/swagger-apidocs")
public class SwaggerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private System system;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String context = request.getContextPath();
		StringBuffer sb = new StringBuffer();
		sb.append(context);
		sb.append(system.getUrlPathResources());
		sb.append("/api-docs");
		response.getOutputStream().write(sb.toString().getBytes());
	}
}
