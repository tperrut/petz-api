package br.com.desafio.petz.api.web.response;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseApiPaged<T> implements ResponsePaged<T> {
	
	
	private T dataPaged;
	private Set<String> erros = new HashSet<String>();
	
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
