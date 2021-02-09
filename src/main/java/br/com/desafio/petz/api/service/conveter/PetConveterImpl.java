package br.com.desafio.petz.api.service.conveter;

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
	private ClienteService service;
	
	/**
	 * Nesse converter o Dto usa o Padrão Builder
	 */
	@Override
	public List<PetDto> convertListToListDto(List<Pet> pets) {
		if(pets == null) 
			return new ArrayList<PetDto>(); 
		
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
		// TODO veificar se vamos pegar o dono no repository 
		
		return PetDto.builder().
				nome(entry.getNome()).
				raca(entry.getRaca()).
				dono(entry.getDono()).
				dataNascimento(entry.getDataNascimento()).
				build();
	}
	
	@Override
	public Optional<Pet> converteDtoToEntity(PetDto dto) {
		if(dto== null) return Optional.of(new Pet());
		
		Optional<Cliente> dono;
		dono = service.buscarPorId(Long.valueOf(dto.getIdDono()));
		
		if(!dono.isPresent()) 
			throw new ResourceNotFoundException("Id do dono não encontrado");
		
		Pet pet = new Pet();
		pet.setDataNascimento(dto.getDataNascimento());
		pet.setDono(dono.get());
		pet.setNome(dto.getNome());
		pet.setRaca(dto.getRaca());
		pet.setTipo(dto.getTipo());
		
		return  Optional.of(pet);
	
	}
	

	@Override
	public List<Pet> convertListDtoToListEntity(List<PetDto> dtos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Pet> converteDtoToEntity(PetDto dto, Pet cliente) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
