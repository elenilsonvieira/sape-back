package br.edu.ifpb.dac.sape.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SportDTO {
	
	private Integer id;
	
	@NotBlank(message = "É obrigatório informar o nome do esporte!")
	@Pattern(regexp = "^[a-zA-ZÀ-ú\\s]{3,255}$", message = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais")
	private String name;
	 
	public SportDTO() {
		
	}
	
	public SportDTO(String name) {
		this.name = name;
	}

	public SportDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
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

}
