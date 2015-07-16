package br.com.aexo.atlas.infraestructure.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class IncludeExcludeFieldFilter extends SimpleBeanPropertyFilter {

	private Stack<String> stack = new Stack<String>();
	private String[] includes;
	private String[] excludes;

	public IncludeExcludeFieldFilter(String[] includes,String[] excludes) {
		this.includes = includes;
		this.excludes = excludes;
	}

	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		stack.push(writer.getName());
		boolean exclude = configurePathToExcludeOrInclude();
		if (!exclude)
			writer.serializeAsField(bean, jgen, provider);

		stack.pop();
	}

	private boolean configurePathToExcludeOrInclude() {
		boolean excluir = false;
		for (String path : excludes) {
			List<String> pathToExclude = Arrays.asList(path.split("[.]"));
			
			if (stack.size() == pathToExclude.size()) {
				for (int i=0;i<stack.size();i++){
					if ((stack.get(i).equals(pathToExclude.get(i)) && i==stack.size()-1) || pathToExclude.get(i).equals("*")){
						excluir = true;
					}
				}
			}
		}
		
		for (String path : includes) {
			List<String> pathToInclude = Arrays.asList(path.split("[.]"));
			
			String actualPath = stack.toString();
			if (pathToInclude.toString().equals(actualPath)) {
				excluir = false;
			}
		}
		return excluir;
	}

	@Override
	protected boolean include(com.fasterxml.jackson.databind.ser.BeanPropertyWriter writer) {
		return true;
	}
}