package br.edu.ifpb.dac.sape.business.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindUserSuapResponse {
    private int count;
    private String next;
    private String previous;
    private List<UserSuapResponse> results;
}