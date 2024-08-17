package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

	public enum AVALIABLE_ROLES { ADMIN, USER, STUDENT, EMPLOYEE }

	public void createDefaultValues();

	public Role findByName(String name);

	public Role findDefault();

}