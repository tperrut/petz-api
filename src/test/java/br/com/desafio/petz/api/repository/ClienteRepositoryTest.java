package br.com.desafio.petz.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.aspectj.apache.bcel.Repository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.desafio.petz.api.controller.ClienteControllerTest;
import br.com.desafio.petz.api.controller.PetControllerTest;
import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.util.PasswordUtils;
import br.com.desafio.petz.api.web.exception.NameNotFoundException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteRepositoryTest {
	
	private static final String CLIENTE_UPDATED = "CLIENTE_UPDATED";
	private static final String CLIENTE_UPDATED2 = "CLIENTE_UPDATED2";

	private static final String EMAIL_TESTE = "teste@gmail.com";
	private static final String EMAIL_TESTE2 = "teste2@gmail.com";
	private static final String EMAIL_INVALIDO = "teste2xxxgmail.com";


	private static final String CAMPO_EMAIL_CLIENTE_JA_ESTA_CADASTRADO = "Email já cadastrado!";

	private static final String CLIENTE_TESTE = "Cliente TESTE";
	private static final String CLIENTE_TESTE2 = "Cliente2 TESTE2";


	@Autowired
	private ClienteRepository repository;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	
	private Cliente createClienteInvalidEmail() {
		Cliente cliente = new Cliente();
		cliente.setNome(ClienteRepositoryTest.CLIENTE_TESTE );
		cliente.setEmail("emailZuado.com.br");
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
		return cliente;
	}
	
	
	private Cliente createCliente1() {
		Cliente cliente = new Cliente();
		cliente.setNome(ClienteRepositoryTest.CLIENTE_TESTE );
		cliente.setEmail(ClienteRepositoryTest.EMAIL_TESTE);
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
		return cliente;
	}
	
	private Cliente createCliente2() {
		Cliente cliente = new Cliente();
		cliente.setNome(ClienteRepositoryTest.CLIENTE_TESTE2 );
		cliente.setEmail(ClienteRepositoryTest.EMAIL_TESTE2);
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
	
		return cliente;
	}
	
	@Test
	public void criarClienteComSucesso() {
		Cliente cliente = createCliente1();
		Cliente resposta = repository.save(cliente);
		assertThat(resposta).isNotNull();
	}
	
	@Test
	public void criarClienteComEmailInavalido() {
		thrown.expect(ConstraintViolationException.class);

		Cliente cliente = createClienteInvalidEmail();
		repository.save(cliente);
	}
	
	@Test
	public void buscarClientesComSucesso() {
		Cliente cliente = createCliente1();
		Cliente cliente2 = createCliente2();
		repository.save(cliente);
		repository.save(cliente2);
		List<Cliente> toodsClientess = repository.findAll();
		assertThat(toodsClientess).size().isEqualTo(2);
		
	}
	
	@Test
	public void buscarClienteByIdComSucesso() {
		Cliente cliente = createCliente1();
		cliente = repository.save(cliente);

		Long id = cliente.getId();
		Optional<Cliente> resposta = repository.findById(id);
		
		assertThat(resposta).isPresent();
		assertThat(resposta.get().getId()).isEqualTo(id);
		assertThat(resposta.get().getNome()).isEqualTo(CLIENTE_TESTE);
		assertThat(resposta.get().getDataNascimento()).isEqualTo(cliente.getDataNascimento());

	}
	
	@Test
	public void buscarClienteByNomeComSucesso() {
		Cliente cliente = createCliente1();
		repository.save(cliente);

		Optional<List<Cliente>> resposta = repository.findByNome(cliente.getNome());
		
		assertThat(resposta).isPresent();
		assertThat(resposta.get().isEmpty()).isFalse();
		assertThat(resposta.get().get(0).getNome()).isEqualTo(CLIENTE_TESTE);
	}
	
	@Test
	public void deleteClienteSucesso() {
		Cliente cliente = createCliente1();
		cliente = this.repository.save(cliente);
		Long id = cliente.getId();
		this.repository.delete(cliente);
		Optional<Cliente> retorno =  this.repository.findById(id);
		assertThat(retorno).isPresent();
	}
	

	@Test
	public void alterarClienteTest() {
		Cliente cliente = createCliente1();
		Optional<Cliente> resposta = Optional.of(new Cliente());
		cliente = repository.save(cliente);

		resposta = repository.findById(cliente.getId());
		resposta.get().setNome(CLIENTE_UPDATED);
		cliente = repository.save(resposta.get());
		
		assertThat(cliente).isNotNull();
		assertThat(cliente.getNome()).isSameAs(CLIENTE_UPDATED);
	}
	
	

}









