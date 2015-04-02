package br.com.aexo.atlas;

public class AtlasException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AtlasException() {
		super();
	}

	public AtlasException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AtlasException(String message, Throwable cause) {
		super(message, cause);
	}

	public AtlasException(String message) {
		super(message);
	}

	public AtlasException(Throwable cause) {
		super(cause);
	}

}
