package com.base.project.infrastructure.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.base.project.infrastructure.event.ResourceCreatedEvent;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

	@Override
	public void onApplicationEvent(ResourceCreatedEvent event) {
		
		HttpServletResponse response = event.getResponse();
		this.addHeaderLocation(response, event);
	}

	/**
	 * Adiciona na resposta o header 'Location', que contem o caminho do recurso criado.
	 * @param response
	 * @param id
	 */
	private void addHeaderLocation(HttpServletResponse response, ResourceCreatedEvent event) {
		
		if(event.getId() != null) {
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
																.path("/{id}")
																.buildAndExpand(event.getId())
																.toUri();
		
			response.setHeader("Location", uri.toASCIIString());
			
		} else if(StringUtils.isNotEmpty(event.getResourcePath())) {
			response.setHeader("Location", event.getResourcePath());
		}	
	}

}
