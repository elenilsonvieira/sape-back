package br.edu.ifpb.dac.sape.model.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.sape.presentation.dto.LoginDTO;

/**
 * 
 * @author Ytallo
 * Test class created to test the loginDTO class
 */
public class LoginDTOTest {
	
	private static LoginDTO loginDTO;
	
	@BeforeEach
	public void beforeEach() {
		loginDTO = new LoginDTO();
	}
	
	@Test
	public void passwordIsNotNull() {
		loginDTO.setPassword("ewgaerae3453234");
		assertNotNull(loginDTO.getPassword(), "Invalid password: password is null");
	}
	
	@Test
	public void passwordIsNull() {
		String password = loginDTO.getPassword();
		assertNull(password,"Password ins't null");
	}
	/**
	 * Password characters number must be <=3 and <=255
	 */
	@Test
	public void IspasswordLenghtValid() {
		loginDTO.setPassword("asdasdasd");
		int size = loginDTO.getPassword().length();
		boolean isSizeValid;
		if(size>=3 && size <255) {
			isSizeValid = true;
		}else {
			isSizeValid = false;
		}
		assertTrue(isSizeValid, "Invalid password: password character number must be >=3 and <=255");
	}
	
	@Test
	public void userNameIsNotNull() {
		loginDTO.setUsername("Ytallo Alves");
		assertNotNull(loginDTO.getUsername(), "Invalid user name: user name is null");
	}
	
	@Test
	public void userNameIsNull() {
		String password = loginDTO.getPassword();
		assertNull(password,"Invalid user name: user name is null");
	}
	
}