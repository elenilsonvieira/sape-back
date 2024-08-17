package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@RequiredArgsConstructor
public class LoginService {

	private final UserService userService;
	private final SuapService suapService;
	private final TokenService tokenService;
	
	public String login(String username, String password) throws NumberFormatException, Exception {
		if(username == null || password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
			
		String suapToken = suapService.login(username, password);

		boolean existsByRegistration = userService.existsByRegistration(Long.parseLong(username));

		if (!existsByRegistration) {
			User userFromSuap = suapService.findUser(suapToken, username);
			User user = userService.save(userFromSuap);
			return tokenService.generate(user);
		}

		User user = userService.findByRegistration(Long.parseLong(username));
		return tokenService.generate(user);
	}

	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
	
}
