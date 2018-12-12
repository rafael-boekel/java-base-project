package com.base.project.application.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.base.project.exception.handler.JavaBaseProjectExceptionHandler.FeignErrorDecoder;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(value = "another-api", 
				url = "${java-base-project.another-api.url:null}", 
				configuration = {FeignErrorDecoder.class}, 
				decode404 = true)
public interface AnotherApi {
	
	@GetMapping("/another/{id}")
	JsonNode find(@PathVariable("id") Integer id);
	
}
