package br.com.desafio.petz.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteRepositoryTest {
	
	private static final String TESTE_PAGO = "TESTE_PAGO";

	//private static final String CLIENTE_NAO_PODE_SER_VAZIO = "Cliente não pode ser vazio";

	private static final String CLIENTE_TESTE = "TESTE";

	@Autowired
	private ClienteRepository repository;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	private Cliente createCliente(LocalDate dataVencimento, String nome) {
		Cliente Cliente = new Cliente(nome, dataVencimento, "Emailxxxxx");
		return Cliente;
	}
	
	@Test
	public void criarClienteTest() {
		Cliente Cliente = createCliente(LocalDate.now().plusDays(12),CLIENTE_TESTE);
		
		Cliente resposta = repository.save(Cliente);
		assertThat(resposta).isNotNull();
	}
	
	/*@Test
	public void criarComClienteIsNullThrowConstraintViolationException() {
		//thrown.expect(ConstraintViolationException.class);
		//thrown.expectMessage(CLIENTE_NAO_PODE_SER_VAZIO);
		repository.save(new Cliente());
	}*/
	
	@Test
	public void pagarClienteTest() {
		
	}
	
	@Test
	public void deleteClienteTest() {
		Cliente Cliente = createCliente(LocalDate.now().plusDays(12),CLIENTE_TESTE);
		Cliente = this.repository.save(Cliente);
		Long id = Cliente.getId();
		this.repository.delete(Cliente);
		Optional<Cliente> retorno =  this.repository.findById(id);
		assertThat(retorno.isPresent()).isFalse();
	}
	
	
	
	

}









