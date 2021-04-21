package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.EmailNotFoundException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

public interface ClienteService {

	Cliente salvar(Cliente cliente) throws BusinessException;

	Cliente alterar(Cliente cliente) throws BusinessException;

	void excluir(Long id) throws ResourceNotFoundException, BusinessException;

	Optional<Cliente> buscarPorId(Long id) throws ResourceNotFoundException, BusinessException;

	Page<Cliente> findAll(Pageable pageable) throws BusinessException;

	Optional<List<Cliente>> buscarPorNome(String name) throws ResourceNotFoundException, BusinessException;

	List<Cliente> findAll() throws BusinessException ;

	Optional<Cliente> buscarPorEmail(String username) throws EmailNotFoundException;

	void verificarSeClienteExiste(Long l) throws ResourceNotFoundException;

}
