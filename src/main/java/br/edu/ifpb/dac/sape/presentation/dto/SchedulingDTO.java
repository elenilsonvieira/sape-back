package br.edu.ifpb.dac.sape.presentation.dto;

import br.edu.ifpb.dac.sape.model.enums.IsPresent;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;
import java.util.Objects;

public class SchedulingDTO {
	
	private Integer id;
	
	@NotBlank(message = "É obrigatório informar a data da prática!")
	@Pattern(regexp = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$", message = "A data deve ter formato 'yyyy-MM-dd'")
	private String scheduledDate;
	
	@NotBlank(message = "É obrigatório informar o horário de início da prática!")
	@Pattern(regexp = "^\\d\\d:\\d\\d$", message = "O horário de início deve ter formato 'HH:mm'")
	private String scheduledStartTime;
	
	@NotBlank(message = "É obrigatório informar o horário de término da prática!")
	@Pattern(regexp = "^\\d\\d:\\d\\d$", message = "O horário de término deve ter formato 'HH:mm'")
	private String scheduledFinishTime;
	
	@NotNull(message = "O id do local não pode ser nulo!")
	private Integer placeId;
	
	@NotNull(message = "O id do esporte não pode ser nulo!")
	private Integer sportId;
	
	private Long creator;
	
	private StatusScheduling status;
	
	private IsPresent willBePresent;
	
	private String title;
	private String location;
	private String start;
	private String end;
	
	public SchedulingDTO() {
	}
	
	public SchedulingDTO(Integer id, String scheduledDate, String scheduledStartTime, String scheduledFinishTime, LocalTime startTime,  
			Integer placeId, Integer sportId,Long creator, StatusScheduling status,IsPresent isPresent) {
		this.id = id;
		this.scheduledDate = scheduledDate;
		this.scheduledStartTime = scheduledStartTime;
		this.scheduledFinishTime = scheduledFinishTime;
		this.placeId = placeId;
		this.sportId = sportId;
		this.creator = creator;
		this.status = status;
		this.willBePresent =  isPresent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public String getScheduledFinishTime() {
		return scheduledFinishTime;
	}

	public void setScheduledFinishTime(String scheduledFinishTime) {
		this.scheduledFinishTime = scheduledFinishTime;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public Integer getSportId() {
		return sportId;
	}

	public void setSportId(Integer sportId) {
		this.sportId = sportId;
	}
	
	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public StatusScheduling getStatus() {
		return status;
	}

	public void setStatus(StatusScheduling status) {
		this.status = status;
	}
	
	public IsPresent getWillBePresent() {
		return willBePresent;
	}

	public void setWillBePresent(IsPresent willBePresent) {
		this.willBePresent = willBePresent;
	}
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, placeId, scheduledDate, scheduledFinishTime, scheduledStartTime, sportId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulingDTO other = (SchedulingDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(placeId, other.placeId)
				&& Objects.equals(scheduledDate, other.scheduledDate)
				&& Objects.equals(scheduledFinishTime, other.scheduledFinishTime)
				&& Objects.equals(scheduledStartTime, other.scheduledStartTime)
				&& Objects.equals(sportId, other.sportId);
	}
	
}
