package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import br.edu.ifpb.dac.sape.model.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository <Sport, Integer> {
	
	Optional<Sport> findByName(String name);
	boolean existsByName(String name);
	
}
