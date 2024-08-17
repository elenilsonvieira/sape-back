package br.edu.ifpb.dac.sape.model.repository;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {
	
	List<Scheduling> findAllByPlaceId(Integer id);
	List<Scheduling> findAllByPlaceIdAndScheduledDate(Integer placeId, LocalDate scheduledDate);
	List<Scheduling> findAllByPlaceIdAndSportId(Integer placeId, Integer sportId);
	List<Scheduling> findAllBySportId(Integer id);
	List<Scheduling> findAllBySportIdAndScheduledDate(Integer sportId, LocalDate scheduledDate);
	List<Scheduling> findAllByStatus(StatusScheduling status);
	List<Scheduling> findAllByParticipantsContaining(User participant);

} 
