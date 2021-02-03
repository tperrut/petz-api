package br.com.desafio.petz.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.desafio.petz.api.dao.PetRepository;
import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Pet;

@Service 
public class PetServiceImpl implements PetService {
	
	@Autowired
	private PetRepository dao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Pet salvar(Pet pet) {
		return dao.save(pet);
	}

	public void excluir(Long id) {}

	
	@Transactional(readOnly = true)
	public Optional<Pet> buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<List<Pet>> buscarPorNome(String nome) {
		return dao.findByNome(nome);
	}
	
	@Transactional(readOnly = true)
	public Page<Pet> findAll(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<Pet> findAll() {
		return dao.findAll();
	}

	
	
	
	


}
