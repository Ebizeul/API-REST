package com.dao;


public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Redéfinition d'un constructeur de RuntimeException.
	 * 
	 * @param message le message de l'exception levée.
	 */
	public DAOException(String message) {
		super(message);
	}
	
	/**
	 * Redéfinition d'un constructeur de RuntimeException.
	 * 
	 * @param message le message de l'exception levée.
	 * @param cause la cause de l'exception levée.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
