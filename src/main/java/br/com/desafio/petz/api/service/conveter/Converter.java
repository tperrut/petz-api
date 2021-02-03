package br.com.desafio.petz.api.service.conveter;

import java.util.List;


public interface Converter<T, E> {
	
		List<E> convertListToListDto(List<T> lista);
		E convertToDto(T entry);
		T converteDtoToEntity(E dto);
		List<T> convertListDtoToListEntity(List<E> dtos);
	
}
