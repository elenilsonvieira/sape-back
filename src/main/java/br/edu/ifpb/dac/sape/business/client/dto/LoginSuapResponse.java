package br.edu.ifpb.dac.sape.business.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginSuapResponse {

    @JsonProperty("refresh")
    private String refreshToken;

    @JsonProperty("access")
    private String accessToken;
}

