package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.sape.business.service.LoginConverterService;
import br.edu.ifpb.dac.sape.business.service.LoginService;
import br.edu.ifpb.dac.sape.business.service.RoleServiceImpl;
import br.edu.ifpb.dac.sape.business.service.SuapService;
import br.edu.ifpb.dac.sape.business.service.TokenService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;


public class LoginServiceTest {
	
	@Mock
    private static UserService userService;

    @Mock
    private static SuapService suapService;

    @Mock
    private LoginConverterService loginConverter;

    @Mock
    private TokenService tokenService;


    @InjectMocks
    private static LoginService loginService;
	
	@BeforeAll
	private static void setUP() {
		loginService = new LoginService();
	}
	@BeforeEach
	public void beforeEach() throws Exception {
		System.out.println("Initializing classes...");
		MockitoAnnotations.openMocks(this);
		
	}

	
	@Test
	public void loginIllagal0ArgumentException() {
		String name= "Teste";
		String password = "";
		
		Throwable expected = assertThrows
		(IllegalArgumentException.class, ()-> loginService.login(name,password));
		
		assertEquals("Campo username ou password inválido!",expected.getMessage());
	}
	
	
	@Test
	public void loginpasswordnull() {
		String name= null;
		String password = null;
		
		Throwable expected = assertThrows
				(IllegalArgumentException.class, ()-> loginService.login(name,password));
		
		assertEquals("Campo username ou password inválido!",expected.getMessage());
		
	}
	
    @Test
    void testLoginWithInvalidUsername() {
        String username = null;
        String password = "password";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loginService.login(username, password));

        assertEquals("Campo username ou password inválido!", exception.getMessage());
    }


    @Test
    void testLoginWithInvalidCredentials(){
        String username = "username";
        String password = "password";
        String jsonToken = null;

        when(suapService.login(username, password)).thenReturn(jsonToken);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loginService.login(username, password));

        assertEquals("Campo username ou password inválido!", exception.getMessage());
    }

    @Test
    void testLoginWithSuapTokenNull()  {
        String username = "username";
        String password = "password";
        String jsonToken = "token";
        String suapToken = null;

        when(suapService.login(username, password)).thenReturn(jsonToken);
        when(loginConverter.jsonToToken(jsonToken)).thenReturn(suapToken);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loginService.login(username, password));

        assertEquals("Campo username ou password inválido!", exception.getMessage());
    }

    @Test
    void testLoginWithUserFound() throws NumberFormatException, Exception {
        String username = "12345";
        String password = "password";
        String jsonToken = "token";
        String suapToken = "suap_token";
        User user = new User();
        user.setId(1);
        user.setName("User Test");

        when(suapService.login(username, password)).thenReturn(jsonToken);
        when(loginConverter.jsonToToken(jsonToken)).thenReturn(suapToken);
        when(userService.findByRegistration(Long.parseLong(username))).thenReturn(Optional.of(user));
        when(tokenService.generate(user)).thenReturn("generated_token");

        String generatedToken = loginService.login(username, password);

        assertEquals("generated_token", generatedToken);
    }

	
	
	
	
	

	
}