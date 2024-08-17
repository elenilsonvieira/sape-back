package br.edu.ifpb.dac.sape.business.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginSuapRequest {
    private String username;
    private String password;
}

