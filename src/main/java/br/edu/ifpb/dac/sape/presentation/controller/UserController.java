package br.edu.ifpb.dac.sape.presentation.controller;

import br.edu.ifpb.dac.sape.business.service.UserConverterService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverterService converterService;

    @GetMapping
    public ResponseEntity getAll() {
        List<User> entityList = userService.findAll();

        List<UserDTO> dtoList = converterService.usersToDtos(entityList);

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id) {

        try {
            User entity = userService.findById(id);

            UserDTO dto = converterService.userToDto(entity);

            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/registration/{registration}")
    public ResponseEntity findByRegistration(@PathVariable Long registration) {

        try {
            User entity = userService.findByRegistration(registration);

            UserDTO dto = converterService.userToDto(entity);

            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody UserDTO dto) {

        try {
            User entity = converterService.dtoToUser(dto);
            entity = userService.save(entity);
            dto = converterService.userToDto(entity);

            return new ResponseEntity(dto, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {

        try {
            dto.setId(id);
            User entity = converterService.dtoToUser(dto);
            entity = userService.update(entity);
            dto = converterService.userToDto(entity);

            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {

        try {
            userService.deleteById(id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}/sportsFavorite/{sportId}")
    public ResponseEntity addSportsFavorite(@PathVariable Integer userId, @PathVariable Integer sportId) throws Exception {
        try {
            userService.addSportsFavorite(userId, sportId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}/removeSportsFavorite/{sportId}")
    public ResponseEntity removeSportsFavorite(@PathVariable Integer userId, @PathVariable Integer sportId) {
        try {
            userService.removeSportsFavorite(userId, sportId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sportsFavorite/{id}")
    public ResponseEntity findSportsFavorite(@PathVariable Integer id) {

        try {
            User entity = userService.findById(id);

            UserDTO dto = converterService.userToDto(entity);

            return ResponseEntity.ok().body(dto.getSportsFavorite());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
