package br.edu.ifpb.dac.sape.presentation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.sape.business.service.SportConverterService;
import br.edu.ifpb.dac.sape.business.service.SportService;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.presentation.dto.SportDTO;

@RestController
@RequestMapping("/api/sport")
public class SportController {
	
	@Autowired
	private SportService sportService;
	
	@Autowired
	private SportConverterService converterService;
	
	@GetMapping
	public ResponseEntity getAll() {
		List<Sport> entityList = sportService.findAll();
		
		List<SportDTO> dtoList = converterService.sportsToDtos(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		
		try {
			Sport entity = sportService.findById(id);
			SportDTO dto = converterService.sportToDto(entity);
			
			return ResponseEntity.ok().body(dto);
		
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@RequestBody @Valid SportDTO dto) {
		
		try {
			Sport entity = converterService.dtoToSport(dto);
			entity = sportService.save(entity);
			dto = converterService.sportToDto(entity);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid SportDTO dto) {
		
		try {
			dto.setId(id);
			Sport entity = converterService.dtoToSport(dto);
			entity = sportService.update(entity);
			dto = converterService.sportToDto(entity);
			
			return ResponseEntity.ok().body(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		
		try {
			sportService.deleteById(id);
			
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
//	@PatchMapping("/addSportsFavorite/add/{id}")
//	public ResponseEntity<?> addSportsFavorite(@PathVariable Integer sportId, @RequestBody Integer user_Id) {
//		
//		
//		try {
//	        Sport sport = sportService.findById(sportId);
//	          System.out.println(sportId);
//	          System.out.println(user_Id);
//			if (sportId==null || user_Id==null) {
//				throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
//			}
//			
//			if(sportId<=0 || user_Id<=0) {
//				throw new IllegalArgumentException("o esporte ou o usuario não existe");
//			}
//			sportService.addSportsFavorite(sportId, user_Id);
//			
//			return ResponseEntity.noContent().build();
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
	
	
}
