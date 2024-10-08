package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.business.service.RoleService.AVALIABLE_ROLES;
import br.edu.ifpb.dac.sape.model.entity.Role;
import br.edu.ifpb.dac.sape.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LoginConverterService {

	@Autowired
	private RoleService roleService;
	
	public String jsonToToken(String json) {
		if(json == null) {
			return null;
		}
		
		JsonElement jsonElement = JsonParser.parseString(json);
		String token = jsonElement.getAsJsonObject().get("access").getAsString();
		return token;
	}
	
	public User jsonToUser(String json) {
		JsonElement jsonElement = JsonParser.parseString(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject()
											.get("results")
											.getAsJsonArray()
											.get(0)
											.getAsJsonObject();
		
		String name = jsonObject.get("nome").getAsString();
		String registration = jsonObject.get("matricula").getAsString();
		
		List<Role> roles = new ArrayList<>();
		roles.add(roleService.findDefault());
		
		try {
			// lança exceção se não encontrar o usuário servidor no SUAP
			roles.add(roleService.findByName(AVALIABLE_ROLES.EMPLOYEE.name()));
		} catch (Exception e) {
			roles.add(roleService.findByName(AVALIABLE_ROLES.STUDENT.name()));
		}
		
		User user = new User();
		user.setName(name);
		user.setRegistration(Long.parseLong(registration));
		user.setRoles(roles);
		
		return user;
	}

	public String mapToJson(Map<String, String> map) {
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
}
