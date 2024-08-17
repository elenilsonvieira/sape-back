package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import br.edu.ifpb.dac.sape.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByName(String name);
	boolean existsByName(String name);

}
