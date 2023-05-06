package br.edu.ifpb.dac.sape.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {
	
	public List<Scheduling> findAllByPlaceId(Integer id);
	public List<Scheduling> findAllByPlaceIdAndScheduledDate(Integer placeId, LocalDate scheduledDate);
	public List<Scheduling> findAllByPlaceIdAndSportId(Integer placeId, Integer sportId);
	public List<Scheduling> findAllBySportId(Integer id);
	public List<Scheduling> findAllBySportIdAndScheduledDate(Integer sportId, LocalDate scheduledDate);
	public List<Scheduling> findAllByStatus(StatusScheduling pendente);
	
	
} 
