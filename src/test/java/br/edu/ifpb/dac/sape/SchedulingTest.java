package br.edu.ifpb.dac.sape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;

public class SchedulingTest {
	
	private Scheduling scheduling;
	Place place = mock(Place.class);
	Sport sport = mock(Sport.class);
	
	@BeforeEach
	void setup() {
		scheduling = new Scheduling();
		scheduling.setPlace(place);
		scheduling.setSport(sport);
		scheduling.setStatus(StatusScheduling.EM_ANDAMENTO);
	}
	
	@DisplayName("Datas validas, Maiores que o a data atual")
	@Test
	void validarDateMaiores() {
		scheduling.setScheduledDate(LocalDate.now().plusDays(1));
		assertEquals(true, scheduling.getScheduledDate().isAfter(LocalDate.now()));
		
	}
	
	@DisplayName("Datas validas igual a data atual")
	@Test
	void validarDateIgual() {
		scheduling.setScheduledDate(LocalDate.now());
		assertEquals(true, scheduling.getScheduledDate().isEqual(LocalDate.now()));	
	}
	
	@DisplayName("Datas invalidas, menor que a data atual")
	@Test
	void validarDateMenor() {
		scheduling.setScheduledDate(LocalDate.now().plusDays(-1));
		assertEquals(true, scheduling.getScheduledDate().isBefore(LocalDate.now()));	
	}
	
	
	@DisplayName("horarios validos entre 07:00 ás 11:00")
	@Test
	void validarHorarioManhaInicio() {
		scheduling.setScheduledStartTime(LocalTime.of(07, 00));
		assertEquals(true, scheduling.getScheduledStartTime().
				equals(LocalTime.of(07, 00)));	
		
		scheduling.setScheduledStartTime(LocalTime.of(11, 00));
		assertEquals(true,scheduling.getScheduledStartTime().
				isBefore(LocalTime.of(12, 00)));
	}
	
	@DisplayName("horarios validas entre 13:00 ás 21:00")
	@Test
	void validarHorarioNoiteInicio() {
		scheduling.setScheduledStartTime(LocalTime.of(13, 00));
		assertEquals(true, scheduling.getScheduledStartTime().
				equals(LocalTime.of(13, 00)));	
		
		scheduling.setScheduledStartTime(LocalTime.of(21, 00));
		assertEquals(true, scheduling.getScheduledStartTime()
				.isBefore(LocalTime.of(22, 00)));
	}
	
	@DisplayName("horario limite para o horario final do agendamento")
	@Test
	void validarHorarioManhaFinal() {	
		
		scheduling.setScheduledFinishTime(LocalTime.of(12, 00));
		assertEquals(true, scheduling.getScheduledFinishTime()
				.equals(LocalTime.of(12, 00)));
	}
	
	@DisplayName("horario final valido 22:00")
	@Test
	void validarHorarioNoiteFinal() {	
		
		scheduling.setScheduledFinishTime(LocalTime.of(22, 00));
		assertEquals(true, scheduling.getScheduledFinishTime()
				.equals(LocalTime.of(22, 00)));
	}
	
	@DisplayName("validar horario inicial menor que horario final")
	@Test
	void validarHorarioinicialFinal() {	
		
		scheduling.setScheduledStartTime(LocalTime.of(07, 00));
		scheduling.setScheduledFinishTime(LocalTime.of(10, 00));
		assertEquals(true, scheduling.getScheduledStartTime()
				.isBefore(scheduling.getScheduledFinishTime()));
	}
	
}
