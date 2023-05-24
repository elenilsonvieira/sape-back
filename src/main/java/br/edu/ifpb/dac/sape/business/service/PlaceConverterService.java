package br.edu.ifpb.dac.sape.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.presentation.dto.PlaceDTO;

@Service
public class PlaceConverterService {

	public Place dtoToPlace(PlaceDTO dto) {
		if (dto != null) {
			Place entity = new Place(dto.getId(), dto.getName(), dto.getReference(), dto.getMaximumCapacityParticipants(), dto.isPublic(),dto.getResponsibles());
			
			return entity;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public PlaceDTO placeToDto(Place entity) {
		if (entity != null) {
			PlaceDTO dto = new PlaceDTO(entity.getId(), entity.getName(), entity.getReference(), entity.getMaximumCapacityParticipants(), entity.isPublic(),entity.getResponsibles());
			
			return dto;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}

	public List<Place> dtosToPlaces(List<PlaceDTO> dtoList) {
		
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
