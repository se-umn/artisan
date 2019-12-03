package de.unipassau.abc.carving.exceptions;

import de.unipassau.abc.exceptions.ABCException;

public class CarvingException extends ABCException {

	private static final long serialVersionUID = -7157628861264610513L;

	public CarvingException() {
		super();
	}

	public CarvingException(String message) {
		super(message);
	}

	public CarvingException(String message, Throwable cause) {
		super(message, cause);
	}

}
