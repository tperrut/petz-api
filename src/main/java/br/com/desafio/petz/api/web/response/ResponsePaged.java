package br.com.desafio.petz.api.web.response;

import java.util.Set;

import org.springframework.data.domain.Page;

public interface ResponsePaged<T> {
	Set<String> getErros();
	T getData();
	void setData(T data);
}
