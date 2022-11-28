package br.edu.ifpb.dac.sape;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.sape.business.service.SchedulingService;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.User;

public class SchedulingServiceTest {
	
	@InjectMocks
	private static SchedulingService service;

	@Mock
	private static SchedulingRepository repository;
	
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
	
	@Test
	void saveTest() {
		schedulingExp.setId(2);
		
		when(repository.save(schedulingExp)).thenReturn(schedulingExp);
		assertEquals(schedulingExp, service.save(schedulingExp));
		verify(repository).save(schedulingExp);
		
	}
	
	
	
	
	
	

}
