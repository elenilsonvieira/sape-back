package br.edu.ifpb.dac.ssp.model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Table(name = "PRACTICE_PLACE", uniqueConstraints = {@UniqueConstraint(columnNames = {"PLACE_NAME"})})
@Entity
public class Place implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLACE_ID")
	private Integer id;
	
	@Column(name = "PLACE_NAME", nullable = false)
	private String name;
	
	@Column(name = "PLACE_REFERENCE", nullable = true)
	private String reference;
	
	@Column(name = "PLACE_MAXIMUM_CAPACITY_PEOPLE", nullable = true)
	private int maximumCapacityParticipants;
	
	@Column(name = "PLACE_IS_PUBLIC", nullable = false)
	private boolean isPublic;
	
	@Column(name = "RESPONSIBLE_OF_THE_LOCATION", nullable = false)
	private String nameResponsible;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_PLACE")
	private Set<Scheduling> setScheduling;

	public Place() {
	
	}

	public Place(Integer id, String name, String reference, int maximumCapacityParticipants, boolean isPublic,String nameResponsible) {
		this.id = id;
		this.name = name;
		this.reference = reference;
		this.maximumCapacityParticipants = maximumCapacityParticipants;
		this.isPublic = isPublic;
		this.nameResponsible = nameResponsible;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getMaximumCapacityParticipants() {
		return maximumCapacityParticipants;
	}

	public void setMaximumCapacityParticipants(int maximumCapacityParticipants) {
		this.maximumCapacityParticipants = maximumCapacityParticipants;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	

	public String getNameResponsible() {
		return nameResponsible;
	}

	public void setNameResponsible(String nameResponsible) {
		this.nameResponsible = nameResponsible;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		return Objects.equals(id, other.id);
	}
}
