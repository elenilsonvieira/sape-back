package br.edu.ifpb.dac.sape.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.Objects;
import java.util.Set;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;

import br.edu.ifpb.dac.sape.model.enums.IsPresent;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;


@Table(name = "SCHEDULED_PRACTICE")
@Entity
public class Scheduling implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULED_PRACTICE_ID")
	private Integer id;
	
	@Column(name = "SCHEDULED_DATE", nullable = false)
	private LocalDate scheduledDate;
	
	@Column(name = "SCHEDULED_START_TIME", nullable = false)
	private LocalTime scheduledStartTime;
	
	@Column(name = "SCHEDULED_FINISH_TIME", nullable = false)
	private LocalTime scheduledFinishTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PLACE")
	private Place place;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPORT")
	private Sport sport;
	
	@Column(name = "USER_CREATOR")
	private User creator;
	
	@Enumerated(EnumType.STRING)
	private StatusScheduling status;
	
	@Enumerated(EnumType.STRING)
	private IsPresent willBePresent;
	
	@ManyToMany
	@JoinTable(
			name = "PRACTICE_PARTICIPANTS",
			joinColumns = @JoinColumn(name = "SCHEDULED_PRACTICE_ID"),
			inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private Set<User> participants;
	
	public Scheduling() {

	}

	public Scheduling(LocalDate scheduledDate, LocalTime scheduledStartTime, LocalTime scheduledFinishTime, Place place, Sport sport, StatusScheduling status, IsPresent isPresent) {
		this.scheduledDate = scheduledDate;
		this.scheduledStartTime = scheduledStartTime;
		this.scheduledFinishTime = scheduledFinishTime;
		this.place = place;
		this.sport = sport;
		this.status = status;
		this.willBePresent = isPresent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public LocalTime getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(LocalTime scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public LocalTime getScheduledFinishTime() {
		return scheduledFinishTime;
	}

	public void setScheduledFinishTime(LocalTime scheduledFinishTime) {
		this.scheduledFinishTime = scheduledFinishTime;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public int hashCode() {
		return Objects.hash(id);
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

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scheduling other = (Scheduling) obj;
		return Objects.equals(id, other.id);
	}
	
}
