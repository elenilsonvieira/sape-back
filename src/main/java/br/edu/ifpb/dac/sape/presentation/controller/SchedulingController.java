package br.edu.ifpb.dac.sape.presentation.controller;

import br.edu.ifpb.dac.sape.business.service.*;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.SchedulingDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling")

public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private SchedulingConverterService converterService;

    @Autowired
    private SchedulingValidatorService validatorService;

    @Autowired
    private UserService userService;


    @Autowired
    private UserConverterService userConverterService;

    @GetMapping
    public ResponseEntity getAll() {
        try {
            List<Scheduling> entityList = schedulingService.findAll();
            System.out.println(entityList.size());
            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);


            return ResponseEntity.ok().body(dtoList);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userCreator/{registration}")
    public ResponseEntity getAllWithCreator(@PathVariable Long registration) {
        try {
            List<Scheduling> entityList = schedulingService.findAllRresponsibleAndCreator(registration);

            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

            return ResponseEntity.ok().body(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/useFilter")
    public ResponseEntity getAllWithFilter(
            @RequestParam(required = false) Integer placeId,
            @RequestParam(required = false) Integer sportId,
            @RequestParam(required = false) String date
    ) {
        try {
            Scheduling filter = converterService.dtoRequestToSchedulinng(placeId, sportId, date);
            List<Scheduling> entityList = schedulingService.findAll(filter);
            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

            return ResponseEntity.ok().body(dtoList);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id) {
        try {
            Scheduling entity = schedulingService.findById(id);
            SchedulingDTO dto = converterService.schedulingToDto(entity);

            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid SchedulingDTO dto) {

        try {
            validatorService.validateSchedulingDTO(dto);
            Scheduling entity = converterService.dtoToScheduling(dto);

            validatorService.validateScheduling(entity);
            entity = schedulingService.save(entity);

            dto = converterService.schedulingToDto(entity);

            return new ResponseEntity(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmedByPlace/{id}")
    public ResponseEntity getAllSchedulingConfirmedByPlace(@PathVariable Integer id) {  // v.2
        try {
            validatorService.validPlaceId(id);
            List<Scheduling> entityList = schedulingService.findAllByPlaceId(id);
            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

            return ResponseEntity.ok().body(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ResponsiblePlace/{userRegistration}")
    public ResponseEntity getAllSchedulingPendingByPlaceResponsible(@PathVariable Long userRegistration) {
        try {
            User user = userService.findByRegistration(userRegistration);

            List<Scheduling> entityList = schedulingService.getAllSchedulingPendingByPlaceResponsible(user);
            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

            return ResponseEntity.ok().body(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmedBySport/{id}")
    public ResponseEntity getAllSchedulingConfirmedBySport(@PathVariable Integer id) { // v.2
        try {
            validatorService.validSportId(id);
            List<Scheduling> entityList = schedulingService.findAllBySportId(id);

            List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

            return ResponseEntity.ok().body(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/{userRegistration}")
    public ResponseEntity getSchedulingsByUserRegistration(@PathVariable Long userRegistration) {
        try {
            List<Scheduling> schedulings = schedulingService.getSchedulingsByUserRegistration(userRegistration);

            List<SchedulingDTO> schedulingDTOs = converterService.schedulingToDtos(schedulings);

            return ResponseEntity.ok(schedulingDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/participation/{id}")
    public ResponseEntity getSchedulingParticipants(@PathVariable Integer id) {
        try {
            List<User> participantList = new ArrayList<>();


            participantList.addAll(schedulingService.getSchedulingParticipants(id));

            List<UserDTO> participantListDTO = userConverterService.usersToDtos(participantList);

            return ResponseEntity.ok().body(participantListDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{schedulingId}/addIsPresent/{userRegistration}")
    public ResponseEntity addIsPresent(@PathVariable Integer schedulingId, @PathVariable Long userRegistration) {
        try {
            User user = userService.findByRegistration(userRegistration);


            if (user != null) {
                schedulingService.addSchedulingParticipant(schedulingId, user);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{schedulingId}/removeIsPresent/{userRegistration}")
    public ResponseEntity removeIsPresent(@PathVariable Integer schedulingId, @PathVariable Long userRegistration) {
        try {
            User user = userService.findByRegistration(userRegistration);


            if (user != null) {
                schedulingService.removeSchedulingParticipant(schedulingId, user);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/participation/add/{id}")
    public ResponseEntity addParticipant(@PathVariable Integer id, @RequestBody Long matricula) {
        try {
            User user = userService.findByRegistration(matricula);

            if (user != null) {
                schedulingService.addSchedulingParticipant(id, user);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/participation/remove/{id}")
    public ResponseEntity removeParticipant(@PathVariable Integer id, @RequestBody Long userRegistration) {
        try {
            User user = userService.findByRegistration(userRegistration);

            if (user != null) {
                schedulingService.removeSchedulingParticipant(id, user);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/approvedScheduling/{schedulingId}")
    public ResponseEntity approveScheduling(@PathVariable Integer schedulingId) {
        try {
            Scheduling scheduling = schedulingService.findById(schedulingId);

            if (scheduling != null) {
                schedulingService.approvePrivatePlaceScheduling(scheduling);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid SchedulingDTO dto) {
        try {
            validatorService.validateSchedulingDTO(dto);
            Scheduling entity = converterService.dtoToScheduling(dto);

            validatorService.validateScheduling(entity);
            entity = schedulingService.update(id, entity);

            dto = converterService.schedulingToDto(entity);

            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        try {
            schedulingService.deleteById(id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
