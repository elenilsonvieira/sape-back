package br.edu.ifpb.dac.sape.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.sape.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Optional<Role> findByName(String name);
	public boolean existsByName(String name);

}
