package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.web.exception.BusinessException;
import br.com.desafio.petz.api.web.exception.EmailNotFoundException;
import br.com.desafio.petz.api.web.exception.NameNotFoundException;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository dao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Cliente salvar(Cliente cliente) throws BusinessException {
			return dao.save(cliente);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CachePut("buscarClientePorId")
	public Cliente alterar(Cliente cliente) throws ResourceNotFoundException, BusinessException {
			return dao.save(cliente);
	}


	@Override
	public void excluir(Long id) throws ResourceNotFoundException, BusinessException {
		try {
			verificarSeClienteExiste(id);
			dao.deleteById(id);
		}catch (ResourceNotFoundException e) {
			throw e;
		}catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> excluir", e);
		}
		
	}
	
	@Override
	public void verificarSeClienteExiste(Long id) throws ResourceNotFoundException {
		dao.findById(id).orElseThrow(() -> new ResourceNotFoundException("CLIENTE ID " + id));
	}

	@Override
	@Cacheable("buscarClientePorId")
	@Transactional(readOnly = true)
	public Optional<Cliente> buscarPorId(Long id) throws ResourceNotFoundException, BusinessException {
			verificarSeClienteExiste(id);
			return dao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<List<Cliente>> buscarPorNome(String nome) throws ResourceNotFoundException, BusinessException {
		
		try {
			Optional<List<Cliente>> cliente = dao.findByNome(nome);
			if (!cliente.isPresent()) {
				throw new NameNotFoundException(" CLIENTE : " + nome+ " NOT_FOUND.");
			}
			return cliente;
		} catch (NameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> buscarPorNome", e);
		
		}
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) throws BusinessException {
		try {
			return dao.findAll(pageable);
		} catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> findAll ( pageable ) ", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		try {
			return dao.findAll();
		} catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> findAll");

		}
	}

	@Override 	@Transactional(readOnly = true)
	public Optional<Cliente> buscarPorEmail(String email) throws EmailNotFoundException{
		return Optional.of(dao.findByEmail(email).orElseThrow(() -> new EmailNotFoundException()));
	}


}
