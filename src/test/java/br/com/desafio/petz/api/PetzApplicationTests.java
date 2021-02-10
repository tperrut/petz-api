package br.com.desafio.petz.api;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.service.ClienteServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetzApplicationTests {

	public final String CLIENTE_TESTE = "VALE";

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private ClienteServiceImpl service;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Test
	public void contextLoads() {
	}
	
	
	private Cliente createCliente(LocalDate dataVencimento) {
		return new Cliente(CLIENTE_TESTE, dataVencimento, "Emailxxxxxx");
	}
	

	
	
	
}
