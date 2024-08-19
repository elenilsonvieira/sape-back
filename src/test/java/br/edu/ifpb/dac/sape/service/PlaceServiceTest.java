package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
import br.edu.ifpb.dac.sape.presentation.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

public class PlaceServiceTest {

	@Mock
	private PlaceRepository repository;
	
	@InjectMocks
	@Spy
	private PlaceService service;
	
	private static Place entity;
	
	@BeforeEach
	public void beforeEach() {
		System.out.println("Initializing classes...");
		
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(service, "placeRepository", repository);
		
		System.out.println("Setting attributtes for Place...");
		entity = new Place();
		entity.setId(1);
		entity.setName("Ginásio");
		entity.setPublic(false);
		entity.setReference("Perto do estacionamento");
		entity.setMaximumCapacityParticipants(80);
	}
	
	@Test
	public void testFindByIdValid() {
		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.getById(anyInt())).thenReturn(entity);
		
		assertDoesNotThrow(() -> service.findById(1));
		
		verify(repository).existsById(anyInt());
		verify(repository).getById(anyInt());
	}
	
	@Test
	public void testFindByIdThrowsObjectNotFoundException() {
		// Testing findById by passing an invalid id...
		when(repository.existsById(anyInt())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(25));
		assertEquals("Não foi encontrado local com id 25", exception.getMessage());
	}
	
	@Test
	public void testFindByNameValid() {
		when(repository.existsByName(anyString())).thenReturn(true);
		
		assertDoesNotThrow(() -> service.findByName("Ginásio"));
		
		verify(repository).existsByName(anyString());
		verify(repository).findByName(anyString());
	}
	
	@Test
	public void testFindByNameThrowsMissingFieldException() {
		// Testing findByName by passing a null value...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.findByName(null));
		assertEquals("Não foi possível concluir a ação, o campo nome está faltando!", exception.getMessage());
	}
	
	@Test
	public void testFindByNameThrowsObjectNotFoundException() {
		// Testing findByName by passing an invalid name...
		when(repository.existsByName(anyString())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findByName("Pátio"));
		assertEquals("Não foi encontrado local com nome Pátio", exception.getMessage());
	}
	
	@Test 
	public void testSaveValid() {
		when(repository.existsByName(anyString())).thenReturn(false);
		when(repository.save(any(Place.class))).thenReturn(entity);
		
		try {
			assertDoesNotThrow(() -> service.save(entity));
			assertEquals(entity, service.save(entity));
			
			verify(repository, times(2)).existsByName(anyString());
			verify(repository, times(2)).save(any(Place.class));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testSaveThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing save by passing a null value for name...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.save(entity));
		assertEquals("Não foi possível usar save, o campo nome está faltando!", exception.getMessage());
	}
	
	@Test
	public void testSaveThrowsObjectAlreadyExistsException() {
		when(repository.existsByName(anyString())).thenReturn(true);
		
		Throwable exception = assertThrows(ObjectAlreadyExistsException.class, () -> service.save(entity));
		assertEquals("Já existe um local com nome Ginásio", exception.getMessage());
	}
	
	@Test
	public void testUpdateValid() {
		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.existsByName(anyString())).thenReturn(false);
		when(repository.save(any(Place.class))).thenReturn(entity);
		
		try {
			assertDoesNotThrow(() -> service.update(entity));
			assertEquals(entity, service.update(entity));
			
			verify(repository, times(2)).existsById(anyInt());
			verify(repository, times(2)).existsByName(anyString());
			verify(repository, times(2)).save(any(Place.class));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testUpdateWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.update(entity));
		assertEquals("Não foi possível usar update, o campo id está faltando!", exception.getMessage());
	}
	
	@Test
	public void testUpdateWithoutNameThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.update(entity));
		assertEquals("Não foi possível usar update, o campo nome está faltando!", exception.getMessage());
	}
	
	@Test
	public void testUpdateThrowsObjectNotFoundException() {
		when(repository.existsById(anyInt())).thenReturn(false);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.update(entity));
		assertEquals("Não foi encontrado local com id 1", exception.getMessage());
	}
	
	@Test
	public void testUpdateThrowsObjectAlreadyExistsException() {
		Place placeMock = mock(Place.class);
		when(placeMock.getId()).thenReturn(2);
		
		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.existsByName(anyString())).thenReturn(true);
		when(repository.findByName(anyString())).thenReturn(Optional.of(placeMock));
		
		Throwable exception = assertThrows(ObjectAlreadyExistsException.class, () -> service.update(entity));
		assertEquals("Já existe um local com nome Ginásio", exception.getMessage());
	}
	
	@Test
	public void testDeleteWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.delete(entity));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exception.getMessage());
	}
	
	@Test
	public void testDeleteThrowsObjectNotFoundException() {
		when(repository.existsById(anyInt())).thenReturn(false);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.delete(entity));
		assertEquals("Não foi encontrado local com id 1", exception.getMessage());
	}
	
	@Test
	public void testDeleteByIdWithoutIdThrowsMissingFieldException() {
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exception.getMessage());
	}
	
	@Test
	public void testDeleteByIdThrowsObjectNotFoundException() {	
		when(repository.existsById(anyInt())).thenReturn(false);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(1));
		assertEquals("Não foi encontrado local com id 1", exception.getMessage());
	}
}
