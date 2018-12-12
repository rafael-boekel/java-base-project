package com.base.project.exception.handler;

import static org.springframework.http.HttpStatus.valueOf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.base.project.exception.ApiIntegrationException;
import com.base.project.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class JavaBaseProjectExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({BusinessException.class})
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
		
		String userMessage = this.messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());		
		String cause = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), ex.getHttpStatus(), request);
	}
	
	@ExceptionHandler({Exception.class})
	protected ResponseEntity<Object> handleUnexpectedError(Exception ex, WebRequest request) {
		
		log.error("Unexpected error.", ex);
		String userMessage = this.messageSource.getMessage("unexpected-error", null, LocaleContextHolder.getLocale());
		String cause = ex.toString();
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, 
															HttpHeaders headers, 
															HttpStatus status, 
															WebRequest request) {
		
		String userMessage = this.messageSource.getMessage("invalid-data-format", null, LocaleContextHolder.getLocale());
		String cause = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, 
																	HttpHeaders headers, 
																	HttpStatus status, 
																	WebRequest request) {
		
		String userMessage = this.messageSource.getMessage("message-not-readable", null, LocaleContextHolder.getLocale());
		String cause = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, 
															    		HttpHeaders headers,
															            HttpStatus status,
															            WebRequest request) {
        
        String userMessage = this.messageSource.getMessage("invalid-request-part", null, LocaleContextHolder.getLocale());
		String cause = ex.toString();
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
	
	@ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
	public ResponseEntity<Object> handleUnsatisfiedServletRequestParameterException(UnsatisfiedServletRequestParameterException ex, WebRequest request){
					
		String userMessage = this.messageSource.getMessage("unsatisfied-parameters", 
															new String[] {ArrayUtils.toString(ex.getParamConditionGroups().toArray())}, 
															LocaleContextHolder.getLocale());
		String cause = ex.toString();
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																	HttpHeaders headers, 
																	HttpStatus status, 
																	WebRequest request) {
		
		List<Error> errors = this.createErrorList(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		
		String userMessage = this.messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
		String cause = ex.toString();
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		
		String userMessage = this.messageSource.getMessage("invalid-payload", null, LocaleContextHolder.getLocale());
		String cause = ExceptionUtils.getRootCauseMessage(ex);
		List<Error> errors = Arrays.asList(new Error(userMessage, cause));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Error> createErrorList(BindingResult bindingResult) {
		
		List<Error> errors = new ArrayList<>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String userMessage = this.messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String cause = fieldError.toString();
			errors.add(new Error(userMessage, cause));
		}
			
		return errors;
	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	public static class Error {
		
		private String userMessage;
		private String cause;
	}
	
	public static class FeignErrorDecoder implements ErrorDecoder {

		@SneakyThrows
		@Override
		public ApiIntegrationException decode(String methodKey, Response response) {
			
			final HttpStatus statusCode = valueOf(response.status());
			
			// realiza log do body da resposta
			String reason = null;
			
			if(response.body() != null) {				
				reason = IOUtils.toString(response.body().asInputStream(), Charset.defaultCharset());
			}
			
			StringJoiner sj = new StringJoiner(" ");
			sj.add("Error [")
				.add(statusCode.toString())
				.add("] on Feign call:")
				.add(methodKey);
				
			if(StringUtils.isNotBlank(reason)) {					
				sj.add("- Reason:")
					.add(reason);
			}
			
			if(statusCode.is4xxClientError()) { 
				log.warn(sj.toString());
			} else {
				log.error(sj.toString());				
			}
			
			throw new ApiIntegrationException(methodKey, statusCode);
		}		
	}
}
