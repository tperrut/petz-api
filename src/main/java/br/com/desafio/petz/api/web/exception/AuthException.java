package br.com.desafio.petz.api.web.exception;

public class AuthException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AuthException() {}

	public AuthException(String msg) {
		super(msg);
	}

	public AuthException(String msg, Throwable t) {
		super(msg, t);
	}
}
	

