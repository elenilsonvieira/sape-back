package br.edu.ifpb.dac.sape.controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.model.repository.SportRepository;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.dto.PlaceDTO;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;
import br.edu.ifpb.dac.sape.presentation.dto.SportDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class SchedulingControllerIntegrationTest {

    @LocalServerPort
    private int port;


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

    private UserDTO creatorDTO;

    private  PlaceDTO placeDTO;

    private SportDTO sportDTO;

    private  SchedulingDTO schedulingDTO;
    
    private User creator;
    
    private  Place place;

    private Sport sport;
    
    private  Scheduling scheduling;



    @BeforeEach
    public void setUp() throws Exception {
        
    	this.creatorDTO = new UserDTO();
    	this.placeDTO = new PlaceDTO();
    	this.sportDTO = new SportDTO();
    	this.schedulingDTO = new SchedulingDTO();
        
    	this.creator = new User();
    	this.place = new Place();
    	this.sport = new Sport();
    	this.scheduling= new Scheduling();
    }
    
    
    @Test
    @Order(1)
    public void testSaveScheduling() throws Exception{
    	
    	System.out.println("Test save User");
    	this.creatorDTO.setName("José Roberto Farias Oliveira Júnior");
    	this.creatorDTO.setRegistration(202015020008L);
        
        HttpHeaders headersUser = new HttpHeaders();
        headersUser.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<UserDTO> responseEntityUser = testRestTemplate.exchange(
                "/api/user",
                HttpMethod.POST,
                new HttpEntity<>(this.creatorDTO, headersUser),
                UserDTO.class
        );
        
        assertEquals(HttpStatus.CREATED, responseEntityUser.getStatusCode());
        
        UserDTO savedUserDTO = responseEntityUser.getBody();
        assertNotNull(savedUserDTO);
        this.creatorDTO = savedUserDTO;
        
        assertEquals("José Roberto Farias Oliveira Júnior", savedUserDTO.getName());
        assertEquals(202015020008L, savedUserDTO.getRegistration());
        
        
        
        ////////////////////////////////////////////////////////////////////////////////
        System.out.println("Test Save Place");
        
        this.placeDTO = new PlaceDTO("Ginásio", "Depois do estacionamento", 300, true);

        this.placeDTO.setResponsibles(new HashSet<UserDTO>());
        
        Set<UserDTO> setUser = new HashSet<>(this.placeDTO.getResponsibles());
        this.placeDTO.getResponsibles().add(this.creatorDTO);
    	
        
        HttpHeaders headersPlace = new HttpHeaders();
        headersPlace.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<PlaceDTO> responseEntityPlace = testRestTemplate.exchange(
                "/api/place",
                HttpMethod.POST,
                new HttpEntity<>(this.placeDTO, headersPlace),
                PlaceDTO.class
        );
        
        assertEquals(HttpStatus.CREATED, responseEntityPlace.getStatusCode());
        
        PlaceDTO savedPlaceDTO = responseEntityPlace.getBody();
        assertNotNull(savedPlaceDTO);
        this.placeDTO.setId(savedPlaceDTO.getId());
        
        assertEquals("Ginásio", savedPlaceDTO.getName());
        assertEquals("Depois do estacionamento", savedPlaceDTO.getReference());
        assertEquals(300, savedPlaceDTO.getMaximumCapacityParticipants());
       
        
        //////////////////////////////////////////////////////////////////////////////
        System.out.println("Test save Sport");
        
        this.sportDTO.setName("Futsal");
        
        HttpHeaders headersSport = new HttpHeaders();
        headersSport.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<SportDTO> responseEntitySport = testRestTemplate.exchange(
                "/api/sport",
                HttpMethod.POST,
                new HttpEntity<>(this.sportDTO, headersSport),
                SportDTO.class
        );
        
        assertEquals(HttpStatus.CREATED, responseEntitySport.getStatusCode());
        
        SportDTO savedSportDTO = responseEntitySport.getBody();
        assertNotNull(savedSportDTO);
        this.sportDTO.setId(savedSportDTO.getId());
        
        assertEquals("Futsal", savedSportDTO.getName());
        
        
        
        ///////////////////////////////////////////////////////////////////////////
        System.out.println("Test save Scheduling");
        
        this.schedulingDTO.setScheduledDate("2023-12-01");
        this.schedulingDTO.setScheduledStartTime("11:00");
        this.schedulingDTO.setScheduledFinishTime("12:00");
        this.schedulingDTO.setPlaceId(this.placeDTO.getId());
        this.schedulingDTO.setSportId(this.sportDTO.getId());
        this.schedulingDTO.setCreator(this.creatorDTO.getRegistration());

        HttpHeaders headersScheduling = new HttpHeaders();
        headersScheduling.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<SchedulingDTO> responseEntityScheduling = testRestTemplate.exchange(
                "/api/scheduling",
                HttpMethod.POST,
                new HttpEntity<>(this.schedulingDTO, headersScheduling),
                SchedulingDTO.class
        );

        assertEquals(HttpStatus.CREATED, responseEntityScheduling.getStatusCode());

        SchedulingDTO savedSchedulingDTO = responseEntityScheduling.getBody();
        assertNotNull(savedSchedulingDTO);
        this.schedulingDTO = savedSchedulingDTO;

        Scheduling savedScheduling = schedulingRepository.findById(savedSchedulingDTO.getId()).orElse(null);
        assertNotNull(savedScheduling);
        this.scheduling = schedulingRepository.save(savedScheduling);

        assertEquals(this.schedulingDTO.getPlaceId(), savedScheduling.getPlace().getId());
        assertEquals(this.schedulingDTO.getSportId(), savedScheduling.getSport().getId());
        assertEquals(this.schedulingDTO.getCreator(), savedScheduling.getCreator().getRegistration());
        assertNotNull(savedScheduling.getCreator());
        
        
        
        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test update Scheduling");
		
		SchedulingDTO schedulingDTOUpdate = new SchedulingDTO();
		schedulingDTOUpdate.setScheduledDate("2023-12-28");
		schedulingDTOUpdate.setScheduledStartTime("13:00");
		schedulingDTOUpdate.setScheduledFinishTime("14:00");
		schedulingDTOUpdate.setPlaceId(this.placeDTO.getId());
		schedulingDTOUpdate.setSportId(this.sportDTO.getId());
		schedulingDTOUpdate.setCreator(this.creatorDTO.getRegistration());
	
		HttpHeaders headersSchedulingUpdate = new HttpHeaders();
		headersScheduling.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<SchedulingDTO> responseEntitySchedulingUpdate = testRestTemplate.exchange(
		"/api/scheduling/"+this.schedulingDTO.getId(),
		HttpMethod.PUT,
		new HttpEntity<>(schedulingDTOUpdate, headersScheduling),
		SchedulingDTO.class
		);
		
		assertEquals(HttpStatus.OK, responseEntitySchedulingUpdate.getStatusCode());
		
		SchedulingDTO updatedSchedulingDTO = responseEntitySchedulingUpdate.getBody();
		assertNotNull(updatedSchedulingDTO);
		this.schedulingDTO = updatedSchedulingDTO;
		
		Scheduling updatedScheduling = schedulingRepository.findById(updatedSchedulingDTO.getId()).orElse(null);
		assertNotNull(updatedScheduling);
		
		assertEquals("13:00", updatedSchedulingDTO.getScheduledStartTime());
		assertEquals("14:00", updatedSchedulingDTO.getScheduledFinishTime());
		assertEquals("2023-12-28", updatedSchedulingDTO.getScheduledDate());
		assertEquals(updatedSchedulingDTO.getPlaceId(), updatedScheduling.getPlace().getId());
		assertEquals(updatedSchedulingDTO.getSportId(), updatedScheduling.getSport().getId());
		assertEquals(updatedSchedulingDTO.getCreator(), updatedScheduling.getCreator().getRegistration());
		assertNotNull(updatedScheduling.getCreator());
	
		
		
		
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test getAll Scheduling");
		
		HttpHeaders headersSchedulingGetAll = new HttpHeaders();
		headersSchedulingGetAll.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<List<SchedulingDTO>> responseEntitySchedulingGetAll = testRestTemplate.exchange(
		    "/api/scheduling",
		    HttpMethod.GET,
		    new HttpEntity<>(headersSchedulingGetAll),
		    new ParameterizedTypeReference<List<SchedulingDTO>>() {}
		);

		List<SchedulingDTO> schedulingDTOList = responseEntitySchedulingGetAll.getBody();
		assertNotNull(schedulingDTOList);

		
		
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test getAllCreator Scheduling");
		
		HttpHeaders headersSchedulingGetAllCreator = new HttpHeaders();
		headersSchedulingGetAllCreator.setContentType(MediaType.APPLICATION_JSON);
		
		ResponseEntity<List<SchedulingDTO>> responseEntitySchedulingGetAllCreator = testRestTemplate.exchange(
		"/api/scheduling/userCreator",
		HttpMethod.GET,
		new HttpEntity<>(headersSchedulingGetAllCreator),
		new ParameterizedTypeReference<List<SchedulingDTO>>() {}
		);
		
		List<SchedulingDTO> schedulingDTOListCreator = responseEntitySchedulingGetAllCreator.getBody();
		assertNotNull(schedulingDTOListCreator);

	
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test getAllFilter Scheduling");
		HttpHeaders headersSchedulingGetFilter = new HttpHeaders();
	    headersSchedulingGetFilter.setContentType(MediaType.APPLICATION_JSON);

	    UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/scheduling/useFilter")
	             .queryParam("placeId", schedulingDTO.getPlaceId())
	             .queryParam("sportId", schedulingDTO.getSportId())
	             .queryParam("date", schedulingDTO.getScheduledDate());

	    ResponseEntity<List<SchedulingDTO>> schedulingGetFilter = testRestTemplate.exchange(
	           builder.build().toUri(),
	           HttpMethod.GET,
	           new HttpEntity<>(headersSchedulingGetFilter),
	           new ParameterizedTypeReference<List<SchedulingDTO>>() {}
	     );
	    
	    assertEquals(HttpStatus.OK, schedulingGetFilter.getStatusCode());
	    assertNotNull(schedulingGetFilter.getBody());
	    assertEquals(schedulingDTOList, schedulingGetFilter.getBody());
		
		
		
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test getAllId Scheduling");
	    
	    HttpHeaders headersGetId = new HttpHeaders();
        headersGetId.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<SchedulingDTO> responseEntityGetId = testRestTemplate.exchange(
                "/api/scheduling/" + schedulingDTO.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headersGetId),
                SchedulingDTO.class
        );

        assertEquals(HttpStatus.OK, responseEntityGetId.getStatusCode());
        assertNotNull(responseEntityGetId.getBody());
        assertEquals(schedulingDTO, responseEntityGetId.getBody());
        assertEquals("2023-12-28", schedulingDTO.getScheduledDate());
	    
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test getConfirmedPlace Scheduling");

        HttpHeaders headersConfirmedPlace = new HttpHeaders();
        headersConfirmedPlace.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<SchedulingDTO>> responseEntityConfirmedPlace = testRestTemplate.exchange(
                "/api/scheduling/confirmedByPlace/" + placeDTO.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headersConfirmedPlace),
                new ParameterizedTypeReference<List<SchedulingDTO>>() {}
        );
        
        assertEquals(HttpStatus.OK, responseEntityConfirmedPlace.getStatusCode());
        assertNotNull(responseEntityConfirmedPlace.getBody());
        assertEquals(schedulingDTOList, responseEntityConfirmedPlace.getBody());
	    
	    
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test agendamentos pendentes do responsavel");
        
        HttpHeaders headersPendingResponsible = new HttpHeaders();
        headersPendingResponsible.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<SchedulingDTO>> responseEntityPendingResponsible = testRestTemplate.exchange(
                "/api/scheduling/ResponsiblePlace/" + creatorDTO.getRegistration(),
                HttpMethod.GET,
                new HttpEntity<>(headersPendingResponsible),
                new ParameterizedTypeReference<List<SchedulingDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntityPendingResponsible.getStatusCode());
        assertNotNull(responseEntityPendingResponsible.getBody());
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test esportes confirmados");
        
        
        HttpHeaders headersSportConfirmed = new HttpHeaders();
        headersSportConfirmed.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<SchedulingDTO>> responseEntitySportConfirmed = testRestTemplate.exchange(
                "/api/scheduling/confirmedBySport/" + sportDTO.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headersSportConfirmed),
                new ParameterizedTypeReference<List<SchedulingDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntitySportConfirmed.getStatusCode());
        assertNotNull(responseEntitySportConfirmed.getBody());
        assertEquals(schedulingDTOList, responseEntitySportConfirmed.getBody());
		
		
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test buscando usuario pela registração");
        HttpHeaders headersUserResgister = new HttpHeaders();
        headersUserResgister.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<SchedulingDTO>> responseEntityUserResgister = testRestTemplate.exchange(
                "/api/scheduling/user/" + creatorDTO.getRegistration(),
                HttpMethod.GET,
                new HttpEntity<>(headersUserResgister),
                new ParameterizedTypeReference<List<SchedulingDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntityUserResgister.getStatusCode());
        assertNotNull(responseEntityUserResgister.getBody());
        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test get participantes do agendamento");
        
        HttpHeaders headersParticipantes = new HttpHeaders();
        headersParticipantes.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<UserDTO>> responseEntityParticipantes = testRestTemplate.exchange(
                "/api/scheduling/participation/" + schedulingDTO.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headersParticipantes),
                new ParameterizedTypeReference<List<UserDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntityParticipantes.getStatusCode());
        assertNotNull(responseEntityParticipantes.getBody());
        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test adicioando presença no agendamento");
        
        HttpHeaders headersPresenca = new HttpHeaders();
        headersPresenca.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> responseEntityPresenca = testRestTemplate.exchange(
                "/api/scheduling/" + schedulingDTO.getId() + "/addIsPresent/" + creatorDTO.getRegistration(),
                HttpMethod.PATCH,
                new HttpEntity<>(headersPresenca),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntityPresenca.getStatusCode());

        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test removendo presença");
		        
        HttpHeaders headersRemovePresent = new HttpHeaders();
        headersRemovePresent.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> responseEntityRemovePresent = testRestTemplate.exchange(
                "/api/scheduling/" + schedulingDTO.getId() + "/removeIsPresent/" + creatorDTO.getRegistration(),
                HttpMethod.PATCH,
                new HttpEntity<>(headersRemovePresent),
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, responseEntityRemovePresent.getStatusCode());
        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test adicionando presença pelo id");
        
        HttpHeaders headersAddingPresent = new HttpHeaders();
        headersAddingPresent.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> responseEntityAddingPresent = testRestTemplate.exchange(
                "/api/scheduling/participation/add/" + schedulingDTO.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(creatorDTO.getRegistration(), headersAddingPresent),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntityAddingPresent.getStatusCode());
        
				
		//////////////////////////////////////////////////////////////////////////
		System.out.println("Test removendo presença pelo id");
        
        
        HttpHeaders headersRemovePresentId = new HttpHeaders();
        headersRemovePresentId.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> responseEntityRemovePresentId = testRestTemplate.exchange(
                "/api/scheduling/participation/remove/" + schedulingDTO.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(creatorDTO.getRegistration(), headersRemovePresentId),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntityRemovePresentId.getStatusCode());
        
        
        
        
		//////////////////////////////////////////////////////////////////////////
		System.out.println("Test aprovando agendamento");
        
        
        HttpHeaders headersAprovingScheduling = new HttpHeaders();
        headersAprovingScheduling.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> responseEntityAprovingScheduling = testRestTemplate.exchange(
                "/api/scheduling/approvedScheduling/" + schedulingDTO.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(headersAprovingScheduling),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntityAprovingScheduling.getStatusCode());
        
        
        
        
        
        
		///////////////////////////////////////////////////////////////////////////
		System.out.println("Test delete Scheduling");
		
		
		Scheduling  deleteScheduling = schedulingRepository.findById(schedulingDTO.getId()).orElse(null);
		HttpHeaders headersSchedulingDelete = new HttpHeaders();
		headersSchedulingDelete.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<SchedulingDTO> responseEntitySchedulingDelete = testRestTemplate.exchange(
		"/api/scheduling/"+this.schedulingDTO.getId(),
		HttpMethod.DELETE,
		new HttpEntity<>(headersSchedulingDelete),
		SchedulingDTO.class
		);
		
		Scheduling deletedScheduling = schedulingRepository.findById(deleteScheduling.getId()).orElse(null);
		assertNull(deletedScheduling);
		
		
    }
    
    
    
    

    @AfterEach
    public void tearDown() throws Exception {
    	
    	this.schedulingRepository.deleteAll();
    	this.sportRepository.deleteAll();
    	this.placeRepository.deleteAll();
    	this.userRepository.deleteAll();

    }
    
}