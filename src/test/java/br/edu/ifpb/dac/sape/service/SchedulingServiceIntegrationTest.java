package br.edu.ifpb.dac.sape.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.dac.sape.business.service.PlaceService;
import br.edu.ifpb.dac.sape.business.service.SchedulingService;
import br.edu.ifpb.dac.sape.business.service.SportService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private PlaceService placeService;
    
    @Autowired
    private SportService sportService;

    @Autowired
    private SchedulingRepository schedulingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PlaceRepository placeRepository;
    
    @Autowired
    private SportRepository sportRepository;
    
    private User creator;
    
    
    private  Place place;
    
    private Sport sport;
    
    private  SchedulingDTO schedulingDTO;
    
    
    
    @BeforeEach
    public void setUp() throws Exception {
    
    	
    	creator = new User();
    	
    	creator.setName("igor"); 
    	creator.setRegistration(1111L);
    	userService.save(creator);
    	
    	
    	 place = new Place();
       
         place.setName("quadraa");
         place.setPublic(true);
         place.setNameResponsible("fulano");
         place.setMaximumCapacityParticipants(25);
         placeRepository.save(place);
         
         sport = new Sport();
       
         sport.setName("futeboll");
         sportRepository.save(sport);
        
         schedulingDTO = new SchedulingDTO();
    
		 schedulingDTO.setScheduledDate("2023-06-25");
		 schedulingDTO.setScheduledStartTime("10:00");
		 schedulingDTO.setScheduledFinishTime("12:00");
		 schedulingDTO.setPlaceId(place.getId());
		 schedulingDTO.setSportId(sport.getId());
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

        
        Scheduling scheduling = new Scheduling();
        scheduling.setCreator(creator);
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
                    
                    assertEquals(creator.getRegistration(), response.getCreator());
                    
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
            	
            });
            
    }
    
    
    @Test
    @Transactional
    public void testAddSchedulingParticipantIntegration() throws Exception {
        
        Place place1 = new Place();
     
        place1.setName("quadra");
        place1.setPublic(true);
        place1.setNameResponsible("fulano");
        place1.setMaximumCapacityParticipants(3);
         
        Sport sport1 = new Sport();
        
        sport1.setName("futebol");
      
        
        User user1 = new User();
        
        user1.setName("Igor");
        user1.setRegistration(11L);
        
        
        userService.save(user1);
       
        Scheduling scheduling = new Scheduling();
       
        scheduling.setCreator(user1);
        scheduling.setPlace(place);
        scheduling.setSport(sport);
        scheduling.setScheduledDate(LocalDate.of(2023, 06, 15));
        scheduling.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling.setScheduledFinishTime(LocalTime.of(12, 0));
     
        Scheduling savedScheduling = schedulingRepository.save(scheduling);

        boolean added = schedulingService.addSchedulingParticipant(savedScheduling.getId(), user1);
       
        assertTrue(added);
 
        Scheduling updatedScheduling = schedulingRepository.getById(savedScheduling.getId());
  
        Set<User> participants = updatedScheduling.getParticipants();
        assertTrue(participants.contains(user1));
    }
    

    @AfterEach
    public void tearDown() throws Exception {
      
    	userRepository.deleteAll();
    	placeRepository.deleteAll();
    	sportRepository.deleteAll();
       
    }
}