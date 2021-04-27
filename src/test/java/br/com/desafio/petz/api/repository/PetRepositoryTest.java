package br.com.desafio.petz.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.dao.PetRepository;
import br.com.desafio.petz.api.enuns.EnumTipo;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.model.Pet;
import br.com.desafio.petz.api.util.PasswordUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PetRepositoryTest {
	
	private static final String PET_UPDATED = "PET_UPDATED";
	private static final String PET_UPDATED2 = "PET_UPDATED2";

	private static final String EMAIL_TESTE = "teste@gmail.com";
	private static final String EMAIL_TESTE2 = "teste2@gmail.com";
	private static final String EMAIL_INVALIDO = "teste2xxxgmail.com";
	
	private static final String RACA = "Pastor Alemão";
	private static final String RACA2 = "Pit Bull";

	private static final EnumTipo TIPO = EnumTipo.CACHORRO;

	private static final String CLIENTE_TESTE = "Cliente TESTE";
	private static final String CLIENTE_TESTE2 = "Cliente2 TESTE2";
	
	private static final String PET_TESTE = "Pet TESTE";
	private static final String PET_TESTE2 = "Pet2 TESTE2";


	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	
	@Test
	public void criarPetComSucesso() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		Pet resposta = petRepository.save(pet);
		assertThat(resposta).isNotNull();
	}
	
//	@Test
//	public void criarPetComIdDonoInavalido() {
//		thrown.expect(ConstraintViolationException.class);
//
//		Pet pet = createPet(LocalDate.now(),CLIENTE_TESTE, EMAIL_INVALIDO);
//		repository.save(pet);
//	}
	
	@Test
	public void buscarPetsComSucesso() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		
		Pet pet2 = createPet2();
		pet2.setDono(dono);
		
		petRepository.save(pet);
		petRepository.save(pet2);
		List<Pet> toodsPetss = petRepository.findAll();
		assertThat(toodsPetss).size().isEqualTo(2);
		
	}
	
	@Test
	public void buscarPetByIdComSucesso() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		
		pet = petRepository.save(pet);

		Long id = pet.getId();
		Optional<Pet> resposta = petRepository.findById(id);
		
		assertThat(resposta).isPresent();
		assertThat(resposta.get().getId()).isEqualTo(id);
		assertThat(resposta.get().getNome()).isEqualTo(PET_TESTE);
		assertThat(resposta.get().getDataNascimento()).isEqualTo(pet.getDataNascimento());

	}
	
	@Test
	public void buscarPetByNomeComSucesso() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		petRepository.save(pet);

		Optional<List<Pet>> resposta = petRepository.findByNome(pet.getNome());
		
		assertThat(resposta).isPresent();
		assertThat(resposta.get().isEmpty()).isFalse();
		assertThat(resposta.get().get(0).getNome()).isEqualTo(PET_TESTE);
	}
	
	@Test
	public void deletePetSucesso() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		pet = this.petRepository.save(pet);
		Long id = pet.getId();
		this.petRepository.delete(pet);
		Optional<Pet> retorno =  this.petRepository.findById(id);
		assertThat(retorno).isEmpty();
	}
	


	@Test
	public void alterarPetTest() {
		Pet pet = createPet();
		Cliente dono = clienteRepository.save(pet.getDono());
		pet.setDono(dono);
		Optional<Pet> resposta = Optional.of(new Pet());
		pet = petRepository.save(pet);

		resposta = petRepository.findById(pet.getId());
		resposta.get().setNome(PET_UPDATED);
		pet = petRepository.save(resposta.get());
		
		assertThat(pet).isNotNull();
		assertThat(pet.getNome()).isSameAs(PET_UPDATED);
	}
	
	private Cliente createCliente(LocalDate dataVencimento, String nome, String email) {
		Cliente cliente = new Cliente();
		setValidFields(cliente);
		return cliente; 
	}
	
	private void setValidFields(Cliente cliente) {
		cliente.setNome(PetRepositoryTest.CLIENTE_TESTE );
		cliente.setEmail(PetRepositoryTest.EMAIL_TESTE);
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
	}
	
	private Pet createPet() {
		Pet pet = new Pet(PET_TESTE, RACA,TIPO, createCliente(LocalDate.now(), CLIENTE_TESTE, EMAIL_TESTE), LocalDate.now());
		return pet;
	}
	
	
	private Pet createPet2() {
		Pet pet = new Pet(PET_TESTE2, RACA2,TIPO, createCliente(LocalDate.now(), CLIENTE_TESTE, EMAIL_TESTE), LocalDate.now());
		return pet;
	}
}









