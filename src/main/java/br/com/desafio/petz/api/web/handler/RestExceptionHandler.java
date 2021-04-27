package br.com.desafio.petz.api.web.handler;

import br.com.desafio.petz.api.web.error.*;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.exception.NameNotFoundException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @RestControllerAdvice <p>is just a syntactic sugar for to: </p>
 * {@link ControllerAdvice} + {@link ResponseBody}
 *
 * 
 * @RestExceptionHandler Manipulador genérico de exceptions.
 * Captura as exceptions lançadas pela aplicação e retorna 
 * com uma resposta padronizada {@link ErrorDetail}.
 * 
 * @author tperrut
 *
 */

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorDetail handleResourceNotFoundException (ResourceNotFoundException e){
		ErrorDetail ex = ErrorDetailBuilder.withBuilder().
		withDetalhe(e.getMessage()).
		withDeveloperMessage(ResourceNotFoundException.class.getName()).
		withStatusCode(HttpStatus.NOT_FOUND.value()).
		withTimestamp(new Date()).
		withTitulo(HttpStatus.NOT_FOUND.getReasonPhrase()).
		build();

		logger.error(ex.toString());

		return ex;
		
	}
	
	@ExceptionHandler(NameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorDetail handleResourceNameNotFoundException (NameNotFoundException e){
		ErrorDetail ex = ErrorDetailBuilder.withBuilder().
			withDetalhe(e.getMessage()).
			withDeveloperMessage(NameNotFoundException.class.getName()).
			withStatusCode(HttpStatus.NOT_FOUND.value()).
			withTimestamp(new Date()).
			withTitulo(HttpStatus.NOT_FOUND.getReasonPhrase()).
			build();

		logger.error(ex.toString());

		return ex;
		
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody ErrorDetail duplicateEmailException(DataIntegrityViolationException e) {
		ErrorDetail ex = ErrorDetailBuilder.withBuilder().
				withDetalhe(e.getRootCause().getMessage()).
				withDeveloperMessage(DataIntegrityViolationException.class.getName()).
				withStatusCode(HttpStatus.CONFLICT.value()).
				withTimestamp(new Date()).
				withTitulo("Erro: Email já cadastrado ou vazio!").
				build();

				logger.error(ex.toString());
				return ex;
    }

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public @ResponseBody
	ErrorDetail handleResourceBusinessException(BusinessException e) {
		ErrorDetail ex = ErrorDetailBuilder.withBuilder().
				withDetalhe(e.getCause().getMessage()).
				withDeveloperMessage(BusinessException.class.getName()).
				withStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
				withTimestamp(new Date()).
				withTitulo(e.getMessage()).
				build();

		logger.error(e.toString());

		return ex;

	}
	
	@ExceptionHandler(InternalServerException.class) 
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handleResourceInternalServerException (InternalServerException e){
		ErrorDetail ex = ErrorDetailBuilder.withBuilder().
				withDetalhe(e.getMessage()).
				withDeveloperMessage(InternalServerException.class.getName()).
				withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
				withTimestamp(new Date()).
				withTitulo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).
		build();

		logger.error(ex.toString());

		return ex ;
    }

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// TODO Refator e tentar os handlers da classe mãe
		String title = "Json is invalid";
		HttpStatus statusName = HttpStatus.UNPROCESSABLE_ENTITY;
		int statusCode = statusName.value();
		
		if(" not a valid representation".contains(ex.getMessage()))
			title = "Erro de Conversão";
		else if("Required request body is missing".contains(ex.getMessage())) {
			statusCode= HttpStatus.BAD_REQUEST.value();
			statusName = HttpStatus.BAD_REQUEST;
			title = "Corpo da Requisição vazio";
		}
		
		if(ex.getCause() instanceof InvalidFormatException) {
			title = "Formato  de  Data Inválida: default format yyyy-MM-dd";
		}
			
		
		ErrorDetail jnr = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getMessage().substring(0,50).concat(" ...")).
				withDeveloperMessage(JsonNotReadableDetail.class.getName()).
				withStatusCode(statusCode).
				withTimestamp(new Date()).
				withTitulo(title).
				build();

		logger.error(ex.toString());

		return new ResponseEntity<>(jnr,statusName);

	}
	
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fields = ex.getBindingResult().getFieldErrors();
		String fieldErro =  fields.stream().map(FieldError::getField).collect(Collectors.joining(" | "));
		String fieldMsg =  fields.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" | "));
		ValidationErrorDetail ved = ValidationErrorDetail.builder().
				statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
				timestamp(new Date()).
				titulo(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).
				detalhe("Ver Field(s): ").
				field(fieldErro).
				fieldMessages(fieldMsg).
				developerMessage(ValidationErrorDetail.class.getName()).
				build();
		ex.printStackTrace();

		logger.error(ex.toString());

		return new ResponseEntity<>(ved,status);
		

	}
	
	// TODO refatorar p @ResponseBody
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetail erd = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getLocalizedMessage()).
				withDeveloperMessage(ClientExceptionDetail.class.getName()).
				withStatusCode(status.value()).
				withTitulo(status.getReasonPhrase()).
				withTimestamp(new Date()).
				build();
		logger.error(erd.toString());

		return new ResponseEntity<>(erd,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetail erd = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getLocalizedMessage()).
				withDeveloperMessage(MissingServletRequestParameterException.class.getName()).
				withStatusCode(status.value()).
				withTitulo(status.getReasonPhrase()).
				withTimestamp(new Date()).
				build();


		logger.error(erd.toString());

		return new ResponseEntity<>(erd,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO criar uma Excpetion para tratar erros genéricos do cliente
		ErrorDetail erd = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getLocalizedMessage()).
				withDeveloperMessage(MissingPathVariableException.class.getName()).
				withStatusCode(status.value()).
				withTitulo(status.getReasonPhrase()).
				withTimestamp(new Date()).
				build();


		logger.error(erd.toString());
		return new ResponseEntity<>(erd,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetail erd = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getLocalizedMessage()).
				withDeveloperMessage(HttpMediaTypeNotSupportedException.class.getName()).
				withStatusCode(status.value()).
				withTitulo(status.getReasonPhrase()).
				withTimestamp(new Date()).
				build();

		logger.error(erd.toString());
		
		return new ResponseEntity<>(erd,status);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetail  ise = ErrorDetailBuilder.withBuilder().
				withDetalhe(ex.getMessage()).
				withDeveloperMessage(Exception.class.getName()).
				withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
				withTimestamp(new Date()).
				withTitulo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).
				build();

				logger.error(ise.toString());

		return new ResponseEntity<>(ise,status);
		

	}
	
	
	
}
