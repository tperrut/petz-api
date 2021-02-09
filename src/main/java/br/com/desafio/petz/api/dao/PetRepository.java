package br.com.desafio.petz.api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.desafio.petz.api.model.Pet;


@Repository
public interface PetRepository extends JpaRepository<Pet,Long>{

	Optional<Pet> findById(Long id);

	Optional<List<Pet>> findByNome(String nome);
	
}
