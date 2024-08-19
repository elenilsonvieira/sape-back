package br.edu.ifpb.dac.sape.presentation.dto;

import br.edu.ifpb.dac.sape.model.entity.Role;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import jakarta.validation.constraints.*;

import java.util.List;

public class UserDTO {
	
	private Integer id;
	
	@NotBlank(message = "É obrigatório informar o nome do usuário!")
	@Size(min = 3, max = 255, message = "Nome inválido! Deve possuir mais que 3 caracteres")
	private String name;
	
	@Email(message = "O email deve ser válido!")
	@Pattern(regexp=".+@.+\\..+", message="O email deve ser válido!")
	private String email;
	
	@NotNull(message = "É obrigatório informar a matrícula do usuário!")
	private Long registration;
	
	private List<Role> roles;
	
	private List<Sport> sportsFavorite;
	
	public UserDTO() {
		
	}
	
	public UserDTO(String name, String email, Long registration) {
		this.name = name;
		this.email = email;
		this.registration = registration;
	}
	
	public UserDTO(Integer id, String name, String email, Long registration) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.registration = registration;
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
		this.name = name.trim();
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getRegistration() {
		return registration;
	}
	
	public void setRegistration(Long registration) {
		this.registration = registration;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Sport> getSportsFavorite() {
		return sportsFavorite;
	}

	public void setSportsFavorite(List<Sport> sportsFavorite) {
		this.sportsFavorite = sportsFavorite;
	}
	
	public String toString() {
		String string = "id: " + id +
				", name: " + name+
				", registration: "  + registration;
		
		return string;
	}
}
