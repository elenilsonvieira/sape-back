package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.sape.model.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer>{
	
	public Optional<Place> findByName(String name);
	public boolean existsByName(String name);
	
}
