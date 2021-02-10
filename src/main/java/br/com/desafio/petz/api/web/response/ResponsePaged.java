package br.com.desafio.petz.api.web.response;

import java.util.Set;

public interface ResponsePaged<T> {
	Set<String> getErros();
	T getData();
	void setData(T data);
}
