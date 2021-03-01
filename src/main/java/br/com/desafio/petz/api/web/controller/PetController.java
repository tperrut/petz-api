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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.petz.api.converter.Converter;
import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Pet;
import br.com.desafio.petz.api.service.PetService;
import br.com.desafio.petz.api.util.ConstanteUtil;
import br.com.desafio.petz.api.web.exception.InternalServerException;
import br.com.desafio.petz.api.web.response.Response;
import br.com.desafio.petz.api.web.response.ResponseApi;
import br.com.desafio.petz.api.web.response.ResponseApiPaged;

@RestController
@RequestMapping("/rest")
public class PetController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PetService service;

	@Autowired
	private Converter<Pet, PetDto> converter;

	@GetMapping(path = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<Response<PetDto>> listarPets() {
		logger.info("LISTAR_PETS");
		Response<PetDto> response = new ResponseApi<>();

		List<Pet> pets = null;
		pets = service.findAll();

		if (pets.isEmpty())
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);

		List<PetDto> dtos = converter.convertListToListDto(pets);
		response.setData(dtos);
	

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/pets/{id}")
	@PreAuthorize("hasRole('USUARIO')")
	public ResponseEntity<Object> getPetById(@PathVariable Long id) {
		logger.info("BUSCAR PET POR ID: {}", id);

		Optional<Pet> pet;
		ResponseApi<PetDto> petResponse = new ResponseApi<>();

		pet = service.buscarPorId(id);
		if (pet.isPresent()) {
			List<PetDto> asList = Arrays.asList(converter.convertToDto(pet.get()));
			petResponse.setData(asList);
		}
		
		return new ResponseEntity<>(petResponse, HttpStatus.OK);
		
	}	
	
	@PreAuthorize("hasRole('USUARIO')")
	@GetMapping(path = "/pets/pagedAndSorted", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarPetsPaged(@PageableDefault(size = 3) Pageable page) {
		logger.info("LISTAR_PETS_PAGED ");
		ResponseApiPaged<Page<Pet>> petResponse = new ResponseApiPaged<>();
		Page<Pet> pets = null;
	
		pets = service.findAll(page);
		petResponse.setData(pets);

		if (!petResponse.getDataPaged().hasContent())
			return new ResponseEntity<>(petResponse, HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(petResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('USUARIO')")
	@PostMapping(path = "/pets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarPet(@RequestBody @Valid PetDto dto) {
		logger.info("Criando PET : {}", dto.getNome());

		ResponseApi<PetDto> petResponse = new ResponseApi<>();
		List<PetDto> listDataTtoResponse = new ArrayList<>();
		
		Pet pet = converter.converteDtoToEntity(dto);
		pet = service.salvar(pet);
		
		listDataTtoResponse.add(converter.convertToDto(pet));
		petResponse.setData(listDataTtoResponse);

		return new ResponseEntity<>(petResponse, HttpStatus.CREATED);
	}

	/**
	 * @return 204 No Content.
	 */
	@PutMapping("/pets/{id}")
	@PreAuthorize("hasRole('USUARIO')")
	public ResponseEntity<Object> alterarPet(@RequestBody PetDto dto, @PathVariable Long id) {
		logger.info("UPDATE PET {}", id);

		Optional<Pet> petOpt = service.buscarPorId(id);
		Pet pet = null;
		if (petOpt.isPresent()) {
			pet = converter.converteDtoToEntity(dto, petOpt.get());
			pet.setId(id);
			service.salvar(pet);
		}

		return ResponseEntity.noContent().build();

	}

	/**
	 * @param id IdPet
	 * @return Pet
	 */
	@DeleteMapping("/pets/{id}")
	@PreAuthorize("hasRole('USUARIO')")
	public ResponseEntity<Void> excluirPet(@PathVariable Long id) {
		logger.info("Excluir Pet id: {}", id);

		Optional<Pet> pet = service.buscarPorId(id);
		if (pet.isPresent()) 
			service.excluir(id);
		
		return ResponseEntity.noContent().build();
	}
}
