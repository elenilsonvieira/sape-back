package br.edu.ifpb.dac.sape.business.client.config;

import br.edu.ifpb.dac.sape.presentation.exception.SuapClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public RuntimeException decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        switch (status) {
            case UNAUTHORIZED:
                return new SuapClientException("Usuário ou senha incorreto.");
            case TOO_MANY_REQUESTS:
                return new SuapClientException("Tentativas excessivas de login detectadas. Aguarde seis minutos e tente novamente.");
            default:
                return new SuapClientException("Falha ao conectar ao SUAP. Tente novamente mais tarde.");
        }
    }

}