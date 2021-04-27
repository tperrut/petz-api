package br.com.desafio.petz.api.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.desafio.petz.api.web.exception.EmailNotFoundException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ClienteServiceTest {

	@Autowired
	private ClienteService service;
		
    @Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Before
	public void contextLoads() {
	}
	
	
	
	@Test
	public void ClienteNotFoundTest() {
		thrown.expect(ResourceNotFoundException.class);
		this.service.verificarSeClienteExiste(1898L);
	}
	
	@Test
	public void shouldThrowException_WhenDeleteIdIsNotFound() {
		thrown.expect(ResourceNotFoundException.class);
		this.service.excluir(1898L);
	}
	
	@Test
	public void shouldThrowException_WhenUpdateIdIsNotFound() {
		thrown.expect(EmailNotFoundException.class);
		this.service.buscarPorEmail("xxxxx");
	}
	
	
	
	
	

}

