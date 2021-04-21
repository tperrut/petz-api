package br.com.desafio.petz.api.web.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.desafio.petz.api.web.error.BusinessExceptionDetail;
import br.com.desafio.petz.api.web.error.ClientExceptionDetail;
import br.com.desafio.petz.api.web.error.DataIntegrityViolationExceptionDetails;
import br.com.desafio.petz.api.web.error.ErrorDetail;
import br.com.desafio.petz.api.web.error.InternalServerExceptionDetail;
import br.com.desafio.petz.api.web.error.JsonNotReadableDetail;
import br.com.desafio.petz.api.web.error.ResourceNotFoundDetails;
import br.com.desafio.petz.api.web.error.ValidationErrorDetail;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.exception.NameNotFoundException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

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
	public @ResponseBody ResourceNotFoundDetails handleResourceNotFoundException (ResourceNotFoundException e){
		ResourceNotFoundDetails ex = ResourceNotFoundDetails.builder().
		detalhe(e.getMessage()).
		developerMessage(ResourceNotFoundException.class.getName()).
		statusCode(HttpStatus.NOT_FOUND.value()).
		timestamp(new Date()).
		titulo(HttpStatus.NOT_FOUND.getReasonPhrase()).
		build();

		logger.error(ex.toString());

		return ex;
		
	}
	
	@ExceptionHandler(NameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ResourceNotFoundDetails handleResourceNameNotFoundException (NameNotFoundException e){
		ResourceNotFoundDetails ex = ResourceNotFoundDetails.builder().
		detalhe(e.getMessage()).
		developerMessage(NameNotFoundException.class.getName()).
		statusCode(HttpStatus.NOT_FOUND.value()).
		timestamp(new Date()).
		titulo(HttpStatus.NOT_FOUND.getReasonPhrase()).
		build();

		logger.error(ex.toString());

		return ex;
		
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody ErrorDetail duplicateEmailException(DataIntegrityViolationException e) {
		DataIntegrityViolationExceptionDetails ex = DataIntegrityViolationExceptionDetails.builder().
				detalhe(e.getRootCause().getMessage()).
				developerMessage(BusinessException.class.getName()).
				statusCode(HttpStatus.CONFLICT.value()).
				timestamp(new Date()).
				titulo("Erro: Email já cadastrado ou vazio!").
				build();

				logger.error(ex.toString());
				return ex;
    }
	
	@ExceptionHandler(BusinessException.class) 
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public @ResponseBody ErrorDetail handleResourceBusinessException (BusinessException e){
		BusinessExceptionDetail ex = BusinessExceptionDetail.builder().
		detalhe(e.getCause().getMessage()).
		developerMessage(BusinessException.class.getName()).
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
		timestamp(new Date()).
		titulo(e.getMessage()).
		build();

		e.printStackTrace();

		logger.error(e.toString());

		return ex;
		
	}
	
	@ExceptionHandler(InternalServerException.class) 
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorDetail handleResourceInternalServerException (InternalServerException e){
		InternalServerExceptionDetail ex = InternalServerExceptionDetail.builder().
		detalhe(e.getMessage()).
		developerMessage(InternalServerException.class.getName()).
		statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
		timestamp(new Date()).
		titulo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).
		build();
		e.printStackTrace();

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
			
		
		JsonNotReadableDetail jnr = JsonNotReadableDetail.builder().
				detalhe(ex.getMessage().substring(0,50).concat(" ...")).
				developerMessage(JsonNotReadableDetail.class.getName()).
				statusCode(statusCode).
				timestamp(new Date()).
				titulo(title).
				build();
				 
		ex.printStackTrace();

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
		ClientExceptionDetail ise = ClientExceptionDetail.builder().
				detalhe(ex.getLocalizedMessage()).
				developerMessage(ClientExceptionDetail.class.getName()).
				statusCode(status.value()).
				titulo(status.getReasonPhrase()).
				timestamp(new Date()).
				build();

		logger.error(ise.toString());

		return new ResponseEntity<>(ise,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ClientExceptionDetail ise = ClientExceptionDetail.builder().
		detalhe(ex.getCause().getMessage()).
		developerMessage(InternalServerException.class.getName()).
		statusCode(status.value()).
		titulo(status.getReasonPhrase()).
		timestamp(new Date()).
		build();

		logger.error(ise.toString());

		return new ResponseEntity<>(ise,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO criar uma Excpetion para tratar erros genéricos do cliente
		ClientExceptionDetail ise = ClientExceptionDetail.builder().
				detalhe(ex.getCause().getMessage()).
				developerMessage(InternalServerException.class.getName()).
				statusCode(status.value()).
				titulo(status.getReasonPhrase()).
				timestamp(new Date()).
				build();

		logger.error(ise.toString());

		return new ResponseEntity<>(ise,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ClientExceptionDetail ise = ClientExceptionDetail.builder().
				detalhe(ex.getCause().getMessage()).
				developerMessage(InternalServerException.class.getName()).
				statusCode(status.value()).
				timestamp(new Date()).
				titulo(status.getReasonPhrase()).
				build();
		
		ex.printStackTrace();	
		logger.error(ise.toString());

		return new ResponseEntity<>(ise,status);
		

	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		InternalServerExceptionDetail ise = InternalServerExceptionDetail.builder().
				detalhe(ex.getMessage()).
				developerMessage(InternalServerException.class.getName()).
				statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
				timestamp(new Date()).
				titulo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).
				build();

				logger.error(ise.toString());



		return new ResponseEntity<>(ise,status);
		

	}
	
	
	
}
