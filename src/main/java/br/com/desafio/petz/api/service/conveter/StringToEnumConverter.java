package br.com.desafio.petz.api.service.conveter;

import org.springframework.core.convert.converter.Converter;

import br.com.desafio.petz.api.model.enuns.EnumTipo;

public class StringToEnumConverter implements Converter<String, EnumTipo> {

	@Override
	public EnumTipo convert(String source) {
        return EnumTipo.valueOf(source.toUpperCase());

	}

}
