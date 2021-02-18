package br.com.desafio.petz.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.web.controller.ClienteController;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;
import br.com.desafio.petz.api.web.response.ResponseApi;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClienteEndPointTest {

	public static final String CLIENTE= "CLIENTE Teste";
	public static final String CLIENTE2= "CLIENTE2 Teste";
	public static final String EMAIL_CLIENTE= "EMAIL_CLIENTE Teste";
	public static final String EMAIL_CLIENTE2= "EMAIL_CLIENTE2 Teste";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteController controller;
	
	@Autowired
	private ClienteService service;
		
	@Autowired
	private ObjectMapper objectMapper;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	static class Config{
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthorization("admin", "123");
		}
	}
	
	@Before
	public void contextLoads() {
		System.out.println("Porta teste: "+port);
	}
	
	private Cliente createCliente(String email) {
		if(email == null)
			email = ClienteEndPointTest.EMAIL_CLIENTE;
		Cliente Cliente = new Cliente(ClienteEndPointTest.CLIENTE, LocalDate.now(), email);
		
		Cliente.setId(1L);
		return Cliente;
	}
	
	@Test
	public void listClientesTest() throws JsonParseException, JsonMappingException, IOException {
		List<Cliente> Clientes = Arrays.
				asList(createCliente(ClienteEndPointTest.EMAIL_CLIENTE),createCliente(ClienteEndPointTest.EMAIL_CLIENTE2));
		clienteRepository.deleteAll();

		BDDMockito.when(clienteRepository.findAll()).thenReturn(Clientes);
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/clientes",String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		ResponseApi<Cliente> result = objectMapper.readValue(response.getBody(), ResponseApi.class);
		assertThat(result.getData()).size().isEqualTo(2);
	}
	
	@Test
	public void findByClienteNotValid() {
		Cliente Cliente = createCliente(null);
		clienteRepository.deleteAll();
		BDDMockito.when(clienteRepository.save(Cliente)).thenReturn(Cliente);
		List list = new ArrayList();
		list.add(Cliente);
		Optional<List<Cliente>> ClienteOpt = Optional.of(list);
		BDDMockito.when(clienteRepository.findByNome( ClienteEndPointTest.EMAIL_CLIENTE)).thenReturn(ClienteOpt);
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/clientes/client_no_exist",String.class);
		assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY);

	}
	
	
	@Test
	public void ClienteNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.service.buscarPorId(1898L);
	}
	
	@Test
	public void findByNomeValidTest() {
		Cliente cliente = createCliente(ClienteEndPointTest.EMAIL_CLIENTE);
		clienteRepository.deleteAll();

		BDDMockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

		List<Cliente> list = new ArrayList<Cliente>();
		list.add(cliente);
		Optional<List<Cliente>> clienteOpt= Optional.of(list);
		
		BDDMockito.when(clienteRepository.findByNome(ClienteEndPointTest.CLIENTE)).thenReturn(clienteOpt);
		
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/clientes/nome/"+ClienteEndPointTest.CLIENTE,String.class);

		assertThat(response.getBody().contains(ClienteEndPointTest.CLIENTE)).isTrue();
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
}
