	package br.com.desafio.petz.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.util.PasswordUtils;
import br.com.desafio.petz.api.web.error.ErrorDetail;
import br.com.desafio.petz.api.web.error.ResourceNotFoundDetails;
import br.com.desafio.petz.api.web.error.ValidationErrorDetail;
import br.com.desafio.petz.api.web.response.ResponseApi;;


public class ClienteControllerTest extends AbstractControllerTest {
	
	public static final String CLIENTE= "CLIENTE Teste";
	public static final String CLIENTE2= "CLIENTE2 Teste";
	public static final String EMAIL_CLIENTE= "teste@gmail.com";
	public static final String EMAIL_CLIENTE2= "teste2@gmail.com";
	public static final String INVALID_EMAIL= "teste2xxxxxgmail.com";
	public static final String PATH = "/rest/clientes";
	public static final String PATH_ = "/rest/clientes/";
	public static final String PATH_NOME = "/rest/clientes/nome/";
	
	
//	@Autowired	private Flyway flyway;
	 
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ClienteRepository repository;
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
//		flyway.info();
	}
	
	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getClientesListEmpty() throws Exception {
	   repository.deleteAll();

	   String uri = ClienteControllerTest.PATH;
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
			   .get(uri)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andExpect(status().isNoContent())
			   .andReturn();
	   
	   ResponseApi<ClienteDto> response = convertStringToObject(mvcResult);
	   assertThat(response).isNotNull();
	   assertThat(response.getData()).isNull();
	}
	
	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getClientesList() throws Exception {
	   String uri = ClienteControllerTest.PATH;
	   repository.deleteAll();
	   createClienteByRepository(ClienteControllerTest.EMAIL_CLIENTE);
	   createClienteByRepository(ClienteControllerTest.EMAIL_CLIENTE2);

	   MvcResult result = mvc.perform(MockMvcRequestBuilders
			   .get(uri)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andExpect(status().isOk())
 		       .andDo(print()) 		       
			   .andReturn();
       /*.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())*/

	   ResponseApi<ClienteDto> response = convertStringToObject(result);
	   assertThat(response).isNotNull();
	   assertThat(response.getData()).isNotEmpty();
	   assertThat(response.getData()).size().isEqualTo(2);
	}
	
	@Test
	@WithMockUser(roles = {"USUARIO"})
	public void getClientesById() throws Exception {
	   repository.deleteAll();
	   Long id = createClienteByRepository(ClienteControllerTest.EMAIL_CLIENTE);
	   
	   String uriGet = ClienteControllerTest.PATH_ +id.toString();
	   MvcResult result = mvc.perform(MockMvcRequestBuilders
			   .get(uriGet)
			   .secure(false)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andExpect(status().isOk())
 		       .andDo(print()) 		       
			   .andReturn();
       /*.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())*/

	   ResponseApi<ClienteDto> response = convertStringToObject(result);
	   assertThat(response).isNotNull();
	   assertThat(response.getData()).isNotEmpty();
	   assertThat(response.getData()).size().isEqualTo(1);
	}
	
	@Test
	@WithMockUser(roles="USUARIO")
	public void updateClienteOK() throws Exception {
	   repository.deleteAll();

	   Long id = createClienteByRepository("teste1@teste.com");
	   
	   String uriPut = ClienteControllerTest.PATH_+id.toString();

	   MvcResult mvcPutResult = updateClienteViaPUTRequest(uriPut);
	   int status = mvcPutResult.getResponse().getStatus();	 

	   assertThat(status).isEqualTo(HttpStatus.NO_CONTENT.value()) ;
	   			  	
	}
	
//	@Test
//	public void updateClienteDeveLancarExceptionQuandoId() throws Exception {
//	   repository.deleteAll();
//
//	   Long id = createClienteByRepository("teste1@teste.com");
//	   
//	   String uriPut = ClienteControllerTest.PATH_+id.toString();
//
//	   MvcResult mvcPutResult = updateClienteViaPUTRequest(uriPut);
//	   int status = mvcPutResult.getResponse().getStatus();	 
//
//	   assertThat(status).isEqualTo(HttpStatus.NO_CONTENT.value()) ;
//	   			  	
//	}
	
	
	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void deleteCliente() throws Exception {
	   repository.deleteAll();

	   Long id = createClienteByRepository(ClienteControllerTest.EMAIL_CLIENTE);
	   String uriDelete = ClienteControllerTest.PATH_+id.toString();

	   mvc.perform(MockMvcRequestBuilders.delete(uriDelete)
			.contentType(MediaType.APPLICATION_JSON_VALUE))
	   		.andExpect(status().is2xxSuccessful())	  
	   		.andReturn();		   
	 
	}
	
	@Test
	public void createClienteOk() throws Exception {
	   String uri = ClienteControllerTest.PATH;
	   repository.deleteAll();

	   MvcResult mvcResult = createClienteViaPostRequest(uri);
	   ResponseApi<ClienteDto> response = convertStringToObject(mvcResult);
	   assertThat(response.getData()).isNotNull();
	   assertThat(response.getData()).size().isEqualTo(1);
	   
	}
	
	@Test
	public void createClienteWithInvalidEmail() throws Exception {
	   String uri = ClienteControllerTest.PATH;
	   repository.deleteAll();

	   MvcResult mvcResult = createClienteViaPostRequestInvalidEmail(uri);
	   assertThat(mvcResult.getResponse()).isNotNull();
	   ValidationErrorDetail ved = super.mapFromJson(mvcResult.getResponse().getContentAsString(), ValidationErrorDetail.class);
	   
	   assertThat(ved.getField()).isEqualTo("email");
	   assertThat(ved.getFieldMessages()).isEqualTo("Por favor digite um email válido!");
	   assertThat(ved.getStatusCode()).isEqualTo(422);
	   assertThat(ved.getDeveloperMessage()).isEqualTo("br.com.desafio.petz.api.web.error.ValidationErrorDetail");

	   
	}

	@Test
	public void createClienteWithEmptyEmail() throws Exception {
	   String uri = ClienteControllerTest.PATH;
	   repository.deleteAll();

	   MvcResult mvcResult = createClienteViaPostRequestEmptyEmail(uri);
	   assertThat(mvcResult.getResponse()).isNotNull();
	   ValidationErrorDetail ved = super.mapFromJson(mvcResult.getResponse().getContentAsString(), ValidationErrorDetail.class);
	   
	   assertThat(ved.getTitulo()).isEqualTo("Erro: Email já cadastrado ou vazio!");
	   assertThat(ved.getStatusCode()).isEqualTo(409);
	   assertThat(ved.getDeveloperMessage()).isEqualTo("br.com.desafio.petz.api.web.exception.BusinessException");
	   
	}

	
	
	
	/**
	 *  TODO Considerar testar esses erros numa classe d test separada
	 *  assim podemos testar a camada de serviço de forma isolada
	 *  
	*/
	
	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void testNameNotFoundException() throws JsonProcessingException, Exception {
		repository.deleteAll();

		String uriGet = ClienteControllerTest.PATH_NOME + ClienteControllerTest.CLIENTE;
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uriGet)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is4xxClientError()).andDo(print()).andReturn();
		/* .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists()) */
		assertThat(mvcResult.getResponse().getContentAsString()).contains("CLIENTE : CLIENTE Teste NOT_FOUND.");
	}
	
	@Test
	@WithMockUser(roles = {"USUARIO"})
	public void whenIdNotOkShouldThrowResourceNotFoundException() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/rest/clientes/45")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		ResourceNotFoundDetails rnfd = objectMapper.readValue(actualResponseBody, ResourceNotFoundDetails.class);	
		
		assertThat(rnfd.getTitulo()).isEqualTo("Not Found");
		assertThat(rnfd.getStatusCode()).isEqualTo(404);
		assertThat(rnfd.getDetalhe()).isEqualTo("CLIENTE ID 45");
		assertThat(rnfd.getDeveloperMessage()).isEqualTo("br.com.desafio.petz.api.web.exception.ResourceNotFoundException");

	}
	
	@Test
	@WithMockUser(roles = {"USUARIO"})
	public void whenParameterIsNotOk_ShouldThrowClientExceptionDetail() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/rest/clientes/xxx")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		ErrorDetail rnfd = objectMapper.readValue(actualResponseBody, ErrorDetail.class);	
		
		assertThat(rnfd.getDetalhe()).isEqualTo(
				"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"xxx\"");
		assertThat(rnfd.getTitulo()).isEqualTo("Bad Request");
		assertThat(rnfd.getStatusCode()).isEqualTo(400);
		assertThat(rnfd.getDeveloperMessage()).isEqualTo("br.com.desafio.petz.api.web.error.ClientExceptionDetail");

	}
	
