package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.sape.presentation.dto.PlaceDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;

public class PlaceConverterServiceTest {

	private static PlaceConverterService converterService;
	private static Place entity;
	private static PlaceDTO dto;
	private static UserDTO userDTO;
	
	
	@BeforeAll
	public static void setUp() {
		converterService = new PlaceConverterService();
		
		System.out.println("Setting attributtes for entity...");
		entity = new Place();
		entity.setId(1);
		entity.setName("Ginásio");
		entity.setPublic(false);
		entity.setReference("Perto do estacionamento");
		entity.setMaximumCapacityParticipants(80);
		
		userDTO = new UserDTO();  
		userDTO.setId(1);
    	userDTO.setName("igor"); 
    	userDTO.setRegistration(1111L);
		
		System.out.println("Setting attributtes for dto...");
		dto = new PlaceDTO();
		dto.setId(1);
		dto.setName("Ginásio");
		dto.setPublic(false);
		dto.setReference("Perto do estacionamento");
		dto.setMaximumCapacityParticipants(80);
		Set<UserDTO> responsibles = new HashSet<>();
		responsibles.add(userDTO);
		dto.setResponsibles(responsibles);
	}
	
	@Test
	public void testConvertEntityToDto() {
		PlaceDTO dtoConverted = converterService.placeToDto(entity);
		assertAll("Testing comparing dto and entity field by field",
			() -> assertEquals(dtoConverted.getId(), entity.getId()),
			() -> assertEquals(dtoConverted.getName(), entity.getName()),
			() -> assertEquals(dtoConverted.getReference(), entity.getReference()),
			() -> assertEquals(dtoConverted.getMaximumCapacityParticipants(), entity.getMaximumCapacityParticipants()),
			() -> assertEquals(dtoConverted.isPublic(), entity.isPublic())
		);
	}
	
	
	//Quebrado
	@Test
	@Transactional
	public void testConvertDtoToEntity() throws Exception {
		Place entityConverted = converterService.dtoToPlace(dto);
		assertAll("Testing comparing dto and entity field by field",
			() -> assertEquals(entityConverted.getId(), dto.getId()),
			() -> assertEquals(entityConverted.getName(), dto.getName()),
			() -> assertEquals(entityConverted.getReference(), dto.getReference()),
			() -> assertEquals(entityConverted.getMaximumCapacityParticipants(), dto.getMaximumCapacityParticipants()),
			() -> assertEquals(entityConverted.isPublic(), dto.isPublic())
		);
	}
	
	@Test
	public void testConvertEntityToDtoThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.placeToDto(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
	@Test
	public void testConvertDtoToEntityThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoToPlace(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
}
