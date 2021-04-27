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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.petz.api.config.SpringSecurityWebAuxTestConfig;
import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.security.util.JwtTokenUtil;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.util.PasswordUtils;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;
import br.com.desafio.petz.api.web.response.ResponseApi;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
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
	private ClienteService service;
		
	@Autowired
	private JwtTokenUtil jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Before
	public void contextLoads() {
		System.out.println("Porta teste: "+port);
	}
	
	private Cliente createCliente(String email) {
		if(email == null)
			email = ClienteEndPointTest.EMAIL_CLIENTE;
		Cliente Cliente = new Cliente();
		setValidFields(Cliente);
		
		Cliente.setId(1L);
		return Cliente;
	}
	
	private void setValidFields(Cliente cliente) {
		cliente.setNome(ClienteControllerTest.CLIENTE );
		cliente.setEmail(ClienteControllerTest.EMAIL_CLIENTE );
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
	}
	
	
	
	
	@Test
	public void findByClienteNotValid() {
		Cliente Cliente = createCliente(null);
		clienteRepository.deleteAll();
		BDDMockito.when(clienteRepository.save(Cliente)).thenReturn(Cliente);
		List<Cliente> list = new ArrayList<Cliente>();
		list.add(Cliente);
		Optional<List<Cliente>> ClienteOpt = Optional.of(list);
		BDDMockito.when(clienteRepository.findByNome( ClienteEndPointTest.EMAIL_CLIENTE)).thenReturn(ClienteOpt);
		
		HttpHeaders headers = securityConfig();

		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				"/rest/clientes/nome/{vacilando}",
				HttpMethod.GET,
				entity,
				String.class,
				"vacilando");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).contains("CLIENTE : vacilando NOT_FOUND.");

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
		
		HttpHeaders headers = securityConfig();
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				"/rest/clientes/nome/{param}",
				HttpMethod.GET,
				entity,
				String.class,
				ClienteEndPointTest.CLIENTE);

		assertThat(response.getBody()).contains(ClienteEndPointTest.CLIENTE);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	private HttpHeaders securityConfig() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String accessToken =  jwtService.obterToken(this.userDetailsService.loadUserByUsername("admin@petzapi.com"));
		headers.set("Authorization", "Bearer "+ accessToken);
		return headers;
	}
	

}

