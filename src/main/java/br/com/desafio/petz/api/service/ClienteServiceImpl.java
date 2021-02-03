package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.desafio.petz.api.dao.ClienteRepository;
import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.web.exception.ResourceNotFoundException;

@Service 
public class ClienteServiceImpl implements ClienteService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClienteRepository dao;
	
	@Override @Transactional(propagation = Propagation.REQUIRED)
	public Cliente salvar(Cliente Cliente) {
		return dao.save(Cliente);
	}

	@Override 
	public void excluir(Long id) {}

	
	@Override	@Transactional(readOnly = true)
	public Optional<Cliente> buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Override	@Transactional(readOnly = true)
	public Optional<List<Cliente>> buscarPorNome(String nome) {
		return dao.findByNome(nome);
	}
	
	@Override @Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override @Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return dao.findAll();
	}
	
	public void verificarSeClienteExiste(String pet) {
		Optional<List<Cliente>> petOptional = dao.findByNome(pet);

		if (!petOptional.isPresent()) {
			LOGGER.info(" PET_NOT_FOUND_BY_PET " + pet);
			throw new ResourceNotFoundException(" PET_NOT_FOUND_BY_PET " + pet);
		}
	}

	public Optional<Cliente> verificarSeClienteExiste(Long id) {
		Optional<Cliente> petOptional = dao.findById(id);

		if (!petOptional.isPresent()) {
			LOGGER.info(" PET_NOT_FOUND " + id);
			throw new ResourceNotFoundException(" PET_NOT_FOUND " + id);
		}
		return petOptional;
	}
}
