package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectAlreadyExistsException;


public class UserServiceTest {
	
	@InjectMocks
	private static UserService service;
	private User exUser;
	private Sport exSport;
	
	
	@Mock
	private static UserRepository repository;
	@Mock
	private static SportRepository sporRepository;

	@Mock
	private SportService sportService;
	
	@BeforeAll
	public static void setup() {
		
		service = new UserService();
		ReflectionTestUtils.setField(service, "userRepository", repository);
		}
	
	@BeforeEach
	public void beforeEach() {
		openMocks(this);
		exUser = new User();		

		exUser = new User();
		exSport = new Sport();
	}

	@Test
	public void findByIdValid() { // id valid
		try {
			when(repository.existsById(anyInt())).thenReturn(true);
			
			assertDoesNotThrow(() -> service.findById(1));
			verify(repository.getById(1));
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void findByIdInvalid() { // id invalid because is not found in DB
		try {
			when(repository.existsById(anyInt())).thenReturn(false);
			
			Throwable exc = assertThrows(ObjectNotFoundException.class, () -> service.findById(1));
			assertEquals("Não foi encontrado usuário com id 1", exc.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Ful@no", "Cilas de Talz \t", "   1", "\t%¨&"}) // name valid
	public void findByNameValid(String n) {
		when(repository.existsByName(n)).thenReturn(true);
		
		assertDoesNotThrow(() -> service.findByName(n));
		verify(repository).findByName(n);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "   ", "\t\t", " \n \n "}) // name invalid because is empty
	public void findByNameInvalidEmpty(String n) {
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.findByName(n));
		assertEquals("Não foi possível concluir a ação, o campo nome está faltando!", exc.getMessage());
	}
	
	@Test
	public void findByNameInvalidNull() { // name invalid because is null
		exUser.setName(null); // only to turn visible
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.findByName(exUser.getName()));
		assertEquals("Não foi possível concluir a ação, o campo nome está faltando!", exc.getMessage());
	}
	
	@Test
	public void findByRegistrationValid() { // registration valid
		when(repository.existsByRegistration(anyLong())).thenReturn(true);
		
		assertDoesNotThrow(() -> service.findByRegistration(Long.valueOf(20201502)));
		verify(repository).findByRegistration(Long.valueOf(20201502));
	}
	
	@Test 
	public void findByRegistrationInvalidNotFound() { // registration invalid because is not found in DB
		when(repository.existsByRegistration(anyLong())).thenReturn(false);
		
		Throwable exc = assertThrows(ObjectNotFoundException.class, () -> service.findByRegistration(Long.valueOf(123456789)));
		assertEquals("Não foi encontrado usuário com matrícula 123456789", exc.getMessage());
	}
	
	@Test 
	public void findByRegistrationInvalidNull() { // registration invalid because is null
		exUser.setRegistration(null); // only to turn visible
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.findByRegistration(exUser.getRegistration()));
		assertEquals("Não foi possível concluir a ação, o campo matrícula está faltando!", exc.getMessage());
	}
	
	@Test
	public void saveValid() { // save valid
		exUser.setName("M@ry");
		exUser.setRegistration(Long.valueOf(159357));
		
		when(repository.existsByRegistration(anyLong())).thenReturn(false);
		
		assertDoesNotThrow(() -> service.save(exUser));
		verify(repository).save(exUser);
	}
	
	@Test
	public void saveInvalidAlwaysInDB() { // save invalid because User is already in DB
		exUser.setName("M@ry");
		exUser.setRegistration(Long.valueOf(159357));
		
		when(repository.existsByRegistration(anyLong())).thenReturn(true);
		
		Throwable exc = assertThrows(ObjectAlreadyExistsException.class, () -> service.save(exUser));
		assertEquals("Já existe um usuário com matrícula 159357", exc.getMessage());
	}
	
	@Test
	public void saveInvalidNameIsNull() { // save invalid because User's name is null
		exUser.setName(null); // only to turn visible
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.save(exUser));
		assertEquals("Não foi possível usar save, o campo nome está faltando!", exc.getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "   ", "\t   ", " \n\n "}) // save invalid because User's name is empty
	public void saveInvalidEmptyName(String n) {
		exUser.setName(n);
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.save(exUser));
		assertEquals("Não foi possível usar save, o campo nome está faltando!", exc.getMessage());
	}
	
	@Test
	public void updateValid() { // update valid 
		try {
			exUser.setName("John");
			exUser.setId(1);
			exUser.setRegistration(Long.valueOf(13579));
			Optional<User> op = Optional.of(exUser);

			when(repository.existsById(anyInt())).thenReturn(true);
			when(repository.existsByRegistration(anyLong())).thenReturn(true);
			when(repository.findByRegistration(anyLong())).thenReturn(op);
			
			assertDoesNotThrow(() -> service.update(exUser));
			verify(repository).save(exUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateInvalidNameNull() { // update invvalid because User's name is null 
		exUser.setName(null); // barred on the first verification in service. Only to turn visible.
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.update(exUser));
		assertEquals("Não foi possível usar update, o campo nome está faltando!", exc.getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "   ", "\n ", " \t\n"})  // update invalid because User's name is empty
	public void updateInvalidEmptyName(String s) {
		exUser.setName(s);
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.update(exUser));
		assertEquals("Não foi possível usar update, o campo nome está faltando!", exc.getMessage());
	}
	
	@Test
	public void updateInvalidIdNull() { // update invalid because User's id is mull
		exUser.setName("John Legend");
		exUser.setId(null); // Only to turn visible. Throw exception on the secouund verification
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.update(exUser));
		assertEquals("Não foi possível usar update, o campo id está faltando!", exc.getMessage());
	}
	
	@Test
	public void updateInvalidIdNotMatch() { // update invalid because User's id not match with the id of User on DB
		exUser.setName("John Legend"); // object A
		exUser.setId(1); // pertence to object A in DB
		exUser.setRegistration(Long.valueOf(123123)); // pertence to object B in DB
		
		User otherUser = new User();
		otherUser.setId(2);
		otherUser.setName("Robbie Williams"); //object B in DB
		otherUser.setRegistration(Long.valueOf(123123));
		Optional<User> op = Optional.of(otherUser);
		
		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.existsByRegistration(anyLong())).thenReturn(true);
		when(repository.findByRegistration(anyLong())).thenReturn(op);
		
		Throwable exc = assertThrows(ObjectAlreadyExistsException.class, () -> service.update(exUser));
		assertEquals("Já existe um usuário com matrícula 123123", exc.getMessage());
	}
	
	@Test
	public void deleteByUserValid() { // delete by User valid
		exUser.setId(1);
		when(repository.existsById(1)).thenReturn(true);
		
		assertDoesNotThrow(() -> service.delete(exUser));
		verify(repository).delete(exUser);
	}
	
	@Test
	public void deleteByUserInvalidIdNull() { // delete by User invalid because User's id is null
		exUser.setId(null); // Only to turn visible. Is the first verification in service
		
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.delete(exUser));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exc.getMessage());
	}
	
	@Test
	public void deleteByUserInvalidIdNotFound() { // delete by User invalid because User's id is not found in DB
		exUser.setId(1);
		
		when(repository.existsById(1)).thenReturn(false);
		
		Throwable exc = assertThrows(ObjectNotFoundException.class, () -> service.delete(exUser));
		assertEquals("Não foi encontrado usuário com id 1", exc.getMessage());
	}
	
	@Test
	public void deleteByIdValid() { // delete by id valid
		when(repository.existsById(1)).thenReturn(true);
		
		assertDoesNotThrow(() -> service.deleteById(1));
		verify(repository).deleteById(1);
	}
	
	@Test
	public void deleteByIdInvalid() { // delete by id invalid because id is null
		Throwable exc = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exc.getMessage());
	}
	
	@Test
	public void deleteByIdInvalidIdNotFound() { // delete by id invalid because id is not found in DB
		when(repository.existsById(1)).thenReturn(false);
		
		Throwable exc = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(1));
		assertEquals("Não foi encontrado usuário com id 1", exc.getMessage());
	}
	

	
	  @Test
	    public void testAddSportsFavorite() throws Exception {
	
	        Integer userId = 1;
	        Integer sportId = 1;

	        User user = new User();
	        user.setId(1);
	        user.setName("igor");
	        
	        when(repository.findById(userId)).thenReturn(Optional.of(user));

	        Sport sport = new Sport();
	        sport.setId(1);
	        sport.setName("futebol");
	        when(sportService.findById(sportId)).thenReturn(sport);

	  
	        service.addSportsFavorite(userId, sportId);

	        verify(repository).findById(userId);
	        verify(sportService).findById(sportId);
	        verify(repository).save(user);
	
	        assertTrue(user.getSportsFavorite().contains(sport));
	    }

	    @Test
	    public void testAddSportsFavorite_UserNotFound() {
	   
	        Integer userId = 1;
	        Integer sportId = 1;
	
	        when(repository.findById(userId)).thenReturn(Optional.empty());

	        assertThrows(IllegalArgumentException.class, 
	        		() -> service.addSportsFavorite(userId, sportId));

	        verify(repository, never()).save(any());
	    }
	

	
	@Test
	public void testRemoveSportsFavorite()throws Exception {
		
		Integer userId = 1;
        Integer sportId = 1;
        
        User user = new User();
        user.setId(1);
        user.setName("igor");
        
        when(repository.findById(userId)).thenReturn(Optional.of(user));
       
        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("futebol");
        when(sportService.findById(sportId)).thenReturn(sport);
        
        List<Sport> removedSport = new ArrayList<>();
        removedSport.add(sport);
        
        user.setSportsFavorite(removedSport);
        
        service.removeSportsFavorite(userId, sportId);
        
        assertFalse(user.getFavorateSports().contains(sport));
        
	}
	
	
	
	@Test
	public void testRemoveSportsFavorite_UserNotFound() {
		// Configurar dados de teste
		Sport exSport = new Sport();
		exSport.setId(1);
		exUser.setId(1);

		// Executar a funcionalidade a ser testada
		Throwable exception = assertThrows(IllegalArgumentException.class, () ->
			service.removeSportsFavorite(exUser.getId(), exSport.getId())
		);

		// Verificar o resultado
		assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	public void testRemoveSportsFavorite_SportNotFound() {
		// Configurar dados de teste
		Sport exSport = new Sport();
		exSport.setId(1);
		exUser.setId(1);

		// Executar a funcionalidade a ser testada
		Throwable exception = assertThrows(IllegalArgumentException.class, () ->
			service.removeSportsFavorite(exSport.getId(), exUser.getId())
		);

		// Verificar o resultado
		assertEquals("Sport not found", exception.getMessage());
	}
	
}

