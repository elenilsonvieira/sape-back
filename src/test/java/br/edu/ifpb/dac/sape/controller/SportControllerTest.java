package br.edu.ifpb.dac.sape.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.sape.business.service.SportConverterService;
import br.edu.ifpb.dac.sape.business.service.SportService;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.presentation.controller.SportController;
import br.edu.ifpb.dac.sape.presentation.dto.SportDTO;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectNotFoundException;


public class SportControllerTest {

	@InjectMocks
	private SportController controller;
	@Mock
	private SportService service;
	@Mock
	private SportRepository repository;
	@Captor
	private ArgumentCaptor<Sport> capPlace;
	private SportConverterService converter = new SportConverterService();
	private SportDTO exDto;
	private Sport exSport;
	private ResponseEntity respEntity;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(service, "sportRepository", repository);
		ReflectionTestUtils.setField(controller, "sportService",service);
		ReflectionTestUtils.setField(controller, "converterService", converter);
		
		exSport = new Sport();
		exSport.setId(1);
		exSport.setName("Voleibol");
		
		exDto = new SportDTO();
		exDto.setId(1);
		exDto.setName("Voleibol");
	}
	
//	@Test
//	public void testAddSportsFavoriteSuccess() throws Exception {
//	
//		Sport sport = new Sport(1,"futebol");
//		
//	    respEntity = (ResponseEntity) controller.addSportsFavorite(sport.getId(), 1);
//
//	    assertEquals(HttpStatus.NO_CONTENT, respEntity.getStatusCode());
//	}
//	
//	@Test
//	public void addSportsFavorite_NullParameters_ShouldReturnBadRequest() {
//	    Integer sportId = null;
//	    Integer userId = null;
//
//	    respEntity = controller.addSportsFavorite(sportId, userId);
//
//	    assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode());
//	}
//	
//	@Test
//	@Disabled
//	public void addSportFavorite_nameNull() { 
//		Sport sport=new Sport(1,"");
//	
//		respEntity=controller.addSportsFavorite(sport.getId(), 1);
//		assertEquals(HttpStatus.BAD_REQUEST,respEntity.getStatusCode());
//	}
//	
//	@Test
//	@Disabled
//	public void addSportsFavorite_nullSport() { 
//		
//		User user=new User();
//		user.setId(1);
//		
//		respEntity=controller.addSportsFavorite(1, user.getId());
//		
//		
//		assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode());
//	
//	}
//	
//	@Test
//	public void addSportsFavorite_EsporteNaoEncontrado() throws Exception {
//	  
//	  Integer sportId = -1;
//	  Integer userId = 1;
//	  when(service.findById(sportId)).thenReturn(null);
//
//	  
//	  ResponseEntity response = controller.addSportsFavorite(sportId, userId);
//	  assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	  assertEquals("o esporte ou o usuario não existe", response.getBody());
//	}
//	
	
	
	
	
	
	
	
	
	

	/*
	 * Object in DB have the same atributes (id and name, for example) as the 
	 * DTO of the parameter's method
	 */
	@Test
	public void saveObjectInDb() {
		try {
			respEntity = controller.save(exDto);
			Mockito.verify(service).save(capPlace.capture());
			Sport placeDB = capPlace.getValue();
			assertAll("Test comparing id and name of dto and entity",
					() -> assertEquals(exDto.getId(), placeDB.getId()),
					() -> assertEquals(exDto.getName(), placeDB.getName())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveStatusCreated() {
		
		try {
			Mockito.when(service.save(Mockito.any())).thenReturn(exSport);
			respEntity = controller.save(exDto);
			
			assertEquals(HttpStatus.CREATED, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Disabled
	public void saveSomeAtributeNotMustBeShown() {
		// verify business rule in the ConverterService
	}
	
	@Test
	public void saveFailWithoutName() {
		exDto.setName(null);
		try {
			Mockito.when(service.save(Mockito.any())).thenCallRealMethod();
			respEntity = controller.save(exDto);
			String sResp = String.valueOf(respEntity.getBody()); // Exception is throwed and passed to de ResponseEntity 
			
			assertAll("Test message error in body and https",
					() -> assertTrue(sResp.contains("campo nome está faltando") && sResp.contains("save")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateInDB() {
		try {
			respEntity = controller.update(1,exDto);
			Mockito.verify(service).update(capPlace.capture());
			Sport sportDB = capPlace.getValue();
			
			assertEquals(exDto.getId(), sportDB.getId());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void updateFailIdNotFounded() {
		try {
			Mockito.when(service.update(exSport)).thenThrow(new ObjectNotFoundException("esporte", "id", exSport.getId()));
			respEntity = controller.update(1, exDto); // body contains message error
			String s = String.valueOf(respEntity.getBody());
			
			assertAll("",
					() -> assertTrue(s.contains("Não foi encontrado esporte com id 1")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateHttpsStatusOK() {
		try {
			Mockito.when(service.update(Mockito.any())).thenReturn(exSport);
			respEntity = controller.update(1,exDto);
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Disabled
	public void updateSomeAtributeNotMustBeShown() {
		// verify business rule in the ConverterService
	}
	
	@Test
	public void deleteHttpStatusAndBody() {
		respEntity = controller.delete(1);
		assertAll("Test https and body",
				() -> assertEquals(HttpStatus.NO_CONTENT, respEntity.getStatusCode()),
				() -> assertEquals(null, respEntity.getBody())
		);
	}
	
	@Test
	public void findByIdHttpStatus() {
		try {
			Mockito.when(service.findById(1)).thenReturn(exSport);
			respEntity = controller.findById(1);
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findByIdNotFounded() {
		try {
			Mockito.when(service.findById(1)).thenCallRealMethod();
			Mockito.when(repository.existsById(1)).thenReturn(false);
			respEntity = controller.findById(1); // with message error of ObjectnotFoundException
			
			assertAll("Test when id is not founded",
					() -> assertTrue(respEntity.getBody().equals("Não foi encontrado esporte com id 1")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void getAllHttpStatusOK() {
		try {
			List<Sport> list = new ArrayList<>();
			for(int i = 1; i < 4; i++) {
				String nameComp = String.valueOf(i);
				exSport = new Sport();
				exSport.setId(i);
				exSport.setName("lugar" + nameComp);
				list.add(exSport);
			}
			Mockito.when(service.findAll()).thenReturn(list);
			respEntity = controller.getAll();
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
