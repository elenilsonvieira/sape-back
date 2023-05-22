package br.edu.ifpb.dac.sape.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
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

import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
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
    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void testGetAll() {
        
    	 User user = new User();
         user.setId(1);
         user.setName("igor");
         user.setEmail("igor@gmail.com");
         user.setRegistration(111L);
         userRepository.save(user);

         Place place = new Place();
         place.setId(2);
         place.setName("quadra");
         place.setPublic(true);
         place.setNameResponsible("fulano");
         placeRepository.save(place);

         Sport sport = new Sport();
         sport.setId(3);
         sport.setName("futebol");
         
         Scheduling scheduling1 = new Scheduling();
         scheduling1.setId(1);
         scheduling1.setCreator(user);
         scheduling1.setPlace(place);
         scheduling1.setSport(sport);
         scheduling1.setScheduledDate(LocalDate.of(2023, 06, 15));
         scheduling1.setScheduledStartTime(LocalTime.of(10, 0));
         scheduling1.setScheduledFinishTime(LocalTime.of(12, 0));
        
        
        Scheduling scheduling2 = new Scheduling();
        scheduling2.setId(2);
        scheduling2.setCreator(user);
        scheduling2.setPlace(place);
        scheduling2.setSport(sport);
        scheduling2.setScheduledDate(LocalDate.of(2023, 07, 15));
        scheduling2.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling2.setScheduledFinishTime(LocalTime.of(12, 0));
        
        
        schedulingRepository.save(scheduling1);
        schedulingRepository.save(scheduling2);

  
        ResponseEntity<SchedulingDTO[]> response = restTemplate.getForEntity("/scheduling", SchedulingDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        SchedulingDTO[] schedulingDTOs = response.getBody();
        assertNotNull(schedulingDTOs);
        assertEquals(2, schedulingDTOs.length);
    }

    @Test
    public void testGetById() {
       
        Scheduling scheduling = new Scheduling();
        schedulingRepository.save(scheduling);

      
        ResponseEntity<SchedulingDTO> response = restTemplate.getForEntity("/scheduling/{id}", SchedulingDTO.class, scheduling.getId());

        
        assertEquals(HttpStatus.OK, response.getStatusCode());

      
        SchedulingDTO schedulingDTO = response.getBody();
        assertNotNull(schedulingDTO);
        assertEquals(scheduling.getId(), schedulingDTO.getId());
    }

    @Test
    public void testSave() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
       
        SchedulingDTO schedulingDTO = new SchedulingDTO();
        schedulingDTO.setId(1);

        
        ResponseEntity<SchedulingDTO> response = restTemplate.postForEntity("/scheduling", schedulingDTO, SchedulingDTO.class);

       
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SchedulingDTO savedSchedulingDTO = response.getBody();
        assertNotNull(savedSchedulingDTO);
        assertEquals(schedulingDTO.getId(), savedSchedulingDTO.getId());

        Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
        assertNotNull(savedScheduling);
        assertEquals(savedSchedulingDTO.getId(), savedScheduling.getId());
    }

    @Test
    public void testDelete() {
       
        Scheduling scheduling = new Scheduling();
        schedulingRepository.save(scheduling);

      
        ResponseEntity<Void> response = restTemplate.exchange("/scheduling/{id}", HttpMethod.DELETE, null, Void.class, scheduling.getId());

       
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

     
        assertFalse(schedulingRepository.existsById(scheduling.getId()));
    }
}