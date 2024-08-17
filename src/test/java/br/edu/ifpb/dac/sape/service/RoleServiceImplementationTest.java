package br.edu.ifpb.dac.sape.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ifpb.dac.sape.model.repository.RoleRepository;

public class RoleServiceImplementationTest {
	
	@InjectMocks
	private static RoleServiceImpl roleImplementation;
	@Mock
	private static RoleRepository repository;
	
	@BeforeAll
    public static void setup() {
        roleImplementation = new RoleServiceImpl();
    }
	
	@BeforeEach
    public void beforeEach() throws Exception {
        System.out.println("Initializing classes...");
        MockitoAnnotations.openMocks(this);

    }
	
	@Test
	public void findbyNameException() {
		String nome=null;

		Throwable expected = assertThrows
		(IllegalStateException.class, ()-> roleImplementation.findByName(nome));
		
		assertEquals("Nome não pode ser nulo",expected.getMessage());
		
	}
	
	@Test
	public void findByNameIsNotPresent() {
		String nome="GUEST";
		
		assertEquals(null,roleImplementation.findByName(nome));
	}
	
	@Test
	public void findByNameIsPresent() {
		String nome="ADMIN";
		
		Role role = new Role();
		role.setName("ADMIN");
		role.setId(1);
		
		when(repository.findByName(nome)).thenReturn(Optional.of(role));
		Role retorno = roleImplementation.findByName(nome);
		assertEquals(nome,retorno.getName());
	}
	
	@Test
	public void findDefaultIsUser() {
		Role role = new Role();
		role.setName("USER");
		role.setId(2);
		
		when(repository.findByName(role.getName())).thenReturn(Optional.of(role));
		Role retorno = roleImplementation.findDefault();
		assertNotNull(retorno);
		assertEquals("USER",retorno.getName());
	}
	
	
	
	
}
