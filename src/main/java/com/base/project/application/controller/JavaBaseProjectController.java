package com.base.project.application.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.base.project.infrastructure.event.ResourceCreatedEvent;

@RestController
@RequestMapping("/resources")
public class JavaBaseProjectController {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		
		Object o = null;
		return o != null ? ResponseEntity.ok(o) : ResponseEntity.notFound().build();
	}
	
	@GetMapping
	public Page<Object> findAll(Pageable pageable) {
		return new PageImpl<>(Collections.emptyList(), pageable, 0);
	}
	
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody Object o, HttpServletResponse response) {
		
		Object createdObject = new Object();
		this.publisher.publishEvent(new ResourceCreatedEvent(this, response, (long)createdObject.hashCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
	}
	
	@DeleteMapping(value="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long id) {		
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Object obj) {
		Object updatedObject = new Object();
		return ResponseEntity.ok(updatedObject);
	}
}
