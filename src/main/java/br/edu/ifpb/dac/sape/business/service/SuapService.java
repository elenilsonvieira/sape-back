package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.business.client.SuapClient;
import br.edu.ifpb.dac.sape.business.client.dto.FindUserSuapResponse;
import br.edu.ifpb.dac.sape.business.client.dto.LoginSuapRequest;
import br.edu.ifpb.dac.sape.business.client.dto.LoginSuapResponse;
import br.edu.ifpb.dac.sape.business.client.dto.UserSuapResponse;
import br.edu.ifpb.dac.sape.model.entity.Role;
import br.edu.ifpb.dac.sape.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuapService implements SuapServiceInterface {

	private final SuapClient suapClient;
	private final RoleService roleService;

	@Override
	public String login(String username, String password) {
		LoginSuapRequest loginRequest = LoginSuapRequest.builder()
				.username(username)
				.password(password)
				.build();
		LoginSuapResponse loginResponse = suapClient.login(loginRequest);
		return loginResponse.getAccessToken();
	}

	@Override
	public User findUser(String token, String username) {
		User employee = findEmployee(token, username);
		User student = findStudent(token, username);

		return employee == null ? student : employee;
	}

	@Override
	public User findEmployee(String token, String username) {
		String bearerToken = TOKEN_HEADER_VALUE + token;
		FindUserSuapResponse employeeResponse = suapClient.findEmployee(bearerToken, username);

		List<UserSuapResponse> results = employeeResponse.getResults();
		if (!results.isEmpty()) {
			UserSuapResponse employee = employeeResponse.getResults().get(0);

			List<Role> roles = new ArrayList<>();
			roles.add(roleService.findDefault());
			roles.add(roleService.findByName(RoleService.AVALIABLE_ROLES.EMPLOYEE.name()));

			User user = new User();
			user.setName(employee.getName());
			user.setRegistration(Long.parseLong(employee.getRegistration()));
			user.setRoles(roles);
			return user;
		}

		return null;
	}

	@Override
	public User findStudent(String token, String username) {
		String bearerToken = TOKEN_HEADER_VALUE + token;
		FindUserSuapResponse studentResponse = suapClient.findStudent(bearerToken, username);

		List<UserSuapResponse> results = studentResponse.getResults();
		if (!results.isEmpty()) {
			UserSuapResponse employee = studentResponse.getResults().get(0);

			List<Role> roles = new ArrayList<>();
			roles.add(roleService.findDefault());
			roles.add(roleService.findByName(RoleService.AVALIABLE_ROLES.STUDENT.name()));

			User user = new User();
			user.setName(employee.getName());
			user.setRegistration(Long.parseLong(employee.getRegistration()));
			user.setRoles(roles);
			return user;
		}

		return null;
	}

	@Override
	public String findEmployee(String token) {
		return null; // Implementar conforme a necessidade
	}

	@Override
	public String findStudent(String token) {
		return null; // Implementar conforme a necessidade
	}

}
