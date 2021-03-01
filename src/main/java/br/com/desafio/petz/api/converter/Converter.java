package br.com.desafio.petz.api.converter;

import java.util.List;


public interface Converter<T, E> {
	
		
		T converteDtoToEntity(E dto, T entity);
		
		T converteDtoToEntity(E dto);
		
		E convertToDto(T entry);

		List<E> convertListToListDto(List<T> lista);

}
