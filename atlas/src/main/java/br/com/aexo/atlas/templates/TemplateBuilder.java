package br.com.aexo.atlas.templates;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import freemarker.template.Configuration;

public class TemplateBuilder {

	private static Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

	/**
	 * build template in output de acordo com o context
	 * 
	 * @param out
	 * @param template
	 * @param context
	 */
	public void buildIn(OutputStream out, Template template, Context context) {
		try {
			StringReader script = new StringReader(template.getScript());
			freemarker.template.Template scriptTemplate = new freemarker.template.Template("script", script, CONFIGURATION);
			scriptTemplate.process(context, new OutputStreamWriter(out));
		} catch (Exception e) { 
			throw new TemplateException("cannot build template", e);
		}
	}

}
