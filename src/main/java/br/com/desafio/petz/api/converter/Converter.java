package br.com.desafio.petz.api.converter;

import java.util.List;


public interface Converter<T, E> {
	
		
		T convertDtoToEntity(E dto, T entity);
		
		T convertDtoToEntity(E dto);
		
		E convertToDto(T entry);

		List<E> convertListToListDto(List<T> list);

}
