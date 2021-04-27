package br.com.desafio.petz.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.dao.PetRepository;
import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.enuns.EnumTipo;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.model.Pet;
import br.com.desafio.petz.api.util.PasswordUtils;
import br.com.desafio.petz.api.web.response.ResponseApi;;

public class PetControllerTest extends AbstractControllerTest {
	
	public static final String CLIENTE= "Cliente Teste";
	
	public static final String EMAIL_CLIENTE= "teste@gmail.com";
	public static final String EMAIL_CLIENTE2= "teste2@gmail.com";
	public static final String PATH = "/rest/pets";
	public static final String PATH_ = "/rest/pets/";
	public static final String PATH_NOME = "/rest/pets/nome/";

	private static final String RACA = "Pastor Alemão";
	private static final String RACA2 = "Pit Bull";

	private static final EnumTipo TIPO = EnumTipo.CACHORRO;

	private static final String PET = "PET teste";
	private static final String PET2 = "PET2 teste";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PetRepository petRepository;
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getClientesListEmpty() throws Exception {
		clienteRepository.deleteAll();
		petRepository.deleteAll();
	
	   String uri = PetControllerTest.PATH;
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
			   .get(uri)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andExpect(status().isNoContent())
			   .andReturn();
	   
	   ResponseApi<PetDto> response = convertStringToObject(mvcResult);
	   assertThat(response).isNotNull();
	   assertThat(response.getData()).isNull();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getClientesList() throws Exception {
	    String uri = PetControllerTest.PATH;
		clienteRepository.deleteAll();
		petRepository.deleteAll();
		Cliente cliente = createClienteByRepository(PetControllerTest.EMAIL_CLIENTE);

		Pet pet = newPet(PetControllerTest.PET, PetControllerTest.RACA, PetControllerTest.TIPO, cliente);
		pet = petRepository.save(pet);
		Pet pet2 = newPet(PetControllerTest.PET2, PetControllerTest.RACA2, PetControllerTest.TIPO, cliente);
		pet2 = petRepository.save(pet2);

		

	   MvcResult result = mvc.perform(MockMvcRequestBuilders
			   .get(uri)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andExpect(status().isOk())
			   .andReturn();
       /*.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())*/

	   ResponseApi<PetDto> response = convertStringToObject(result);
	   assertThat(response).isNotNull();
	   assertThat(response.getData()).isNotEmpty();
	   assertThat(response.getData()).size().isEqualTo(2);
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void getPetById() throws Exception {
		clienteRepository.deleteAll();
		petRepository.deleteAll();
		Cliente cliente = createClienteByRepository(PetControllerTest.EMAIL_CLIENTE);
		Pet pet = newPet(PetControllerTest.PET, PetControllerTest.RACA, PetControllerTest.TIPO, cliente);
		pet = petRepository.save(pet);

		String uriGet = PetControllerTest.PATH_ + pet.getId().toString();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uriGet).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		/* .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists()) */

		ResponseApi<PetDto> response = convertStringToObject(result);
		assertThat(response).isNotNull();
		assertThat(response.getData()).isNotEmpty();
		assertThat(response.getData()).size().isEqualTo(1);
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void updatePetAllFileds() throws Exception {
		clienteRepository.deleteAll();
		petRepository.deleteAll();
		Cliente cliente = createClienteByRepository(PetControllerTest.EMAIL_CLIENTE);
		Pet pet = newPet(PetControllerTest.PET, PetControllerTest.RACA, PetControllerTest.TIPO, cliente);
		pet = petRepository.save(pet);
		String uriPut = PetControllerTest.PATH_ + pet.getId().toString();

		MvcResult mvcPutResult = updatePetAllFieldsByPUTRequest(uriPut, cliente);
		int status = mvcPutResult.getResponse().getStatus();
		
		assertThat(status).isEqualTo(HttpStatus.NO_CONTENT.value());
		
		Optional<Pet> optPet =petRepository.findById(pet.getId());
		assertThat(optPet).isPresent();
		assertThat(optPet.get().getNome()).isEqualTo(PetControllerTest.PET2);
		assertThat(optPet.get().getRaca()).isEqualTo(PetControllerTest.RACA2);

	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void updatePetPartialFields() throws Exception {
		clienteRepository.deleteAll();
		petRepository.deleteAll();
		Cliente cliente = createClienteByRepository(PetControllerTest.EMAIL_CLIENTE);
		Pet pet = newPet(PetControllerTest.PET, PetControllerTest.RACA, PetControllerTest.TIPO, cliente);
		pet = petRepository.save(pet);
		String uriPut = PetControllerTest.PATH_ + pet.getId().toString();

		MvcResult mvcPutResult = updatePetPartialFieldsByPUTRequest(uriPut, cliente);
		
		int status = mvcPutResult.getResponse().getStatus();
		
		assertThat(status).isEqualTo(HttpStatus.NO_CONTENT.value());
		
		Optional<Pet> optPet =petRepository.findById(pet.getId());
		assertThat(optPet).isPresent();
		assertThat(optPet.get().getNome()).isEqualTo(PetControllerTest.PET2);
		assertThat(optPet.get().getRaca()).isEqualTo(PetControllerTest.RACA2);

	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void deletePet() throws Exception {
	   clienteRepository.deleteAll();
	   petRepository.deleteAll();
	   Cliente cliente =createClienteByRepository(PetControllerTest.EMAIL_CLIENTE);
	   Pet pet = newPet(PetControllerTest.PET, PetControllerTest.RACA, PetControllerTest.TIPO, cliente);
	   pet = petRepository.save(pet); 
	   String uriDelete = PetControllerTest.PATH_+pet.getId().toString();

	   mvc.perform(MockMvcRequestBuilders.delete(uriDelete)
			.contentType(MediaType.APPLICATION_JSON_VALUE))
	   		.andExpect(status().is2xxSuccessful())	  
	   		.andReturn();		   
	 
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void createPet() throws Exception {
	   String uri = PetControllerTest.PATH;
	   clienteRepository.deleteAll();
	   petRepository.deleteAll();
   
	   MvcResult mvcResult = createPetViaPostRequest(uri);
	   ResponseApi<PetDto> response = convertStringToObject(mvcResult);
	   assertThat(response.getData()).isNotNull();
	   assertThat(response.getData()).size().isEqualTo(1);
	   assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value()); 
	}
	
	/** 
	 * Refatorar ====================================>>>>>>>>>>>>>>>>>>>>>>>>
	 * TODO colocar esses metodos numa classeUtil
	 */
	private Cliente createClienteByRepository(String keyEmail) throws JsonProcessingException, Exception {
		Cliente entity = newCliente(keyEmail);
		entity = clienteRepository.save(entity);		
		return entity;
	}
	
	private MvcResult createPetViaPostRequest(String uri) throws JsonProcessingException, Exception {
		PetDto dto = newPetDtoToPost(newCliente(PetControllerTest.EMAIL_CLIENTE));
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = postApiCliente(uri, inputJson);
		return mvcResult;
	}
	
	private MvcResult updatePetAllFieldsByPUTRequest(String uri, Cliente cliente) throws JsonProcessingException, Exception {
		PetDto dto = newPetDtoToPutAllFields(cliente);
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = putApiCliente(uri, inputJson);
		return mvcResult;
	}

	private MvcResult updatePetPartialFieldsByPUTRequest(String uri, Cliente cliente) throws JsonProcessingException, Exception {
		PetDto dto = newPetDtoToPutPartialFields(cliente);
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = putApiCliente(uri, inputJson);
		return mvcResult;
	}
	
	
	
	private MvcResult putApiCliente(String uri, String inputJson) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
		      .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		return mvcResult;
	}

	private MvcResult postApiCliente(String uri, String inputJson) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		      .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		return mvcResult;
	}
	
	private Cliente newCliente(String keyEmail) throws JsonProcessingException {
		Cliente cliente = new Cliente();
		setValidFields(cliente);
		return clienteRepository.save(cliente);
	}
	
	private void setValidFields(Cliente cliente) {
		cliente.setNome(PetControllerTest.CLIENTE );
		cliente.setEmail(PetControllerTest.EMAIL_CLIENTE );
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
	}
	
	private Pet newPet(String nome, String raca, EnumTipo tipo, Cliente dono) {
		return new Pet(nome, raca, tipo,dono, LocalDate.now());
	}
	
	
	private PetDto newPetDtoToPost(Cliente dono) throws JsonProcessingException {
		
		PetDto dto = PetDto.builder().
				idDono(dono.getId().toString()).
				nome(PetControllerTest.PET).
				dataNascimento(LocalDate.now()).
				dono(dono).
				raca(PetControllerTest.RACA).
				build();
				
		return dto;
	}
	
	private PetDto newPetDtoToPutAllFields(Cliente dono) throws JsonProcessingException {
		PetDto dto = PetDto.builder().
				idDono(dono.getId().toString()).
				nome(PetControllerTest.PET2).
				dataNascimento(LocalDate.now()).
				dono(dono).
				tipo(PetControllerTest.TIPO).
				raca(PetControllerTest.RACA2).
				build();
				
		return dto;
	}
	
	private PetDto newPetDtoToPutPartialFields(Cliente dono) throws JsonProcessingException {
		PetDto dto = PetDto.builder().
				idDono(dono.getId().toString()).
				nome(PetControllerTest.PET2).
				raca(PetControllerTest.RACA2).
				build();
				
		return dto;
	}
	
	
	private ResponseApi<PetDto> convertStringToObject(MvcResult result)
			throws IOException, JsonParseException, JsonMappingException, UnsupportedEncodingException {
		return objectMapper.readValue(result.getResponse().getContentAsString(), ResponseApi.class);
	}
	
	private String convertToJson(PetDto dto) throws JsonProcessingException {
		return super.mapToJson(dto);
	}
	
}
