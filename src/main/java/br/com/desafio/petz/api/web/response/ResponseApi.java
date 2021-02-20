package br.com.desafio.petz.api.web.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseApi<T> implements Response<T>{
	
	private List<T> data;
	private Set<String> erros = new HashSet<String>();
	
	@Override
	public Set<String> getErros() {
		return this.erros;
	}
	
	@Override
	public List<T> getData() {
		return this.data;
	}
		
	@Override
	public void setData(List<T> data) {
		this.data = data;
	}
	
}
