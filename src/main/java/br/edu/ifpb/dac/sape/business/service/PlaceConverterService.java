package br.edu.ifpb.dac.sape.business.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.PlaceDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;

@Service
public class PlaceConverterService {

	@Autowired
	UserService userService;
	
	@Autowired
	UserConverterService userConverterService;
	
	public Place dtoToPlace(PlaceDTO dto) throws Exception {
		if (dto != null) {
			
			Place entity = new Place();
			
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setMaximumCapacityParticipants(dto.getMaximumCapacityParticipants());
			entity.setReference(dto.getReference());
			entity.setPublic(dto.isPublic());
			
			User user = userService.findByRegistration(dto.getResponsible().getRegistration()).orElse(null);
		
			entity.setResponsibles(new HashSet<User>());
			Set<User> setUser = new HashSet<>(entity.getResponsibles());
			
			setUser.add(user);
			entity.setResponsibles(setUser);
			
			
			
			return entity;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public PlaceDTO placeToDto(Place entity) {
		if (entity != null) {
			PlaceDTO dto = new PlaceDTO();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setMaximumCapacityParticipants(entity.getMaximumCapacityParticipants());
			dto.setPublic(entity.isPublic());
			dto.setReference(entity.getReference());
			
			
			
			
			return dto;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}

	public List<Place> dtosToPlaces(List<PlaceDTO> dtoList) throws Exception {
		
		if (dtoList != null) {
			List<Place> entityList = new ArrayList<>();
			
			Place entity = null;
			
			if (!dtoList.isEmpty()) {
				for (PlaceDTO dto: dtoList) {
					entity = dtoToPlace(dto);
					entityList.add(entity);
				}
			}
			
			return entityList;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public List<PlaceDTO> placesToDtos(List<Place> entityList) {
		if (entityList != null) {
			List<PlaceDTO> dtoList = new ArrayList<>();
			
			PlaceDTO dto = null;
			
			if (!entityList.isEmpty()) {
				for (Place place: entityList) {
					dto = placeToDto(place);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
}
