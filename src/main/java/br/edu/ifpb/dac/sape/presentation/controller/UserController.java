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
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        User entity = userService.findById(id);

        UserDTO dto = converterService.userToDto(entity);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/registration/{registration}")
    public ResponseEntity findByRegistration(@PathVariable Long registration) {
        User entity = userService.findByRegistration(registration);

        UserDTO dto = converterService.userToDto(entity);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody UserDTO dto) {
        User entity = converterService.dtoToUser(dto);
        entity = userService.save(entity);
        dto = converterService.userToDto(entity);

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
        dto.setId(id);
        User entity = converterService.dtoToUser(dto);
        entity = userService.update(entity);
        dto = converterService.userToDto(entity);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/sportsFavorite/{sportId}")
    public ResponseEntity<Void> addSportsFavorite(@PathVariable Integer userId, @PathVariable Integer sportId) {
        userService.addSportsFavorite(userId, sportId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{userId}/removeSportsFavorite/{sportId}")
    public ResponseEntity<Void> removeSportsFavorite(@PathVariable Integer userId, @PathVariable Integer sportId) {
        userService.removeSportsFavorite(userId, sportId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sportsFavorite/{id}")
    public ResponseEntity findSportsFavorite(@PathVariable Integer id) {
        User entity = userService.findById(id);

        UserDTO dto = converterService.userToDto(entity);

        return ResponseEntity.ok().body(dto.getSportsFavorite());
    }
}
