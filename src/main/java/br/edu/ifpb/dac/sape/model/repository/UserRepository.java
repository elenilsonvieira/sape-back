package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import br.edu.ifpb.dac.sape.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Integer>{
	
	Optional<User> findByName(String name);
	boolean existsByName(String name);
	Optional<User> findByRegistration(Long registration);
	boolean existsByRegistration(Long registration);
	
}
