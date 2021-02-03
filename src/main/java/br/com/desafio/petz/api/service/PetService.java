package br.com.desafio.petz.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.desafio.petz.api.dto.PetDto;
import br.com.desafio.petz.api.model.Pet;

public interface PetService {
	
	Pet salvar(Pet Pet);
	
	void excluir(Long id);

	Optional<Pet> buscarPorId(Long id);

	Page<Pet> findAll(Pageable pageable);
	
	Optional<List<Pet>> buscarPorNome(String name);

	List<Pet> findAll();
	
}
