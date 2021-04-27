package br.com.desafio.petz.api.web.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ResponseApiPaged<T> implements ResponsePaged<T> {
	
	
	private T dataPaged;
	private Set<String> erros = new HashSet<>();
	
	@Override
	public Set<String> getErros() {
		return erros;
	}
	
	@Override
	public T getData() {
		return this.dataPaged;
	}
	
	@Override
	public void setData(T data) {
		 this.dataPaged = data;	
	}
	
}
