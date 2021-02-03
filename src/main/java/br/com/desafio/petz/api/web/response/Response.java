package br.com.desafio.petz.api.web.response;

import java.util.List;
import java.util.Set;

public interface Response<T> {
	Set<String> getErros();
	List<T> getData();
	void setData(List<T> data);
}
