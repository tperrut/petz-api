package br.com.desafio.petz.api.service.conveter;

import java.util.List;
import java.util.Optional;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.model.Cliente;


public interface Converter<T, E> {
	
		List<E> convertListToListDto(List<T> lista);
		
		E convertToDto(T entry);
		
		Optional<T> converteDtoToEntity(E dto, T entity);
		
		List<T> convertListDtoToListEntity(List<E> dtos);

		Optional<T> converteDtoToEntity(E dto);
	
}
