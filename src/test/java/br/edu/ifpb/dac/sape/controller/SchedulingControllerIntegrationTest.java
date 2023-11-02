package br.edu.ifpb.dac.sape.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulingControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestRestTemplate testRestTemplate;


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
    	
    	userRepository.deleteAll();
        placeRepository.deleteAll();
        sportRepository.deleteAll();

        creator = new User();
        creator.setName("Igor");
        creator.setRegistration(1111L);
        this.creator = userService.save(creator);


        place = new Place();
        place.setName("Quadra");
        place.setPublic(true);

        place.setResponsibles(new HashSet<User>());
        Set<User> setUser = new HashSet<>(place.getResponsibles());

        setUser.add(creator);
        place.setResponsibles(setUser);

        place.setMaximumCapacityParticipants(25);
        place.setReference("Ginásio esportivo do IFPB");
        this.place = placeService.save(place);

        sport = new Sport();
        sport.setName("futsal");
        this.sport = sportRepository.save(sport);

        schedulingDTO = new SchedulingDTO();
        schedulingDTO.setScheduledDate("2023-12-01");
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

//        webTestClient.post()
//        .uri("/api/scheduling")
//        .contentType(MediaType.APPLICATION_JSON)
//        .bodyValue(schedulingDTO)
//        .exchange()
//        .expectStatus().isCreated()
//        .expectBody(SchedulingDTO.class)
//        .value(savedSchedulingDTO -> {
//
//            assertNotNull(savedSchedulingDTO);
//
//            Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
//
//            assertEquals(schedulingDTO.getPlaceId(), savedScheduling.getPlace().getId());
//
//            assertEquals(schedulingDTO.getSportId(), savedScheduling.getSport().getId());
//
//            assertEquals(schedulingDTO.getCreator(), savedScheduling.getCreator().getRegistration());
//
//            assertNotNull(savedScheduling);
//
//            assertNotNull(savedScheduling.getCreator());
//
//        });

        ResponseEntity<SchedulingDTO> responseEntity = testRestTemplate.exchange(
                "/api/scheduling",
                HttpMethod.POST,
                new HttpEntity<>(schedulingDTO, headers),
                SchedulingDTO.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        SchedulingDTO savedSchedulingDTO = responseEntity.getBody();
        assertNotNull(savedSchedulingDTO);

        Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
        assertNotNull(savedScheduling);

        assertEquals(schedulingDTO.getPlaceId(), savedScheduling.getPlace().getId());
        assertEquals(schedulingDTO.getSportId(), savedScheduling.getSport().getId());
        assertEquals(schedulingDTO.getCreator(), savedScheduling.getCreator().getRegistration());
        assertNotNull(savedScheduling.getCreator());




    }


    @Disabled
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

    @Disabled
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

    @Disabled
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

    @Disabled
    @Test
    @Transactional
    public void testAddSchedulingParticipantIntegration() throws Exception {

        User user = new User();

        user.setName("Igor");
        user.setRegistration(11L);

        userService.save(user);

        Scheduling scheduling = new Scheduling();

        scheduling.setCreator(user);
        scheduling.setPlace(place);
        scheduling.setSport(sport);
        scheduling.setScheduledDate(LocalDate.of(2023, 06, 15));
        scheduling.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling.setScheduledFinishTime(LocalTime.of(12, 0));

        Scheduling savedScheduling = schedulingRepository.save(scheduling);

        boolean addedTrue = schedulingService.addSchedulingParticipant(savedScheduling.getId(), user);

        assertTrue(addedTrue);

        Scheduling updatedScheduling = schedulingRepository.getById(savedScheduling.getId());

        Set<User> participants = updatedScheduling.getParticipants();
        assertTrue(participants.contains(user));
    }
    @Disabled
    @Test
    @Transactional
    public void testRemoveSchedulingParticipantIntegration() throws Exception {

        User user = new User();

        user.setName("fulano");
        user.setRegistration(113L);

        userService.save(user);

        Scheduling scheduling = new Scheduling();

        scheduling.setCreator(user);
        scheduling.setPlace(place);
        scheduling.setSport(sport);
        scheduling.setScheduledDate(LocalDate.of(2023, 06, 15));
        scheduling.setScheduledStartTime(LocalTime.of(10, 0));
        scheduling.setScheduledFinishTime(LocalTime.of(12, 0));

        Scheduling savedScheduling = schedulingRepository.save(scheduling);

        boolean removed = schedulingService.removeSchedulingParticipant(savedScheduling.getId(), user);

        assertTrue(removed);

        Scheduling updatedScheduling = schedulingRepository.getById(savedScheduling.getId());

        assertNotNull(updatedScheduling);

        Set<User> participants = updatedScheduling.getParticipants();

        assertFalse(participants.contains(user));
    }


    @AfterEach
    public void tearDown() throws Exception {

        userRepository.deleteAll();
        placeRepository.deleteAll();
        sportRepository.deleteAll();


    }
}