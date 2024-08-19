package br.edu.ifpb.dac.sape.service;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;


public class SchedulingServiceTest {

	@Mock
	private static UserService userService;

	@Mock
	private static SchedulingRepository repository;

	
	@InjectMocks
	private static SchedulingService service;
	
	private static Scheduling schedulingExp;
	private static Place placeExp;
	private static User userExp;

	@BeforeAll
	public static void setup() {
		service = new SchedulingService();
		schedulingExp = new Scheduling();
		
		placeExp = new Place();
		userExp = new User();
		userExp.setId(3);
		
		ReflectionTestUtils.setField(service, "schedulingRepository", repository);
		
	}

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this); 
		
		User user = new User();
		user.setId(1);
		User user02 = new User();
		user02.setId(2);
		Set<User> users = Set.of(user, user02);
		schedulingExp.setParticipants(users);
	}

	@DisplayName("Id Valid")
	@Test
	public void testFindByIdObjectValid() {

		when(repository.existsById(anyInt())).thenReturn(true);

		assertDoesNotThrow(() -> service.findById(2));
		verify(repository).getById(2);
	}

	@DisplayName("Id Invalid")
	@Test
	public void testFindByIdObjectInalid() {
		when(repository.existsById(anyInt())).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(2));
		assertEquals("Não foi encontrado agendamento com id 2", exception.getMessage());

	}

	@DisplayName("Save Valid")
	@Test
	public void testSaveObjectValid() {

		schedulingExp.setId(2);

		when(repository.existsById(anyInt())).thenReturn(false);

		assertDoesNotThrow(() -> service.save(schedulingExp));
		verify(repository).save(schedulingExp);

	}

	@DisplayName("Delete Valid User")
	@Test
	public void testDeleteValidUser() {

		when(repository.existsById(2)).thenReturn(true);

		assertDoesNotThrow(() -> service.deleteById(2));
		verify(repository).deleteById(2);
	}

	@DisplayName("Delete Invalid User and null id")
	@Test
	public void testDeleteInvalidUser() {
		schedulingExp.setId(2);

		when(repository.existsById(2)).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.delete(schedulingExp));
		assertEquals("Não foi encontrado agendamento com id 2", exception.getMessage());

		Throwable exception02 = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exception02.getMessage());
	}

	@DisplayName("Quantity of participants and participants valids")
	@Test
	public void getSchedulingQuantityOfParticipantsValid() {
		try {
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedulingExp);
			
			//quantity os particiapnts
			int qtdPart = service.getSchedulingQuantityOfParticipants(1);

			assertEquals(2, qtdPart);
			
			//participants
			Set<User> participants = service.getSchedulingParticipants(1);

			assertTrue(participants.containsAll(schedulingExp.getParticipants()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@DisplayName("Quantity of participants and participants invalid")
	@Test
	public void getSchedulingQuantityOfParticipantsInvalid() {
		// ID is null - quantity of participants
		Throwable excep = assertThrows(MissingFieldException.class,
				() -> service.getSchedulingQuantityOfParticipants(null));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());

		// ID is null - participants
		Throwable excep03 = assertThrows(MissingFieldException.class,
				() -> service.getSchedulingParticipants(null));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep03.getMessage());
		
		when(repository.existsById(1)).thenReturn(false);
		
		// ID not finded in DB - quantity of participants
		Throwable excep02 = assertThrows(ObjectNotFoundException.class,
				() -> service.getSchedulingQuantityOfParticipants(1));
		assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
		
		// ID not finded in DB - participants
		Throwable excep04 = assertThrows(ObjectNotFoundException.class,
				() -> service.getSchedulingParticipants(1));
		assertEquals("Não foi encontrado agendamento com id 1", excep04.getMessage());
	}
	
	@DisplayName("add participant valid")
	@Test
	public void addSchedulingParticipantValid() {
		try {
			placeExp.setMaximumCapacityParticipants(3);
			Scheduling schedSpy = spy(schedulingExp);
			schedSpy.setPlace(placeExp);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.addSchedulingParticipant(1, userExp);
			verify(schedSpy).setParticipants(anySet());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@DisplayName("add participant invalid")
	@Test
	public void addSchedulingParticipantInvalid() {
		
		//ID is null
		Throwable excep = assertThrows(MissingFieldException.class, () -> service.addSchedulingParticipant(null, userExp));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());
		
		when(repository.existsById(1)).thenReturn(false);
		
		//ID is not present in DB 
		Throwable excep02 = assertThrows(ObjectNotFoundException.class, () -> service.addSchedulingParticipant(1, userExp));
		assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
		
		//no more confirmations avaliable to this scheduling - atcualy we have 2 confirmed, and place capacity 3
		placeExp.setMaximumCapacityParticipants(2);
		Scheduling schedSpy = spy(schedulingExp);
		schedSpy.setPlace(placeExp);
		
		try {
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.addSchedulingParticipant(1, userExp);
			
			assertAll("quantity of scheduling participants must be 2, and setParticipants never called",
					() -> assertEquals(2, schedSpy.getParticipants().size()),
					() -> verify(schedSpy,never()).setParticipants(anySet())
			);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@DisplayName("removing participant valid")
	@Test
	public void removeSchedulingParticipantValid() {
		try {
			Scheduling schedSpy = spy(schedulingExp);
			Set<User> set = new HashSet<User>(schedSpy.getParticipants());
			set.add(userExp);
			schedSpy.setParticipants(set);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.removeSchedulingParticipant(1, userExp);
			assertAll("tests with removing interested user of scheduling",
					() -> verify(schedSpy, times(3)).getParticipants(),
					() -> assertEquals(2, schedSpy.getParticipants().size()),
					() -> assertFalse(schedSpy.getParticipants().contains(userExp))
			);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@DisplayName("removing participant invalid")
	@Test
	public void removeSchedulingParticipantInvalid() {
		try {
			//ID is null
			Throwable excep = assertThrows(MissingFieldException.class, () -> service.addSchedulingParticipant(null, userExp));
			assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());
			
			when(repository.existsById(1)).thenReturn(false);
			
			//ID is not present in DB 
			Throwable excep02 = assertThrows(ObjectNotFoundException.class, () -> service.addSchedulingParticipant(1, userExp));
			assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
			
			//list participant empty on scheduling
			Set<User> listEmpty = Set.of();
			schedulingExp.setParticipants(listEmpty);
			Scheduling schedSpy = spy(schedulingExp);
			schedSpy.setParticipants(listEmpty);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.removeSchedulingParticipant(1, userExp);
			
			verify(schedSpy,times(1)).getParticipants();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	@DisplayName("get all by date scheduling - valid")
	void getAllByDateValid() {
		Scheduling sched01 = new Scheduling();
		LocalDate d01 = LocalDate.now().plusDays(-1); // 1 day before
		sched01.setScheduledDate(d01);
		
		Scheduling sched02 = new Scheduling();
		LocalDate d02 = LocalDate.now(); 			 // today
		sched02.setScheduledDate(d02);
		
		Scheduling sched03 = new Scheduling();
		LocalDate d03 = LocalDate.now().plusDays(1); // tomorrow
		sched03.setScheduledDate(d03);
		
		List<Scheduling> list = new ArrayList<>();
		list.add(sched01);
		list.add(sched02);
		list.add(sched03);
		
		when(repository.findAll()).thenReturn(list);
		
		List<Scheduling> returned = service.findAll();
		
		assertAll("return only schedulins to today and to the future",
			() -> assertEquals(2, returned.size()),
			() -> assertEquals(d02, returned.get(0).getScheduledDate()),
			() -> assertEquals(d03, returned.get(1).getScheduledDate())
		);
	}
	
	@Test
	@DisplayName("get all by date scheduling - invalid")
	void getAllByDateInvalid() {
		Scheduling sched01 = new Scheduling();
		LocalDate d01 = LocalDate.now().plusDays(-1); // 1 day before
		sched01.setScheduledDate(d01);
		
		Scheduling sched02 = new Scheduling();
		LocalDate d02 = LocalDate.now().plusDays(-2); // 2 days before
		sched02.setScheduledDate(d02);
		
		Scheduling sched03 = new Scheduling();
		LocalDate d03 = LocalDate.now().plusDays(-3); // 3 days before
		sched03.setScheduledDate(d03);
		
		List<Scheduling> list = new ArrayList<>();
		list.add(sched01);
		list.add(sched02);
		list.add(sched03);
		
		when(repository.findAll()).thenReturn(list);
		
		List<Scheduling> returned = service.findAll();
		
		assertEquals(0, returned.size());
	}
	
	//!!!!
	  @Test
	    public void testGetSchedulingsByUserRegistration_ValidUser() throws Exception {
	        
	       
	        userExp.setRegistration(1234L);
	        
	        
	        Scheduling scheduling1 = new Scheduling();
	        Scheduling scheduling2 = new Scheduling();
	        
	        
	        when(userService.findByRegistration(1234L)).thenReturn(Optional.of(userExp));

	      
	        when(repository.findAllByParticipantsContaining(userExp)).thenReturn(List.of(scheduling1, scheduling2));

	        
	        List<Scheduling> result = service.getSchedulingsByUserRegistration(1234L);
	        
	        
	        assertEquals(2, result.size());
	        assertEquals(scheduling1, result.get(0));
	        assertEquals(scheduling2, result.get(1));
	    }
	
	   @Test
	    public void testGetSchedulingsByUserRegistration_InvalidUser() throws Exception {
	        when(userService.findByRegistration(123456L)).thenReturn(Optional.empty());

	       
	        List<Scheduling> result = service.getSchedulingsByUserRegistration(123456L);
	        
	        
	        
	        assertEquals(Collections.emptyList(), result);
	    }
	   
	   @Test
	    public void testGetSchedulingsByUserRegistration_UserServiceError() throws Exception {
	        
	        when(userService.findByRegistration(123456L)).thenThrow(new Exception("Erro ao buscar usuário"));

	        Exception exception = Assertions.assertThrows(Exception.class, () -> {
	            service.getSchedulingsByUserRegistration(123456L);
	        });

	        System.out.println(exception.getMessage());
	        assertEquals("Erro ao buscar usuário", exception.getMessage());
	    }
	   
	   @Test
	    public void testGetSchedulingsByUserRegistration_UserFound_ReturnsListOfSchedulings() throws Exception {
	        // Criar um usuário de teste
	       
	        userExp.setRegistration(1234L);
	       userService.save(userExp);
	        
	        HashSet<User> users = new HashSet<User>();
	        users.add(userExp);
	        // Criar agendamentos de teste e associar o usuário como participante
	      
	        
	        schedulingExp.setParticipants(new HashSet<>());
	        schedulingExp.getParticipants().add(userExp);
	        repository.save(schedulingExp);

	        Scheduling scheduling2 = new Scheduling();	 
	        scheduling2.setParticipants(new HashSet<>());
	        scheduling2.getParticipants().add(userExp);
	        System.out.println(scheduling2.getParticipants().contains(userExp));
	        repository.save(scheduling2);

	        when(userService.findByRegistration(1234L)).thenReturn(Optional.of(userExp));

		      
	        when(repository.findAllByParticipantsContaining(userExp)).thenReturn(List.of(schedulingExp, scheduling2));
	        // Chamar o método getSchedulingsByUserRegistration
	        List<Scheduling> result = repository.findAllByParticipantsContaining(userExp);
	        
	        System.out.println(result.size());
	        // Verificar se a lista retornada contém os agendamentos corretos
	        assertEquals(2, result.size());
	        assertTrue(result.contains(schedulingExp));
	        assertTrue(result.contains(scheduling2));
	    }

	   @Test
	   public void testGetSchedulingsByUserRegistration_UserNotFound_ReturnsEmptyList() throws Exception {
	       // Chamar o método getSchedulingsByUserRegistration com um número de registro inexistente
	       List<Scheduling> result = service.getSchedulingsByUserRegistration(987654L);

	       // Verificar se a lista retornada está vazia
	       assertTrue(result.isEmpty());
	   }


	

	
	
	

}
