package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifpb.dac.sape.business.service.SportService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;

@SpringBootTest
public class UserServiceIntegrationTest {
	
	private static User exUser;
	private static Sport exSport;
	private static Sport exSport2;
	
	@Autowired
	private static UserService userService;
	@Autowired
	private static SportService sportService;
	@Autowired
	private static UserRepository userRepository;
	@Autowired
	private static SportRepository sporRepository;
	
	@BeforeEach
	public void beforeEach() throws Exception{
		userService = new UserService();
		sportService = new SportService();
		
		exUser = new User();
		exUser.setId(1);
		exUser.setName("Ytallo");
		exUser.setRegistration(111111L);
		userService.save(exUser);
		
		exSport = new Sport();
		exSport.setId(1);
		exSport.setName("Futebol");
		sportService.save(exSport);
		
		exSport2 = new Sport();
		exSport2.setId(2);
		exSport2.setName("Vôlei");
		sportService.save(exSport);
	}
	
	@Test
    public void testRemoveSportsFavorite_SportNoIntheList() throws Exception {

		userService.addSportsFavorite(exUser.getId(), 1);
//		when(userRepository.save(exUser)).thenReturn(Optional.of(exUser));
		
		for (Sport sports : exUser.getSportsFavorite()) {
			
				System.out.println(sports);
			
		}
	}
	
//	@Test
//	public void testRemoveSportsFavorite_UserNotFound() {
//		exSport.setId(1);
//		exUser.setId(1);
//		
//		when(repository.findById(exUser.getId())).thenReturn(Optional.empty());
//		
//		assertThrows(IllegalArgumentException.class, 
//                () -> service.removeSportsFavorite(exUser.getId(), exSport.getId()));
//	}
}
