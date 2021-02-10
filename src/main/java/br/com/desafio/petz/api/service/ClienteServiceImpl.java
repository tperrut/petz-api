package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
		}catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> excluir", e);
		
		}
		
	}
	
	
	public Optional<Cliente> getClienteById(Long id) {
		 return dao.findById(id);
	}

	private void verificarSeClienteExiste(Long id) {
		 dao.findById(id).orElseThrow(() ->  new ResourceNotFoundException("CLIENTE ID " + id.toString()));
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
				throw new ResourceNotFoundException(" CLIENTE_NOT_FOUND " + nome);
			}
			return cliente;
		}catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> buscarPorNome", e);
		
		}
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) throws BusinessException {
		try {
			return dao.findAll(pageable);
		} catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> findAll ( pageable ) ");
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

	public void clienteExisteByNome(String cliente) throws ResourceNotFoundException, BusinessException {
		try {
			Optional<List<Cliente>> clienteOptional = dao.findByNome(cliente);

			if (!clienteOptional.isPresent()) {
				LOGGER.info(String.format("CLIENTE_NOT_FOUND:  %s", cliente));
				throw new ResourceNotFoundException(" CLIENTE_NOT_FOUND_BY_CLIENTE " + cliente);
			}
		} catch (Exception e) {
			throw new BusinessException("ERRO INTERNO -> clienteExisteByNome");

		}
	}


}
