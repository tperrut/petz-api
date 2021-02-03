package br.com.desafio.petz.api.controller;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.web.controller.ClienteController;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClienteEndPointTest {

	private static final String RESOURCE_NOT_FOUND = "Resource Not Found";

	private static final String ICN = "ICN";

	private static final String VALE = "VALE";

	public static final String CLIENTE_TESTE ="CLIENTE_TESTE";  
	
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
	
	private Cliente createCliente(LocalDate dataVencimento, String nome) {
		Cliente Cliente = new Cliente(nome, dataVencimento, "EmaiXXXXX");
		
		Cliente.setId(1L);
		return Cliente;
	}
	
	@Test
	public void listClientesTest() {
		List<Cliente> Clientes = Arrays.asList(createCliente(LocalDate.now(), VALE),createCliente(LocalDate.now(), ICN));
		BDDMockito.when(clienteRepository.findAll()).thenReturn(Clientes);
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/Clientes",String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().contains(ICN)).isTrue();
		assertThat(response.getBody().contains(VALE)).isTrue();
	}
	
	@Test
	public void findByClienteNotValid() {
		Cliente Cliente = createCliente(LocalDate.now(), CLIENTE_TESTE);
		BDDMockito.when(clienteRepository.save(Cliente)).thenReturn(Cliente);
		List list = new ArrayList();
		list.add(Cliente);
		Optional<List<Cliente>> ClienteOpt = Optional.of(list);
		BDDMockito.when(clienteRepository.findByNome(CLIENTE_TESTE)).thenReturn(ClienteOpt);
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/Clientes/cliente/client_no_exist",String.class);
		assertThat(response.getBody().contains(RESOURCE_NOT_FOUND)).isTrue();
		assertThat(response.getStatusCodeValue()).isEqualTo(404);

	}
	
	//@Test
	public void pagarClienteEmDiaTest() {
	}
	
	//@Test
	public void pagarClienteEmAtrasoTest() {
	}
	
	//@Test
	public void pagarClienteComMaisDe10DiasEmAtrasoTest() {
	}
	
	//@Test
	public void pagarClienteComMenosDe10DiasEmAtrasoTest() {
	}
	
	@Test
	public void ClienteNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.service.verificarSeClienteExiste(1898L);
	}
	
	@Test
	public void findByClientValidTest() {
		Cliente cliente = createCliente(LocalDate.now(), CLIENTE_TESTE);
		BDDMockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

		List list = new ArrayList();
		list.add(cliente);
		Optional<List<Cliente>> clienteOpt= Optional.of(list);
		BDDMockito.when(clienteRepository.findByNome(CLIENTE_TESTE)).thenReturn(clienteOpt);
		restTemplate = restTemplate.withBasicAuth("admin", "123");
		ResponseEntity<String> response = restTemplate.getForEntity("/rest/Clientes/cliente/"+CLIENTE_TESTE,String.class);

		assertThat(response.getBody().contains(CLIENTE_TESTE)).isTrue();
		assertThat(response.getBody().contains(ICN)).isFalse();
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
}
