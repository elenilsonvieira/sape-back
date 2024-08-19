package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.presentation.dto.SportDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SportConverterService {
	
	public Sport dtoToSport(SportDTO dto) {
		if (dto != null) {
			Sport entity = new Sport(dto.getId(), dto.getName());
			
			return entity;
		}
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public SportDTO sportToDto(Sport entity) {
		if (entity != null) {
			SportDTO dto = new SportDTO(entity.getId(), entity.getName());
			
			return dto;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public List<Sport> dtosToSports(List<SportDTO> dtoList) {

		if (dtoList != null) {
			List<Sport> entityList = new ArrayList<>();

			Sport entity = null;

			if (dtoList != null && !dtoList.isEmpty()) {
				for (SportDTO dto : dtoList) {
					entity = dtoToSport(dto);
					entityList.add(entity);
				}
			}

			return entityList;
		}

		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public List<SportDTO> sportsToDtos(List<Sport> entityList) {
		if (entityList != null) {
			List<SportDTO> dtoList = new ArrayList<>();
			
			SportDTO dto = null;
			
			if (!entityList.isEmpty()) {
				for (Sport sport: entityList) {
					dto = sportToDto(sport);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}

}