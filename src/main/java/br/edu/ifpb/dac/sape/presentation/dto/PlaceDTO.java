package br.edu.ifpb.dac.sape.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public class PlaceDTO {

	private Integer id;
	
	@NotBlank(message = "É obrigatório informar o nome do local!")
	@Pattern(regexp = "^[a-zA-ZÀ-ú0-9\\s]{4,255}$", message = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais")
	private String name;
	
	private String reference;
	
	@Positive(message = "A capacidade de participantes deve ser um valor positivo!")
	@Max(value = 400, message = "O valor máximo para capacidade de participantes é 400!")
	private int maximumCapacityParticipants;
	
	private boolean isPublic;
	
	private Set<br.edu.ifpb.dac.sape.presentation.dto.UserDTO> responsibles;
	
	
	public PlaceDTO() {
		
	}
	
	public PlaceDTO(String name, String reference, int maximumCapacityParticipants, boolean isPublic) {
		this.name = name;
		this.reference = reference;
		this.maximumCapacityParticipants = maximumCapacityParticipants;
		this.isPublic = isPublic;
		
		
	}
	
	public PlaceDTO(Integer id, String name, String reference, int maximumCapacityParticipants, boolean isPublic) {
		this.id = id;
		this.name = name;
		this.reference = reference;
		this.maximumCapacityParticipants = maximumCapacityParticipants;
		this.isPublic = isPublic;
		
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

	public Set<br.edu.ifpb.dac.sape.presentation.dto.UserDTO> getResponsibles() {
		return responsibles;
	}

	public void setResponsibles(Set<br.edu.ifpb.dac.sape.presentation.dto.UserDTO> responsibles) {
		this.responsibles = responsibles;
	}
	
	

//	public UserDTO getResponsible() {
//		return responsible;
//	}
//
//	public void setResponsible(UserDTO responsible) {
//		this.responsible = responsible;
//	}
	
	public String toString() {
		String string = "id: " + id +
				", name: " + name+
				", reference: "  + reference+
				", maximumCapacityParticipants: " + maximumCapacityParticipants + 
				", isPublic: " + isPublic +
				", reponsible: ";
		for(UserDTO u : responsibles) {
			string += "  " + u.getName() + "  id:  " + u.getId(); 
		}
		return string;
	}

}
