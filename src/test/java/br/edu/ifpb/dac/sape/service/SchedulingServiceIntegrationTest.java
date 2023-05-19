package br.edu.ifpb.dac.sape.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.dac.sape.business.service.SchedulingService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulingServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;
    
    @Autowired
    private SchedulingService schedulingService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SchedulingRepository schedulingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PlaceRepository placeRepository;
    
    private User creator;
    
    private  SchedulingDTO schedulingDTO;
    
    
    
    @BeforeEach
    public void setUp() throws Exception {
    
    	
    	creator = new User();
    	creator.setId(2);
    	creator.setName("igor"); 
    	creator.setRegistration(1111L);
    	userService.save(creator);
    	
    	
        
      
    	 schedulingDTO = new SchedulingDTO();
    	 schedulingDTO.setId(1);
		 schedulingDTO.setScheduledDate("2023-06-25");
		 schedulingDTO.setScheduledStartTime("10:00");
		 schedulingDTO.setScheduledFinishTime("12:00");
		 schedulingDTO.setPlaceId(1);
		 schedulingDTO.setSportId(1);
		 schedulingDTO.setCreator(this.creator.getRegistration());
    }

    @Test
    public void testSaveScheduling() throws Exception {
    	
    	
       
        

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


      
        webTestClient.post()
        .uri("/api/scheduling")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(schedulingDTO)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(SchedulingDTO.class)
        .value(savedSchedulingDTO -> {
            
            assertNotNull(savedSchedulingDTO);
            
            Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
            
            assertEquals(schedulingDTO.getPlaceId(), savedScheduling.getPlace().getId());
            
            assertEquals(schedulingDTO.getSportId(), savedScheduling.getSport().getId());
            
            assertEquals(schedulingDTO.getCreator(), savedScheduling.getCreator().getRegistration());
            
            assertNotNull(savedScheduling);
            
            assertNotNull(savedScheduling.getCreator());
            

        });
    }
    
    @Test
    public void testCreateScheduling() {
        User creator = new User();
        creator.setName("igor");
        creator.setEmail("testando@gmail.com");
        creator.setRegistration(123L);
        userRepository.save(creator);

        Place place = new Place();
        place.setId(1);
        place.setName("quadra");
        place.setPublic(true);
        place.setNameResponsible("fulano");
        placeRepository.save(place);
        
        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("futebol");

        SchedulingDTO schedulingDTO = new SchedulingDTO();
        schedulingDTO.setCreator(creator.getRegistration());
        schedulingDTO.setPlaceId(place.getId());
        schedulingDTO.setSportId(sport.getId());
        schedulingDTO.setScheduledDate("2023-05-23");
        schedulingDTO.setScheduledStartTime("10:00");
        schedulingDTO.setScheduledFinishTime("12:00");

        webTestClient.post()
                .uri("/api/scheduling")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(schedulingDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SchedulingDTO.class)
                .value(savedSchedulingDTO -> {
                	
                    assertNotNull(savedSchedulingDTO.getId());
                    
                    assertEquals(schedulingDTO.getScheduledDate(), savedSchedulingDTO.getScheduledDate());
                    
                    assertEquals(schedulingDTO.getScheduledStartTime(), savedSchedulingDTO.getScheduledStartTime());
                    
                    assertEquals(schedulingDTO.getScheduledFinishTime(), savedSchedulingDTO.getScheduledFinishTime());
                    
                    assertEquals(creator.getRegistration(), savedSchedulingDTO.getCreator());
                    
                    assertEquals(place.getId(), savedSchedulingDTO.getPlaceId());
                    
                    
                });
    }
   
    
    @Test
    public void testGetScheduling() {
        User user = new User();
        user.setId(1);
        user.setName("igor");
        user.setEmail("igor@gmail.com");
        user.setRegistration(111L);
        userRepository.save(user);

        Place place = new Place();
        place.setId(1);
        place.setName("quadra");
        place.setPublic(true);
        place.setNameResponsible("fulano");
        placeRepository.save(place);

        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("futebol");
        
        Scheduling scheduling = new Scheduling();
        scheduling.setCreator(user);
        scheduling.setPlace(place);
        scheduling.setSport(sport);
        scheduling.setScheduledDate(LocalDate.of(2023, 06, 15));
        scheduling.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling.setScheduledFinishTime(LocalTime.of(12, 0));
        schedulingRepository.save(scheduling);

        webTestClient.get()
                .uri("/api/scheduling/{id}", scheduling.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(SchedulingDTO.class)
                .value(response -> {
                   
                	assertNotNull(response.getId());
                    
                    assertEquals(scheduling.getScheduledDate(), LocalDate.parse(response.getScheduledDate()));


                    assertEquals(scheduling.getScheduledStartTime(),LocalTime.parse(response.getScheduledStartTime()));
                    
                    assertEquals(scheduling.getScheduledFinishTime(),LocalTime.parse( response.getScheduledFinishTime()));
                    
                    assertEquals(user.getRegistration(), response.getCreator());
                    
                    assertEquals(place.getId(), response.getPlaceId());
                });
    }
    
   
	@Test
    public void testInvalidInput() throws Exception {
       
        SchedulingDTO invalidDTO = new SchedulingDTO();
        invalidDTO.setCreator(creator.getRegistration());
        invalidDTO.setPlaceId(null);
        invalidDTO.setSportId(1);
        invalidDTO.setScheduledDate("2023-05-23");
        invalidDTO.setScheduledStartTime("10:00");
        invalidDTO.setScheduledFinishTime("12:00");

      
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

      
        webTestClient.post()
            .uri("/api/scheduling")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidDTO)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(String.class)
            .consumeWith( response -> {

            	String exception = response.getResponseBody();
            	
            	
            	 assertNotNull(exception);
            	 assertTrue(exception.contains("O id do local não pode ser nulo!"));
            	 System.out.println("a "+exception);
            	
            });
            
    }
    
    
    @Test
    @Transactional
    public void testAddSchedulingParticipantIntegration() throws Exception {
        
        Place place = new Place();
        place.setId(1);
        place.setName("quadra");
        place.setPublic(true);
        place.setNameResponsible("fulano");
         place.setMaximumCapacityParticipants(3);
         
         
         
        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("futebol");
        
        User user = new User();
        
        user.setName("Igor");
        user.setRegistration(11L);
        
        
        userService.save(user);
       
        Scheduling scheduling = new Scheduling();
        scheduling.setId(1);
        scheduling.setCreator(user);
        scheduling.setPlace(place);
        scheduling.setSport(sport);
        scheduling.setScheduledDate(LocalDate.of(2023, 06, 15));
        scheduling.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling.setScheduledFinishTime(LocalTime.of(12, 0));
        
        Scheduling savedScheduling = schedulingRepository.save(scheduling);

      
        

        System.out.println(savedScheduling.toString());
       
        boolean added = schedulingService.addSchedulingParticipant(savedScheduling.getId(), user);

        
       
        assertTrue(added);

        
        Scheduling updatedScheduling = schedulingRepository.getById(savedScheduling.getId());

       
        Set<User> participants = updatedScheduling.getParticipants();
        assertTrue(participants.contains(user));
    }
    
    @AfterEach
    public void tearDown() throws Exception {
      
    	userRepository.deleteAll();
    	schedulingRepository.deleteAll();
       
        
    }
}