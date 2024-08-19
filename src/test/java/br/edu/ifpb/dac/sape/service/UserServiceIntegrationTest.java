package br.edu.ifpb.dac.sape.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectAlreadyExistsException;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

	private User exUser;
	private Sport exSport;
	private Sport exSport2;

	@Autowired
	private UserService userService;
	@Autowired
	private SportService sportService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SportRepository sporRepository;

	@BeforeEach
	@Transactional
	public void setUp() throws Exception {

		userRepository.deleteAll();
		sporRepository.deleteAll();

		exUser = new User();



		exUser.setName("Ytallo");
		exUser.setRegistration(111112L);


		userService.save(exUser);




		exSport = new Sport();


		exSport.setName("Ping Pong");
		sportService.save(exSport);


		exSport2 = new Sport();




		exSport2 = new Sport();

		exSport2.setName("Vôlei");
		sportService.save(exSport2);
	}

	@Test
	@Transactional
	public void testRemoveSportsFavorite_Successful() throws Exception {

		exUser.setSportsFavorite(new ArrayList<>());
		exUser.getSportsFavorite().add(exSport);

		int initialSize = exUser.getSportsFavorite().size();

		userService.removeSportsFavorite(exUser.getId(), exSport.getId());

		int finalSize = exUser.getSportsFavorite().size();
		System.out.println(initialSize);
		System.out.println(finalSize);
		assertNotEquals(initialSize, finalSize);
	}

	@Test
	@Transactional
	public void testRemoveSportsFavorite_Unsuccessful() throws Exception {

		exUser.setSportsFavorite(new ArrayList<>());
		exUser.getSportsFavorite().add(exSport);

		int initialSize = exUser.getSportsFavorite().size();

		userService.removeSportsFavorite(exUser.getId(), exSport2.getId());

		int finalSize = exUser.getSportsFavorite().size();

		System.out.println(initialSize);
		System.out.println(finalSize);
		assertEquals(initialSize, finalSize);
	}

	@Test
	@Transactional
	public void testAddSportsFavorite_Successful() throws Exception {

		exUser.setSportsFavorite(new ArrayList<>());

		int initialSize = exUser.getSportsFavorite().size();

		userService.addSportsFavorite(exUser.getId(),exSport.getId());

		int finalSize = exUser.getSportsFavorite().size();

		System.out.println(initialSize);
		System.out.println(finalSize);
		assertNotEquals(initialSize, finalSize);
	}

	@Test
	@Transactional
	public void testAddSportsFavorite_UserNotFound() throws Exception {
		assertThrows(IllegalArgumentException.class,
				() -> userService.addSportsFavorite(exSport.getId(),10));
	}
	@AfterEach
	@Transactional
	public void tearDown() throws Exception {
		userRepository.deleteAll();
		sporRepository.deleteAll();
	}



	@Test
	@Transactional
	public void testRemoveSportsFavorite_SportNoIntheList() throws Exception {

		exUser.setSportsFavorite(new ArrayList<>());
		exUser.getSportsFavorite().add(exSport);

		int initialSize = exUser.getSportsFavorite().size();

		userService.removeSportsFavorite(exUser.getId(), exSport.getId());

		int finalSize = exUser.getSportsFavorite().size();
		System.out.println(initialSize);
		System.out.println(finalSize);
		//assertNotEquals(initialSize, finalSize);
	}


	//A partir daqui ------------------------

	@Test
	@Transactional
	public void testFindAllUsers() {
		List<User> users = userService.findAll();
		assertNotNull(users);
	}

	@Test
	@Transactional
	public void testExistById() throws Exception {
		User returnedUser = userService.findById(exUser.getId());
		assertEquals(exUser.getId(), returnedUser.getId());
	}

	@Test
	@Transactional
	public void testNotExisteId() {
		assertThrows(ObjectNotFoundException.class, () -> userService.findById(26326436));
	}

	@Test
	@Transactional
	public void testFindBySportFavorite() throws Exception {
		userService.addSportsFavorite(exUser.getId(), exSport.getId());

		Set<User> list = userService.findBySportFavorite(exSport);

	}

	@Test
	@Transactional
	public void testFindByName() throws Exception {
		Optional<User> users =  userService.findByName(exUser.getName());

		assertEquals(exUser.getName(), users.get().getName());


		assertThrows(ObjectNotFoundException.class, () -> userService.findByName("vevevrv"));

		assertThrows(MissingFieldException.class, () -> userService.findByName(""));

		assertThrows(MissingFieldException.class, () -> userService.findByName(null));

	}

	@Test
	@Transactional
	public void testLoadUserByUsername() {
		User userTest = (User) userService.loadUserByUsername(exUser.getUsername());
		assertNotNull(userTest);
	}

	@Test
	@Transactional
	public void testUpdateUser() throws Exception {
		exUser.setName("updated");
		User userUpdate = userService.update(exUser);
		assertEquals("updated", userUpdate.getName());
	}


	@Test
	@Transactional
	public void testDeleteByIdAnThrow() throws Exception {
		assertNotNull(exUser);
		userService.deleteById(exUser.getId());

		User user = new User();
		user.setId(null);
		assertThrows(MissingFieldException.class, () -> userService.deleteById(user.getId()));

		assertThrows(ObjectNotFoundException.class, () -> userService.deleteById(4264326));

	}

	@Test
	@Transactional
	public void testDeleteAndThrow() throws Exception {
		userService.delete(exUser);

		User user = new User();
		user.setId(null);
		assertThrows(MissingFieldException.class, () -> userService.delete(user));

		user.setId(23414);
		assertThrows(ObjectNotFoundException.class, () -> userService.delete(user));
	}

	@Test
	@Transactional
	public void testThrowFindByRegistration() {
		assertThrows(MissingFieldException.class, () -> userService.findByRegistration(null));
		assertThrows(ObjectNotFoundException.class, () -> userService.findByRegistration(235464L));

	}

	@Test
	@Transactional
	public void testThrowSavedUser() {

		User user = new User();
		user.setName(null);
		assertThrows(MissingFieldException.class, () -> userService.save(user));

		user.setName("");
		assertThrows(MissingFieldException.class, () -> userService.save(user));

		assertThrows(ObjectAlreadyExistsException.class, () -> userService.save(exUser));
	}

	@Test
	@Transactional
	public void testThrowUpdateUser() throws Exception {
		User user1 = new User();
		user1.setName(null);
		assertThrows(MissingFieldException.class, () -> userService.update(user1));

		user1.setName("");
		assertThrows(MissingFieldException.class, () -> userService.update(user1));

		User user2 = new User();
		assertThrows(MissingFieldException.class, () -> userService.update(user2));

		User user3 = new User();
		user3.setId(324643);
		user3.setName("jose");
		assertThrows(ObjectNotFoundException.class, () -> userService.update(user3));


		User user4 = new User();
		user4.setRegistration(111112L);
		user4.setName("jose");
		user4.setId(234);
		assertThrows(ObjectNotFoundException.class, () -> userService.update(user4));

		User user5 = new User();
		user5.setName("junior");
		user5.setId(null);
		assertThrows(MissingFieldException.class, () -> userService.update(user5));

	}

	@Test
	@Transactional
	public void testTrowsRemoveSportsFavorite() {
		assertThrows(IllegalArgumentException.class, () -> userService.removeSportsFavorite(3414, 1324324));
	}

	@Test
	@Transactional
	public void testThrowLoadUserByUsername() {
		assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("junior"));
	}


}
