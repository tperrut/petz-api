package br.com.desafio.petz.api.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.petz.api.dto.DataDto;
import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Pet;
import br.com.desafio.petz.api.service.PetService;
import br.com.desafio.petz.api.service.conveter.Converter;
import br.com.desafio.petz.api.util.ConstanteUtil;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;
import br.com.desafio.petz.api.web.response.Response;
import br.com.desafio.petz.api.web.response.ResponseApi;
import br.com.desafio.petz.api.web.response.ResponseApiPaged;


@RestController
@RequestMapping("/rest")
public class PetController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PetService service;
	
	@Autowired
	private Converter<Pet, PetDto> converter;
	
	@GetMapping(path="/pets",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseApi<PetDto>> listarPets(){ 
		LOGGER.info("LISTAR_PETS");
		Response<PetDto> response;
		
		List<Pet>  pets = null;
		try {
			response = new ResponseApi<PetDto>();
			pets = service.findAll();
			
			if(pets.isEmpty()) 
				return new ResponseEntity<ResponseApi<PetDto>>((ResponseApi<PetDto>) response, HttpStatus.NO_CONTENT) ;		

			List<PetDto> dtos = converter.convertListToListDto(pets); 
			response.setData(dtos);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + "ERRO_LISTAR_PET");
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + "ERRO_LISTAR_PET",e);
		}
		
		
		return  new ResponseEntity<ResponseApi<PetDto>>((ResponseApi<PetDto>) response, HttpStatus.OK) ;
	}
	
	
	@GetMapping(path="/pets/pagedAndSorted",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarPetsPaged(@PageableDefault(size = 3) Pageable page){ 
		LOGGER.info(" LISTAR_PETS_PAGED ");
		ResponseApiPaged<Page<Pet>> petResponse = new ResponseApiPaged<Page<Pet>>();
		Page<Pet>  pets = null;
		try {
			pets = service.findAll(page);
			petResponse.setData(pets);
		} catch (Exception e) {
			LOGGER.error(ConstanteUtil.INTERNAL_SERVER_ERROR + e.getMessage() + "LISTAR_PETS_PAGED");
			throw new InternalServerException(ConstanteUtil.ERRO+ e.getMessage() + "ERRO_LISTAR_PET",e);
		}
		
		if(!petResponse.getDataPaged().hasContent()) 
			return new ResponseEntity<>(petResponse, HttpStatus.NO_CONTENT) ;		
		
		return new ResponseEntity<>(petResponse, HttpStatus.OK) ;
	}
	
	@PostMapping(path="/pets",produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarPet(@RequestBody @Valid PetDto dto){
		LOGGER.info("Criando PET "+ dto.getNome());
		Pet pet = null;
		ResponseApi<PetDto> petResponse = new ResponseApi<PetDto>();
		try {
			
			pet = converter.converteDtoToEntity(dto);			
			pet = service.salvar(pet);
		} catch(Exception ex){
			LOGGER.error(ex.getMessage() +" Pet: "+ dto.getNome());
			throw new InternalServerException(ex.getMessage(),ex);
		}
		
		List<PetDto> listDataTtoResponse = new ArrayList<PetDto>();
		listDataTtoResponse.add(converter.convertToDto(pet));
		petResponse.setData(listDataTtoResponse);
		return new ResponseEntity<>(petResponse, HttpStatus.CREATED) ;
	}


	/**
	 *   --Pagar um Pet---
	 *  Esse método da API deve alterar o status do Pet para PAID.
	 * 
	 * @param dataPagamento
	 * {@code Request:	{ "payment_date" : "2018-06-30" }	 }		
	 * 
	 * @param id
	 * @return 204 No Content.
	 */
	@PutMapping("/pets/{id}")
	public ResponseEntity<Object> alterarPet(@RequestBody @Valid DataDto dataPagamento, @PathVariable Long id) {
		LOGGER.info("UPDATE PET " + id );
		verificarSePetExiste(id);
		
		Optional<Pet> PetOptional = service.buscarPorId(id);

//			throw new BusinessException(String.format(ConstanteUtil.ERRO, id));

		try {
			service.salvar(PetOptional.get());
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e.toString());
			throw new InternalServerException(e.getMessage(), e);
		}
		ResponseApi<Pet> PetResponse = new ResponseApi<Pet>();
		PetResponse.setData(Arrays.asList(PetOptional.get()));
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Regra para definir se é possivel CANCELAR um Pet:
	 * 
	 * NÂO pode ter os seguintes Status: PAID e CANCELED 
	 * deve ser apenas status PENDING
	 * 
	 * @param id resposta
	 * @return Pet
	 */
	@DeleteMapping("/pets/{id}")
	public ResponseEntity<Object> excluirPet(@PathVariable Long id) {
		LOGGER.info("Cancelar pedido: " + id );
		verificarSePetExiste(id);
			
		Optional<Pet> pet = service.buscarPorId(id);
		
		try {			
			service.excluir(pet.get().getId());	
		} catch (Exception e) {
			LOGGER.error("INTERNAL_SERVER_ERROR ", e.toString());
			throw new InternalServerException(" ERRO_CANCELAR_PET ",e);
		}	
		return ResponseEntity.noContent().build();
	}
	
	
	// TODO aqui??? 
	public void verificarSePetExiste(String pet) {
		Optional<List<Pet>> PetOptional = service.buscarPorNome(pet);

		if (!PetOptional.isPresent()) {
			LOGGER.info(" PET_NOT_FOUND_BY_PET " + pet);
			throw new ResourceNotFoundException(" PET_NOT_FOUND_BY_PET " + pet);
		}
	}



	public void verificarSePetExiste(Long id) {
		Optional<Pet> PetOptional = service.buscarPorId(id);

		if (!PetOptional.isPresent()) {
			LOGGER.info(" PET_NOT_FOUND " + id);
			throw new ResourceNotFoundException(" PET_NOT_FOUND " + id);
		}
	}
}
