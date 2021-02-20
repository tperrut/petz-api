package br.com.desafio.petz.api.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
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
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.service.conveter.Converter;
import br.com.desafio.petz.api.util.ConstanteUtil;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.response.Response;
import br.com.desafio.petz.api.web.response.ResponseApi;
import br.com.desafio.petz.api.web.response.ResponseApiPaged;

@RestController
@RequestMapping("/rest")
public class ClienteController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClienteService service;

	@Autowired
	private Converter<Cliente, ClienteDto> converter;

	@GetMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response<ClienteDto>> listarClientes() {
		logger.info("LISTAR_CLIENTES");
		Response<ClienteDto> response;

		List<Cliente> clientes = null;
		response = new ResponseApi<>();

			clientes = service.findAll();
			if (clientes.isEmpty())
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);

			List<ClienteDto> dtos = converter.convertListToListDto(clientes);

			response.setData(dtos);
		

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(path = "/clientes/pagedAndSorted", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClientesPaged(@PageableDefault(size = 3) Pageable page) {
		logger.info(String.format(" LISTAR_CLIENTES_PAGED "));
		ResponseApiPaged<Page<Cliente>> clienteResponse = new ResponseApiPaged<Page<Cliente>>();
		Page<Cliente> clientes = null;
		try {
			clientes = service.findAll(page);
			clienteResponse.setData(clientes);
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalServerException(ConstanteUtil.ERRO + e.getMessage() + "ERRO_LISTAR_CLIENTE", e);
		}

		if (!clienteResponse.getDataPaged().hasContent())
			return new ResponseEntity<>(clienteResponse, HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(clienteResponse, HttpStatus.OK);
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<Object> getClienteById(@PathVariable Long id) {
		logger.info(String.format("BUSCAR CLIENTE %d" , id));
		
		Optional<Cliente> cliente;
		ResponseApi<ClienteDto> clienteResponse = new ResponseApi<>();

		cliente = service.buscarPorId(id);
		if (cliente.isPresent()) {
			List<ClienteDto> asList = Arrays.asList(converter.convertToDto(cliente.get()));
			clienteResponse.setData(asList);
		}
		
		return new ResponseEntity<>(clienteResponse, HttpStatus.OK);
		
	}	
	
	@GetMapping("/clientes/nome/{nome}")
	public ResponseEntity<Object> getClienteByNome(@PathVariable String nome) {
		logger.info(String.format("BUSCAR CLIENTE  POR NOME %s" , nome));

		Optional<List<Cliente>> clientes;
		ResponseApi<ClienteDto> clienteResponse = new ResponseApi<>();

		clientes = service.buscarPorNome(nome);
		if (clientes.isPresent()) {
			List<ClienteDto> asList = Arrays.asList(converter.convertToDto(clientes.get().get(0)));
			clienteResponse.setData(asList);
		}
		
		return new ResponseEntity<>(clienteResponse, HttpStatus.OK);
		
	}	
	
	@PostMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarCliente(@RequestBody @Valid ClienteDto dto) {
		logger.info(String.format("Criando CLIENTE %s ", dto.getNome()));

		Optional<Cliente> clienteOpt = Optional.empty();;
		List<ClienteDto> listDataTtoResponse = new ArrayList<>();
		ResponseApi<ClienteDto> clienteResponse = new ResponseApi<>();

		clienteOpt = converter.converteDtoToEntity(dto);
		if(clienteOpt.isPresent()) {
			Cliente cliente = service.salvar(clienteOpt.get());
			listDataTtoResponse.add(converter.convertToDto(cliente));
			clienteResponse.setData(listDataTtoResponse);
		}

		return new ResponseEntity<>(clienteResponse, HttpStatus.CREATED);
	}

	/**
	 * --Alterar um Cliente--- 
	 * Esse método pode alterar alguma propriedade do
	 * Cliente.
	 * 
	 * @param IdCliente {@code }
	 * 
	 * @param id
	 * @return 204 No Content.
	 */
	@PutMapping("/clientes/{id}")
	
	public ResponseEntity<Object> alterarCliente(@RequestBody ClienteDto dto, @PathVariable Long id) {
		logger.info(String.format("UPDATE CLIENTE id: %d" , id));
		Optional<Cliente> clienteOpt;
		clienteOpt = service.buscarPorId(id);
		if (clienteOpt.isPresent()) {
			clienteOpt = converter.converteDtoToEntity(dto, clienteOpt.get());
			if (clienteOpt.isPresent()) 
				service.alterar(clienteOpt.get());
		}
		
		return ResponseEntity.noContent().build();
		
	}		

	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<Object> excluirCliente(@PathVariable Long id) {
		logger.info(String.format("Excluir cliente id: %d", id));

		Optional<Cliente> cliente = service.buscarPorId(id);
		if (cliente.isPresent()) {
			service.excluir(id);
		}
		
		return ResponseEntity.noContent().build();
	}

}
