package br.com.desafio.petz.api.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.desafio.petz.api.dto.ClienteDto;;

public class ClienteControllerTest extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getClientesListEmpty() throws Exception {
	   String uri = "/rest/clientes";
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE))
		  .andReturn();
	   
	   int status = mvcResult.getResponse().getStatus();
	   assertEquals(200, status);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getClientesList() throws Exception {
	   String uri = "/rest/clientes";
	   MvcResult mvcPOSTResult1 = createClienteViaPostRequest(uri);
	   MvcResult mvcPOSTResult2 = createClienteViaPostRequest(uri);

	   MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders
			   .get(uri)
			   .accept(MediaType.APPLICATION_JSON_VALUE))
			   .andDo(print())
			   .andExpect(status().isOk())
 		       .andExpect(MockMvcResultMatchers.jsonPath(uri).exists())
			   .andReturn();
	   
//	   int status = mvcGETResult.getResponse().getStatus();
//	   
//	   assertEquals(200, status);
	}
	
//	@Test
//	public void getAllEmployeesAPI() throws Exception 
//	{
//	  mvc.perform( MockMvcRequestBuilders
//	      .get("/employees")
//	      .accept(MediaType.APPLICATION_JSON))
//	      .andDo(print())
//	      .andExpect(status().isOk())
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());
//	}
//	 
//	@Test
//	public void getEmployeeByIdAPI() throws Exception 
//	{
//	  mvc.perform( MockMvcRequestBuilders
//	      .get("/employees/{id}", 1)
//	      .accept(MediaType.APPLICATION_JSON))
//	      .andDo(print())
//	      .andExpect(status().isOk())
//	      .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId").value(1));
//	}
	
	
	@Test
	public void createCliente() throws Exception {
	   String uri = "/rest/clientes";
	   
	   MvcResult mvcResult = createClienteViaPostRequest(uri);
	   
	   int status = mvcResult.getResponse().getStatus();
	   assertEquals(201, status);
	   //String content = mvcResult.getResponse().getContentAsString();	 
	   //assertEquals(content, "Cliente is created successfully");
	}
	
	private MvcResult createClienteViaPostRequest(String uri) throws JsonProcessingException, Exception {
		ClienteDto dto = createNewClienteDto();
		String inputJson = convertToJson(dto);
		MvcResult mvcResult = postApiCliente(uri, inputJson);
		return mvcResult;
	}

	private MvcResult postApiCliente(String uri, String inputJson) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		      .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		return mvcResult;
	}

	private ClienteDto createNewClienteDto() throws JsonProcessingException {
		ClienteDto dto = new ClienteDto("CLIENTE Teste" ,"Emailxxxxxxxx"+Math.random(), LocalDate.now() );
		return dto;
	}

	private String convertToJson(ClienteDto dto) throws JsonProcessingException {
		String inputJson = super.mapToJson(dto);
		return inputJson;
	}
	
}
