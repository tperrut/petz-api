package br.com.desafio.petz.api.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.dto.DataDto;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.service.conveter.Converter;
import br.com.desafio.petz.api.util.ConstanteUtil;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;
import br.com.desafio.petz.api.web.response.Response;
import br.com.desafio.petz.api.web.response.ResponseApi;
import br.com.desafio.petz.api.web.response.ResponseApiPaged;

@RestController
@RequestMapping("/rest")
public class ClienteController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ClienteService service;
	
	@Autowired
	private Converter<Cliente, ClienteDto> converter;
	
	@GetMapping(path="/clientes",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response<ClienteDto>> listarClientes(){ 
		LOGGER.info("LISTAR_CLIENTES");
		Response<ClienteDto> response;
		
		List<Cliente>  clientes = null;
		response = new ResponseApi<ClienteDto>();
		try {
			
			clientes = service.findAll();
			if(clientes.isEmpty())
				return new ResponseEntity<Response<ClienteDto>>(response, HttpStatus.NO_CONTENT) ;		
			
			List<ClienteDto> dtos = converter.convertListToListDto(clientes); 
			
			response.setData(dtos);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + "ERRO_LISTAR_CLIENTE");
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + "ERRO_LISTAR_CLIENTE",e);
		}
		
		
		return new ResponseEntity<Response<ClienteDto>>(response, HttpStatus.OK) ;
	}
	
	
	@GetMapping(path="/clientes/pagedAndSorted",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClientesPaged(@PageableDefault(size = 3) Pageable page){ 
		LOGGER.info(" LISTAR_CLIENTES_PAGED ");
		ResponseApiPaged<Page<Cliente>> clienteResponse = new ResponseApiPaged<Page<Cliente>>();
		Page<Cliente>  clientes = null;
		try {
			clientes = service.findAll(page);
			clienteResponse.setData(clientes);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + "LISTAR_CLIENTES_PAGED");
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + "ERRO_LISTAR_CLIENTE",e);
		}
		
		if(!clienteResponse.getDataPaged().hasContent()) 
			return new ResponseEntity<>(clienteResponse, HttpStatus.NO_CONTENT) ;		
		
		return new ResponseEntity<>(clienteResponse, HttpStatus.OK) ;
	}
	
	@PostMapping(path="/clientes",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarCliente(@RequestBody @Valid ClienteDto dto){
		LOGGER.info("Criando CLIENTE "+ dto.getNome());
		Cliente cliente = null;
		ResponseApi<ClienteDto> clienteResponse = new ResponseApi<ClienteDto>();
		try {
			
			cliente = converter.converteDtoToEntity(dto);			
			cliente = service.salvar(cliente);
		
		
			List<ClienteDto> listDataTtoResponse = new ArrayList<ClienteDto>();
			listDataTtoResponse.add(converter.convertToDto(cliente));
			clienteResponse.setData(listDataTtoResponse);
		} catch (ResourceNotFoundException ex) {
			throw ex;
		} catch(Exception ex){
			LOGGER.error(ex.getMessage() +" Cliente: "+ dto.getNome());
			throw new InternalServerException(ex.getMessage(),ex);
		}
		return new ResponseEntity<>(clienteResponse, HttpStatus.CREATED) ;
	}


	/**
	 *   --Alterar um Cliente---
	 *  Esse método da API deve alterar alguma propriedade do Cliente.
	 * 
	 * @param IdCliente
	 * {@code Request:	{ "payment_date" : "2018-06-30" }	 }		
	 * 
	 * @param id
	 * @return 204 No Content.
	 */
	@PutMapping("/clientes/{id}")
	public ResponseEntity<Object> alterarCliente(@RequestBody @Valid DataDto dataPagamento, @PathVariable Long id) {
		LOGGER.info("UPDATE CLIENTE " + id );
		
		
		Optional<Cliente> clienteOptional = service.verificarSeClienteExiste(id);
        if(!clienteOptional.isPresent()) {
			throw new ResourceNotFoundException("Erro ao realizar update no Cliente "+id);
		}

		try {
			service.salvar(clienteOptional.get());
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e.toString());
			throw new InternalServerException(e.getMessage(), e);
		}
		ResponseApi<Cliente> ClienteResponse = new ResponseApi<Cliente>();
		ClienteResponse.setData(Arrays.asList(clienteOptional.get()));
		return ResponseEntity.noContent().build();
	}
	
	/**
	 *  
	 * @param id idCliente
	 * @return Cliente
	 */
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<Object> excluirCliente(@PathVariable Long id) {
		LOGGER.info("Cancelar pedido: " + id );
		
			
		Optional<Cliente> clienteOpt = service.verificarSeClienteExiste(id);
    	if(!clienteOpt.isPresent()) {
    		throw new ResourceNotFoundException("Error ao realizar delete do Cliente " + id);
		}

		try {			
			service.excluir(clienteOpt.get().getId());	
		} catch (Exception e) {
			LOGGER.error("INTERNAL_SERVER_ERROR ", e.toString());
			throw new InternalServerException(" ERRO_CANCELAR_CLIENTE " + id,e);
		}	
		return ResponseEntity.noContent().build();
	}
	
	
}
