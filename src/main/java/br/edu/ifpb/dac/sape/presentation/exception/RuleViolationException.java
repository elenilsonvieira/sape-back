package br.edu.ifpb.dac.sape.presentation.exception;

public class RuleViolationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RuleViolationException(String message) {
		super(message);
	}

}
