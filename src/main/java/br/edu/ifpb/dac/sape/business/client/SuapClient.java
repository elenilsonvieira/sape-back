package br.edu.ifpb.dac.sape.business.client;

import br.edu.ifpb.dac.sape.business.client.config.FeignConfiguration;
import br.edu.ifpb.dac.sape.business.client.dto.FindUserSuapResponse;
import br.edu.ifpb.dac.sape.business.client.dto.LoginSuapRequest;
import br.edu.ifpb.dac.sape.business.client.dto.LoginSuapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "suapClient", url = "https://suap.ifpb.edu.br", configuration = FeignConfiguration.class)
public interface SuapClient {
    @PostMapping("/api/jwt/obtain_token/")
    LoginSuapResponse login(@RequestBody LoginSuapRequest loginRequest);
    @GetMapping("/api/recursos-humanos/servidores/v1/")
    FindUserSuapResponse findEmployee(@RequestHeader("Authorization") String token, @RequestParam("search") String username);
    @GetMapping("/api/ensino/alunos/v1/")
    FindUserSuapResponse findStudent(@RequestHeader("Authorization") String token, @RequestParam("search") String username);
}