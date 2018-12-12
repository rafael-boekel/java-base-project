package com.base.project.infrastructure.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class ResourceCreatedEvent extends ApplicationEvent {

/**
 * Evento indicando a criacao de um recurso no sistema
 * @author rafaelboekel
 *
 */
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	private Long id;
	private String resourcePath;

	public ResourceCreatedEvent(Object source, HttpServletResponse response, Long id) {
		super(source);
		this.response = response;
		this.id = id;
	}
	
	public ResourceCreatedEvent(Object source, HttpServletResponse response, String resourcePath) {
		super(source);
		this.response = response;
		this.resourcePath = resourcePath;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Long getId() {
		return id;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	
}
