package br.com.desafio.petz.api.dao;

import br.com.desafio.petz.api.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PetRepository extends JpaRepository<Pet,Long>{

	Optional<Pet> findById(Long id);

	Optional<List<Pet>> findByNome(String nome);
	
}
