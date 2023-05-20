package br.edu.ifpb.dac.sape.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.sape.business.service.UserConverterService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.controller.UserController;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;


public class UserControllerTest {

	@InjectMocks
	private static UserController controller;
	private static UserConverterService userConverter;
	@InjectMocks
	private static UserService service;
	@Mock
	private static UserRepository repository;
	@Captor
	private ArgumentCaptor<User> captor;

	private static UserDTO exUserDto;
	private static User exUser;
	private ResponseEntity<?> resp;

	private Set<ConstraintViolation<UserDTO>> violations;
	private static Validator validator;

	@BeforeAll
	public static void setup() {
		service = new UserService();
		controller = new UserController();
		userConverter = new UserConverterService();
		ReflectionTestUtils.setField(service, "userRepository", repository);
		ReflectionTestUtils.setField(controller, "userService", service);
		ReflectionTestUtils.setField(controller, "converterService", userConverter);

		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);

		exUserDto = new UserDTO();
		exUserDto.setName("Tamires Xavier");
		exUserDto.setEmail("tamiresxavier@gmail.com");
		exUserDto.setRegistration(Long.valueOf(123456789));
		exUserDto.setId(1);
		

		exUser = userConverter.dtoToUser(exUserDto);
	}

	@Test
	public void saveValidFields() { // save valid - compare fields of Dto and Entity; 201 and body

		resp = controller.save(exUserDto);

		verify(repository).save(captor.capture());

		User captured = captor.getValue();

		assertAll("Verify if atributes between Dto an Entity are equals",
				() -> assertEquals(exUserDto.getName(), captured.getName()),
				() -> assertEquals(exUserDto.getEmail(), captured.getEmail()),
				() -> assertEquals(exUserDto.getRegistration(), captured.getRegistration()));

		when(repository.save(any(User.class))).thenReturn(exUser);

		resp = controller.save(exUserDto);

		assertAll("HttpStatus and body", () -> assertEquals(HttpStatus.CREATED, resp.getStatusCode()),
				() -> assertEquals(UserDTO.class, resp.getBody().getClass()),
				() -> assertNotEquals(exUserDto, resp.getBody()));
	}

	@ParameterizedTest
	@ValueSource(strings = { "a", " 12   ", " \n   ", "   ", " bc \t" })
	public void saveInvalidNameJSON(String name) { // save invalid - field name invalid - JSON -> DTO
		try {
			exUserDto.setName(name);

			resp = controller.save(verifyViolations(exUserDto, "name"));

			fail();

		} catch (Exception e) {
			String error01 = "É obrigatório informar o nome do usuário!";
			String error02 = "Nome inválido! Deve possuir mais que 3 caracteres";

			if (!(e.getMessage().equals(error01) || e.getMessage().equals(error02))) {
				fail();
			}
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " \n  ", " \n \t", "null" }) // save invalid - name blanck or null
	public void saveInvalidNameAfeterJson(String name) {
		if (name.equals("null")) {
			UserDTO novo = new UserDTO(null, "tamiresx@gmail.com", Long.valueOf(123456)); // name null - barred in UserService
			resp = controller.save(novo);
		} else {
			exUserDto.setName(name); // name is blanck - barred in UserService
			resp = controller.save(exUserDto);
		}
		assertEquals("Não foi possível usar save, o campo nome está faltando!", resp.getBody());
	}

	@Test
	public void saveInvalidNull() { // save invalid - obj is null

		resp = controller.save(null); // barred in ConverterService

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Não foi possível converter pois o objeto é nulo", resp.getBody());
	}

	@Test
	public void saveInvalidRegistration() { // save invalid - user's registration already exists in DB
		when(repository.existsByRegistration(anyLong())).thenReturn(true);

		resp = controller.save(exUserDto);

		assertEquals("Já existe um usuário com matrícula 123456789", resp.getBody());
	}

	@Test
	public void updateValid() { // update valid - HttpStatus ok and body
		when(repository.existsById(anyInt())).thenReturn(true); // userService
		when(repository.existsByRegistration(Long.valueOf(123456789))).thenReturn(true); // userService
		when(repository.findByRegistration(Long.valueOf(123456789))).thenReturn(Optional.of(exUser)); // userService
		when(repository.save(any(User.class))).thenReturn(exUser);

		resp = controller.update(1, exUserDto);

		assertAll("HttpStatus and body - Update", () -> assertEquals(HttpStatus.OK, resp.getStatusCode()),
				() -> assertEquals(UserDTO.class, resp.getBody().getClass()),
				() -> assertNotEquals(exUserDto, resp.getBody()));
	}

	@Test
	public void updateInvalidDtoNull() { // update invalid - obj null

		resp = controller.update(1, null); // controller

		String error = " is null"; // final message of a NullPointException
		System.out.println(resp.getBody().toString());
		assertTrue(resp.getBody().toString().contains(error));
	}

	@ParameterizedTest
	@ValueSource(strings = { "    ", "\n  ", "  \t  \n \t ", "null" })
	public void updateInvalidName(String name) { // update invalid - Dto's name is blanck or null
		if (!name.equals("null")) {

			exUserDto.setName(name);

			resp = controller.update(1, exUserDto);
			assertEquals("Não foi possível usar update, o campo nome está faltando!", resp.getBody());
		} else {
			resp = controller.update(1, new UserDTO(null, "example@gmail.com", Long.valueOf(13579)));
			assertEquals("Não foi possível usar update, o campo nome está faltando!", resp.getBody());
		}
	}

	@Test
	public void updateInvalidIdNotFound() { // update invalid - Dto's Id not found in DB
		when(repository.existsById(anyInt())).thenReturn(false);

		resp = controller.update(12, exUserDto);

		assertEquals("Não foi encontrado usuário com id 12", resp.getBody());
	}

	@Test
	public void updateInvalidIdNotMatch() { // update invalid - dto's id in DB not match (same registration)
		User user = new User(2, "Afonso", "affff@gmail.com", Long.valueOf(123456789));

		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.existsByRegistration(anyLong())).thenReturn(true);
		when(repository.findByRegistration(Long.valueOf(123456789))).thenReturn(Optional.of(user));

		resp = controller.update(12, exUserDto);

		assertEquals("Já existe um usuário com matrícula 123456789", resp.getBody());
	}

	@Test
	public void deleteValid() { // delete valid - Htttp and body
		when(repository.existsById(1)).thenReturn(true);

		resp = controller.delete(1);

		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
		assertFalse(resp.hasBody());
	}

	@Test
	public void deleteInvalidIdNull() { // delete invalid - the id is null
		resp = controller.delete(null);

		assertEquals("Não foi possível usar delete, o campo id está faltando!", resp.getBody());
	}

	@Test
	public void deleteInvalidIdNotFound() { // delete invalid - id not found in DB
		when(repository.existsById(9)).thenReturn(false);

		resp = controller.delete(9);

		assertEquals("Não foi encontrado usuário com id 9", resp.getBody());
	}

	@Test
	public void findByIDValid() { // findByID valid - Http and body
		when(repository.existsById(1)).thenReturn(true);
		when(repository.getById(1)).thenReturn(exUser);

		resp = controller.findById(1);

		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(UserDTO.class, resp.getBody().getClass());
	}

	@Test
	public void findByIDInvalidNotFound() { // findByID invalid - not found in DB
		when(repository.existsById(9)).thenReturn(false);
		resp = controller.findById(9);

		assertEquals("Não foi encontrado usuário com id 9", resp.getBody());
	}

	@Test
	public void findByIDInvalidNull() { // findByID invalid - id is null
		resp = controller.findById(null);

		assertEquals("Não foi encontrado usuário com id null", resp.getBody());
	}

	@Test
	public void getAllValidWithUsers() { // getAll valid - have User on DB
		User user = new User(2, "Afonso", "affff@gmail.com", Long.valueOf(123456710));
		List<User> list = new ArrayList<>();
		list.add(user);
		list.add(exUser);

		when(repository.findAll()).thenReturn(list);
		resp = controller.getAll();

		assertEquals(HttpStatus.OK, resp.getStatusCode());
	}

	@Test
	public void getAllValidWithoutUsers() { // getAll valid - have no User in DB
		List<User> list = new ArrayList<>();

		when(repository.findAll()).thenReturn(list);
		resp = controller.getAll();
		
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(ArrayList.class, resp.getBody().getClass());
	}
	
	// is the "mock" of Http body request
	private @Valid UserDTO verifyViolations(UserDTO exUserDto2, String field) throws Exception { 
		violations = validator.validateProperty(exUserDto2, field);
		if (violations.size() == 0) {
			return exUserDto2;
		}
		throw new Exception(violations.stream().findFirst().get().getMessage());
	}
	

}
