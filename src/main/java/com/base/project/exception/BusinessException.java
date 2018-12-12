package com.base.project.exception;

import org.springframework.http.HttpStatus;

/**
 * Superclasse para erros de negocio
 * @author rafaelboekel
 *
 */
public abstract class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;
	
	private String[] args;
	
	public BusinessException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}	

	@Override
	public String getMessage() {		
		return this.getMessageKey();
	}
	
	/**
	 * Retorna chave da mensagem de erro baseado no nome da excecao.
	 * @return String no padrao 'nome-da-minha-excecao'
	 */
	public final String getMessageKey() {
		
		String msgKey = this.getClass()
							.getSimpleName()
							.replace("Exception", "")
							.replaceAll("([A-Z])", "-$1")
							.toLowerCase();
		
		return msgKey.startsWith("-") ? msgKey.replaceFirst("-", "") : msgKey;
					
	}
		
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
	
	/**
	 * Argumentos utilizados pelos placeholders ({0}, {1}, etc...) do 'message.properties'
	 * @return
	 */
	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}
}
