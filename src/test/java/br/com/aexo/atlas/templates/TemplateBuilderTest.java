package br.com.aexo.atlas.templates;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import br.com.aexo.atlas.templates.Context;
import br.com.aexo.atlas.templates.Template;
import br.com.aexo.atlas.templates.TemplateBuilder;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TemplateBuilderTest {

	private TemplateBuilder builder;

	@Before
	public void setup(){
		builder = new TemplateBuilder();
	}
	
	@Test
	public void deveriaGerarUmTemplateUsandoOfreeMaker(){
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Template template= new Template();
		template.setScript("${name}");;
		Context context = new Context();
		context.set("name", "carlos");
		
		builder.buildIn(out,template,context);
		
		assertThat("carlos",is(new String(out.toByteArray())));
	}
	
	
}
