package br.com.aexo.atlas.commons.templates;

/**
 * Template que representa uma configuração de um balanceador
 * 
 * @author Carlos Alberto
 *
 */
public class Template {

	private TemplateType type;
	private String name;
	private String script;

	public TemplateType getType() {
		return type;
	}

	public void setType(TemplateType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
