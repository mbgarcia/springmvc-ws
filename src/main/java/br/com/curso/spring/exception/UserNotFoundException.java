package br.com.curso.spring.exception;

public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 2776826916230787832L;

	public UserNotFoundException(String publicId) {
		super(String.format("User [%s] not found",publicId));
	}
}
