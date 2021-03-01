package br.com.desafio.petz.api.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.model.Pet;
import br.com.desafio.petz.api.service.ClienteService;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

@Service 
public class PetConveterImpl implements Converter<Pet, PetDto> {
	
	
	@Autowired
	private ClienteService clienteService;
	
	
	/**
	 * Nesse converter o Dto usa o Padrão Builder
	 */
	@Override
	public List<PetDto> convertListToListDto(List<Pet> pets) {
		if(pets == null) 
			return new ArrayList<>(); 
		
		List<PetDto>  lista = null;
		lista = pets.stream().map(pet -> {
			return PetDto.builder().nome(pet.getNome()).
					raca(pet.getRaca()).
					dataNascimento(pet.getDataNascimento()).
					build();
					
		}).collect(Collectors.toList());
					
		return lista;
	}
	
	@Override
	public PetDto convertToDto(Pet entry) {
		return PetDto.builder().
				nome(entry.getNome()).
				raca(entry.getRaca()).
				dono(entry.getDono()).
				dataNascimento(entry.getDataNascimento()).
				build();
	}
	
	@Override
	public Pet converteDtoToEntity(PetDto dto, Pet pet) {
		if(dto== null) return new Pet();
		
		Optional<Cliente> dono;
		dono = clienteService.buscarPorId(Long.valueOf(dto.getIdDono()));
		
		if(!dono.isPresent()) 
			throw new ResourceNotFoundException("Id do dono não encontrado");
		
		setValidFields(dto, pet);
		return  pet;
	}

	
	@Override
	public Pet converteDtoToEntity(PetDto dto) {
		Pet pet = new Pet();
		if(dto== null) return pet;
		
		Optional<Cliente> dono;
		dono = clienteService.buscarPorId(Long.valueOf(dto.getIdDono()));
		
		if(!dono.isPresent()) 
			throw new ResourceNotFoundException("Id do dono não encontrado");
		
		setValidFields(dto, pet);
		return  pet;
	
	}
	
	private void setValidFields(PetDto dto, Pet pet) {
		if(dto.getNome() != null) pet.setNome(dto.getNome());
		if(dto.getRaca() != null) pet.setRaca(dto.getRaca());
		if(dto.getTipo() != null) pet.setTipo(dto.getTipo());
		if(dto.getDataNascimento() != null) pet.setDataNascimento(dto.getDataNascimento());
	}

}
