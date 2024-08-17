package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import br.edu.ifpb.dac.sape.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer>{
	
	Optional<Place> findByName(String name);
	boolean existsByName(String name);
	
}