//	@Test
//	@WithMockUser(roles = {"USUARIO"})
//	public void whenUrlIsNotOk_ShouldThrowClientExceptionDetail() throws Exception {
//		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/res/clientes/nome/")
//				.contentType("application/json")
//				.accept(MediaType.APPLICATION_JSON_VALUE))
//					.andReturn();
//		
//		String actualResponseBody = mvcResult.getResponse().getContentAsString();
//
//		ErrorDetail rnfd = objectMapper.readValue(actualResponseBody, ErrorDetail.class);	
//		
//		assertThat(rnfd.getDetalhe()).isEqualTo(
//				"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"xxx\"");
//		assertThat(rnfd.getTitulo()).isEqualTo("Bad Request");
//		assertThat(rnfd.getStatusCode()).isEqualTo(400);
//		assertThat(rnfd.getDeveloperMessage()).isEqualTo("br.com.desafio.petz.api.web.error.ClientExceptionDetail");
//
//	}
	
//	@Test
//	public void whenNullValue_thenReturns400AndErrorResult() throws Exception {
//		
//		
//		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/res/cliente")
//		          .contentType("application/json")
//		          .param("teste", "true")
//		          .andExpect(status().isBadRequest())
//		          .andReturn();
//		
//		ErrorResult expectedErrorResponse = new ErrorResult("name", "must not be null");
//		String actualResponseBody = mvcResult.getResponse().getContentAsString();
//		String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);
//		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
//	}
	
	/**
	 *
	 * 
	 * Refatorar ====================================>>>>>>>>>>>>>>>>>>>>>>>>
	 * TODO colocar esses metodos numa classeUtil

	
	 */
	private Long createClienteByRepository(String keyEmail) throws JsonProcessingException, Exception {
		Cliente entity = newCliente(keyEmail);
		entity = repository.save(entity);		
		return entity.getId();
	}
	

	private MvcResult createClienteViaPostRequestInvalidEmail(String uri) throws JsonProcessingException, Exception {
		ClienteDto dto = newClienteDtoToPostWithInvalidEmail();
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = postApiCliente(uri, inputJson);
		return mvcResult;
	}
	
	private MvcResult createClienteViaPostRequestEmptyEmail(String uri) throws JsonProcessingException, Exception {
		ClienteDto dto = newClienteDtoToPostWithEmptyEmail();
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = postApiCliente(uri, inputJson);
		return mvcResult;
	}
	
	private MvcResult createClienteViaPostRequest(String uri) throws JsonProcessingException, Exception {
		ClienteDto dto = newClienteDtoToPost();
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = postApiCliente(uri, inputJson);
		return mvcResult;
	}
	
	private MvcResult updateClienteViaPUTRequest(String uri) throws JsonProcessingException, Exception {
		ClienteDto dto = newClienteDtoToPut();
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
		cliente.setEmail(keyEmail);
		return cliente;
	}
	
	private ClienteDto newClienteDtoToPost() throws JsonProcessingException {
		ClienteDto dto = new ClienteDto(ClienteControllerTest.CLIENTE ,ClienteControllerTest.EMAIL_CLIENTE, LocalDate.now(), PerfilEnum.ROLE_ADMIN, "12345");
		return dto;
	}
	
	private ClienteDto newClienteDtoToPostWithInvalidEmail() throws JsonProcessingException {
		ClienteDto dto = new ClienteDto(ClienteControllerTest.CLIENTE ,ClienteControllerTest.INVALID_EMAIL, LocalDate.now(), PerfilEnum.ROLE_ADMIN, "12345");
		return dto;
	}
	
	private ClienteDto newClienteDtoToPostWithEmptyEmail() throws JsonProcessingException {
		ClienteDto dto = new ClienteDto(ClienteControllerTest.CLIENTE , null, LocalDate.now(), PerfilEnum.ROLE_ADMIN, "12345");
		return dto;
	}
	
	private ClienteDto newClienteDtoToPut() throws JsonProcessingException {
		ClienteDto dto = new ClienteDto(ClienteControllerTest.CLIENTE2 ,ClienteControllerTest.EMAIL_CLIENTE2, LocalDate.now(), PerfilEnum.ROLE_ADMIN, "12345");
		return dto;
	}
	
	private ResponseApi<ClienteDto> convertStringToObject(MvcResult result)
			throws IOException, JsonParseException, JsonMappingException, UnsupportedEncodingException {
		return objectMapper.readValue(result.getResponse().getContentAsString(), ResponseApi.class);
	}
	
	private String convertToJson(ClienteDto dto) throws JsonProcessingException {
		return super.mapToJson(dto);
	}
	
	private void setValidFields(Cliente cliente) {
		cliente.setNome(ClienteControllerTest.CLIENTE );
		cliente.setEmail(ClienteControllerTest.EMAIL_CLIENTE );
		cliente.setDataNascimento(LocalDate.now());
		cliente.setSenha(PasswordUtils.gerarBCrypt("123456"));		
		cliente.setPerfil(PerfilEnum.ROLE_ADMIN);		
	}

}
