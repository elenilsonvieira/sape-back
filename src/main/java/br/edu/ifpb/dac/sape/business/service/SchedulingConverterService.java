package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.enums.IsPresent;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingConverterService {
	
	@Autowired 
	private DateConverterService dateConverter;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private SportService sportService;
	
	@Autowired
	private UserService userService;
	
	public Scheduling dtoToScheduling(SchedulingDTO dto) throws Exception {
		if (dto != null) {
			Scheduling entity = new Scheduling();
			
			entity.setId(dto.getId());
			entity.setScheduledDate(dateConverter.stringToDate(dto.getScheduledDate()));
			entity.setScheduledStartTime(dateConverter.stringToTime(dto.getScheduledStartTime()));
			entity.setScheduledFinishTime(dateConverter.stringToTime(dto.getScheduledFinishTime()));
			entity.setPlace(placeService.findById(dto.getPlaceId()));
			entity.setSport(sportService.findById(dto.getSportId()));
			entity.setCreator(userService.findByRegistration(dto.getCreator()));
			
			if(entity.getPlace().isPublic() == true) {
				entity.setStatus(StatusScheduling.CONFIRMED);
			}else {
				entity.setStatus(StatusScheduling.PENDING);
			}
			
			entity.setWillBePresent(dto.getWillBePresent());
			
			
			return entity;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public SchedulingDTO schedulingToDto(Scheduling entity) throws Exception {
		if (entity != null) {
			SchedulingDTO dto = new SchedulingDTO();
			
			dto.setId(entity.getId());
			dto.setScheduledDate(dateConverter.dateToString(entity.getScheduledDate()));
			dto.setScheduledStartTime(dateConverter.timeToString(entity.getScheduledStartTime()));
			dto.setScheduledFinishTime(dateConverter.timeToString(entity.getScheduledFinishTime()));
			dto.setPlaceId(entity.getPlace().getId());
			dto.setSportId(entity.getSport().getId());
			dto.setCreator(entity.getCreator().getRegistration());
			dto.getStatus();
			dto.setStatus(entity.getStatus());
			dto.setWillBePresent(IsPresent.YES);
			dto.setTitle(entity.getSport().getName());
			dto.setLocation(entity.getPlace().getName());
			dto.setStart(entity.getScheduledDate() +"T"+ entity.getScheduledStartTime()+":00");
			dto.setEnd(entity.getScheduledDate() +"T"+ entity.getScheduledFinishTime()+":00");
			
			
			return dto;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public List<Scheduling> dtosToScheduling(List<SchedulingDTO> dtoList) throws Exception {
		
		if (dtoList != null ) {
			List<Scheduling> entityList = new ArrayList<>();
			
			Scheduling entity = null;
			
			if (!dtoList.isEmpty()) {
				for (SchedulingDTO dto: dtoList) {
					entity = dtoToScheduling(dto);
					entityList.add(entity);
				}
			}
			
			return entityList;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}
	
	public List<SchedulingDTO> schedulingToDtos(List<Scheduling> entityList) throws Exception {
		if (entityList != null) {
			List<SchedulingDTO> dtoList = new ArrayList<>();
			
			SchedulingDTO dto = null;
			
			if (!entityList.isEmpty()) {
				for (Scheduling scheduling: entityList) {
					dto = schedulingToDto(scheduling);
					dtoList.add(dto);
				}
			}
			
			return dtoList;
		}
		
		throw new IllegalArgumentException("Não foi possível converter pois o objeto é nulo");
	}

	public Scheduling dtoRequestToSchedulinng(Integer placeId, Integer sportId, String date) throws Exception {
		Scheduling entity = new Scheduling();
		
		if(date != null) {
			entity.setScheduledDate(dateConverter.stringToDate(date));
		}
		if(placeId != null) {
			entity.setPlace(placeService.findById(placeId));
		}
		if(sportId != null) {
			entity.setSport(sportService.findById(sportId));
		}
		
		return entity;
	}
	
}
