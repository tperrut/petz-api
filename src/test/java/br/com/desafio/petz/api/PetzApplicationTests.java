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
	

	@Test
	public void calcularMultaClienteTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().minusDays(20)); 
		this.service.calcularMulta(Cliente);
		assertThat(Cliente.getMulta()).isNotNull();*/
	}
	
	@Test
	public void testIfClienteEmDiaMultaIsNullTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().plusDays(20)); 
		this.service.calcularMulta(Cliente);
		assertThat(Cliente.getMulta()).isNull();*/
	}
	
	@Test
	public void calcularMultaMaisDe10DiasClienteTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().minusDays(20)); 
		this.service.calcularMulta(Cliente);
		assertThat(Cliente.getMulta()).isEqualTo(10.0);
		assertThat(Cliente.getTotal().doubleValue()).isEqualTo(110.00);*/
	}
	
	@Test
	public void calcularMultaMenos10DiasClienteTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().minusDays(5)); 
		this.service.calcularMulta(Cliente);
		assertThat(Cliente.getMulta()).isEqualTo(5.0);
		assertThat(Cliente.getTotal().doubleValue()).isEqualTo(105.00);*/
	}
	
	
	@Test
	public void criarClienteTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().plusDays(12)); 
		this.repository.save(Cliente);
		assertThat(Cliente.getId()).isNotNull();
		assertThat(Cliente.getMulta()).isNull();
		assertThat(Cliente.getCliente()).isEqualTo(CLIENTE_TESTE);
		assertThat(Cliente.getStatus().id()).isEqualTo(EnumStatus.PENDING.id());*/
	}

	@Test
	public void validarClienteAtrasadoTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().minusDays(20));
		assertThat(Cliente.isAtrasado()).isTrue();*/
	}
	
	@Test
	public void validarClienteEmDiaTest() {
		/*Cliente Cliente = createCliente(LocalDate.now().plusDays(20));
		assertThat(Cliente.isAtrasado()).isFalse();*/
	}
	
	@Test
	public void isNotCalculableClienteTest() {
//		Cliente Cliente = createCliente(LocalDate.now());
//		assertThat(Cliente.isCalculable()).isFalse();
//		
//		
//		Cliente.setDataVencimento(LocalDate.now().minusDays(10));
//		assertThat(Cliente.isCalculable()).isTrue();
//		
//		Cliente.setStatus(EnumStatus.PAID);
//		assertThat(Cliente.isCalculable()).isFalse();
	}
	
	@Test
	public void isCalculableClienteTest() {
//		Cliente Cliente = createCliente(LocalDate.now().minusDays(12));
//		assertThat(Cliente.isCalculable()).isTrue();
	}
}
