package com.springjpa.controller;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.InternalException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.ErrorResponseBody;
import com.springjpa.util.LogUtil;


@RestControllerAdvice
public class BaseControllerExceptionHandler {

	static final String URL_NOT_FOUND_ERROR_MSG = "URL Not Found";
	static final String DATA_NOT_FOUND_ERROR_MSG = "Data Not Found";
	static final String INTERNAL_SERVER_ERROR_MSG = "Server got errors";
	static final String BAD_REQUEST_ERROR_MSG = "Bad Request";
	static final String FORBIDDEN_ERROR_MSG = "Not permitted";

	public static final Logger log = LoggerFactory.getLogger(BaseControllerExceptionHandler.class);

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> notFoundError(NoHandlerFoundException ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), URL_NOT_FOUND_ERROR_MSG,
				ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<Object> notFoundDataError(NoDataFoundException ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), DATA_NOT_FOUND_ERROR_MSG,
				ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<Object> internalError(InternalException ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				INTERNAL_SERVER_ERROR_MSG, ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> badRequestError(Exception ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_ERROR_MSG,
				ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> customBadRequestError(BadRequestException ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_ERROR_MSG,
				ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> forbiddenError(AccessDeniedException ex) {
		LogUtil.error(log, ex.getMessage());
		ErrorResponseBody body = new ErrorResponseBody(HttpStatus.FORBIDDEN.value(), FORBIDDEN_ERROR_MSG,
				ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.FORBIDDEN);
	}
}
