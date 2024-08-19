package br.edu.ifpb.dac.sape.business.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSuapResponse {
    @JsonProperty("uuid")
    private String id;
    @JsonProperty("nome")
    private String name;
    @JsonProperty("matricula")
    private String registration;
}
