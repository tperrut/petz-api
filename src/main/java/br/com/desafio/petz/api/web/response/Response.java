package br.com.desafio.petz.api.web.response;

import java.util.List;
import java.util.Set;

public interface Response<T> {
	/*
	 * 
	 *  TODO Criar metodos na interface q sejam semanticos 
		Ex.: montar resposta -> setData 
	 * 
	 * */
	Set<String> getErros();
	List<T> getData();
	void setData(List<T> data);
}
