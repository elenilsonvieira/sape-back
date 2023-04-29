package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ifpb.dac.sape.business.service.LoginConverterService;

import br.edu.ifpb.dac.sape.business.service.RoleService;
import br.edu.ifpb.dac.sape.business.service.RoleService.AVALIABLE_ROLES;

import br.edu.ifpb.dac.sape.model.entity.Role;
import br.edu.ifpb.dac.sape.model.entity.User;

public class LoginConverterServiceTest {
	// By Igor
	@Mock
    private RoleService roleService;

    @InjectMocks
    private User userUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
	

	
	
	@InjectMocks
	private static LoginConverterService loginService;

	
	@Test
	public void testJsonToUser() throws Exception {
		// Testing that User to JSON conversion is working correctly...

		
	    String json = "{\"results\":[{\"nome\":\"Joao silva\",\"matricula\":\"123456\"}]}";
	    User expectedUser = new User();
	    expectedUser.setName("Joao silva");
	    expectedUser.setRegistration(123456L);
	    
	    List<Role> roles = new ArrayList<>();
	    roles.add(roleService.findDefault());
	    roles.add(roleService.findByName(AVALIABLE_ROLES.STUDENT.name()));
	    
	    expectedUser.setRoles(roles);
	    
	    User resultUser = loginService.jsonToUser(json);
	    
	    assertEquals(expectedUser.getName(), resultUser.getName());
	    assertEquals(expectedUser.getRegistration(), resultUser.getRegistration());
	    assertEquals(expectedUser.getAuthorities(), resultUser.getAuthorities());
	}
	
	@Test
	public void testJsonToToken() {
		// this test checks if the returned token is equal to the expected token
		
		String json = "{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3OD\"}";
	    String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3OD";
	    
	    String actualToken = loginService.jsonToToken(json);
	    
	    assertEquals(expectedToken, actualToken);
	}
	@Test
	public void testJsonToTokenIsReturnNull() {
		// test if JsonToToken file returns null in case of json Null
	   
	    String json = null;
	    
	    String result = loginService.jsonToToken(json);
	    System.out.println(result);
	    assertNull(result);
	    
	}
}
