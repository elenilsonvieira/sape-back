package br.edu.ifpb.dac.sape.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulingControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAll() {
        // Criar alguns objetos de teste e salvá-los no banco de dados
        Scheduling scheduling1 = new Scheduling();
        Scheduling scheduling2 = new Scheduling();
        schedulingRepository.save(scheduling1);
        schedulingRepository.save(scheduling2);

        // Fazer a chamada GET /scheduling
        ResponseEntity<SchedulingDTO[]> response = restTemplate.getForEntity("/scheduling", SchedulingDTO[].class);

        // Verificar o status da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar se a lista de agendamentos retornada não está vazia
        SchedulingDTO[] schedulingDTOs = response.getBody();
        assertNotNull(schedulingDTOs);
        assertEquals(2, schedulingDTOs.length);
    }

    @Test
    public void testGetById() {
        // Criar um objeto de teste e salvá-lo no banco de dados
        Scheduling scheduling = new Scheduling();
        schedulingRepository.save(scheduling);

        // Fazer a chamada GET /scheduling/{id}
        ResponseEntity<SchedulingDTO> response = restTemplate.getForEntity("/scheduling/{id}", SchedulingDTO.class, scheduling.getId());

        // Verificar o status da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar se o agendamento retornado é o correto
        SchedulingDTO schedulingDTO = response.getBody();
        assertNotNull(schedulingDTO);
        assertEquals(scheduling.getId(), schedulingDTO.getId());
    }

    @Test
    public void testSave() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Criar um objeto de teste
        SchedulingDTO schedulingDTO = new SchedulingDTO();
        schedulingDTO.setId(1);

        // Fazer a chamada POST /scheduling
        ResponseEntity<SchedulingDTO> response = restTemplate.postForEntity("/scheduling", schedulingDTO, SchedulingDTO.class);

        // Verificar o status da resposta
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verificar se o agendamento retornado é o correto
        SchedulingDTO savedSchedulingDTO = response.getBody();
        assertNotNull(savedSchedulingDTO);
        assertEquals(schedulingDTO.getId(), savedSchedulingDTO.getId());

        // Verificar se o agendamento foi salvo no banco de dados
        Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
        assertNotNull(savedScheduling);
        assertEquals(savedSchedulingDTO.getId(), savedScheduling.getId());
    }

    @Test
    public void testDelete() {
        // Criar um objeto de teste e salvá-lo no banco de dados
        Scheduling scheduling = new Scheduling();
        schedulingRepository.save(scheduling);

        // Fazer a chamada DELETE /scheduling/{id}
        ResponseEntity<Void> response = restTemplate.exchange("/scheduling/{id}", HttpMethod.DELETE, null, Void.class, scheduling.getId());

        // Verificar o status da resposta
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verificar se o agendamento foi removido do banco de dados
        assertFalse(schedulingRepository.existsById(scheduling.getId()));
    }
}