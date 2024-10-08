package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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