package br.edu.ifpb.dac.sape.service;

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
	private UserService userService;
	@Autowired
	private SportService sportService;
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
		userRepository.save(exUser);
		
		exSport = new Sport();
		exSport.setId(1);
		exSport.setName("Futebol");
		sporRepository.save(exSport);
		
		exSport2 = new Sport();
		exSport2.setId(2);
		exSport2.setName("Vôlei");
		sportService.save(exSport);
	}
	
	@Test
    public void testRemoveSportsFavorite_SportNoIntheList() throws Exception {

		userService.addSportsFavorite(exUser.getId(), 1);
		
		for (Sport sports : exUser.getSportsFavorite()) {
			
				System.out.println(sports);
			
		}
	}
}
