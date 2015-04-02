package br.com.aexo.atlas.templates;

import br.com.aexo.atlas.AtlasException;

public class TemplateException extends AtlasException {

	private static final long serialVersionUID = 1L;

	public TemplateException() {
		super();
	}

	public TemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable cause) {
		super(cause);
	}

}
