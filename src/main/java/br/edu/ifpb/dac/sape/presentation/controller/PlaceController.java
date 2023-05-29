package br.edu.ifpb.dac.sape.presentation.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.sape.business.service.PlaceConverterService;
import br.edu.ifpb.dac.sape.business.service.PlaceService;
import br.edu.ifpb.dac.sape.business.service.UserConverterService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.PlaceDTO;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
	
	@Autowired
	private PlaceService placeService;
	@Autowired
	private PlaceConverterService converterService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserConverterService userConverterService;
	
	// Falta organizar o getAll para funcionar com um filtro para name também (utilizando Example)
	
	@GetMapping
	public ResponseEntity getAll() {
		List<Place> entityList = placeService.findAll();
		
		List<PlaceDTO> dtoList = converterService.placesToDtos(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		
		try {
			Place entity = placeService.findById(id);
			
			PlaceDTO dto = converterService.placeToDto(entity);
			
			return ResponseEntity.ok().body(dto);
		
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody @Valid PlaceDTO dto) {
		
		try {
			System.out.println(dto.getResponsible());
			Place entity = converterService.dtoToPlace(dto);
			entity = placeService.save(entity);
			dto = converterService.placeToDto(entity);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid PlaceDTO dto) {
		
		try {
			dto.setId(id);
			Place entity = converterService.dtoToPlace(dto);
			entity = placeService.update(entity);
			dto = converterService.placeToDto(entity);
			
			return ResponseEntity.ok().body(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		
		try {
			placeService.deleteById(id);
			
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/getResponsibles/{placeId}")
	public ResponseEntity getResponsibles(@PathVariable Integer placeId) throws Exception {
	try {
		List<User> entityList = placeService.getResponsibles(placeId);
		
		List<UserDTO> dtoList = userConverterService.usersToDtos(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	} catch (Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
	
	
	@PatchMapping("/{placeId}/addResponsibles/{userRegistration}")
	public ResponseEntity addResponsibles(@PathVariable Integer placeId, @PathVariable Long userRegistration) {
		
		try {
			User user = userService.findByRegistration(userRegistration).orElse(null);
			
			if(user != null) {
				placeService.addResponsibles(placeId, user);
			}
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping("/{placeId}/removeResponsibles/{userId}")
	public ResponseEntity removeResponsibles(@PathVariable Integer placeId, @PathVariable Integer userId) {
		
		try {
			User user = userService.findById(userId);
			
			if(user != null) {
				placeService.removeResponsibles(placeId, user);
			}
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
