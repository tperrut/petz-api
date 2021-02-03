package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.model.Cliente;

public interface ClienteService {
	
	Cliente salvar(Cliente Cliente);
	
	void excluir(Long id);

	Optional<Cliente> buscarPorId(Long id);

	Page<Cliente> findAll(Pageable pageable);
	
	Optional<List<Cliente>> buscarPorNome(String name);

	List<Cliente> findAll();
	
	Optional<Cliente> verificarSeClienteExiste(Long id);
	
	void verificarSeClienteExiste(String nome);
	
}
