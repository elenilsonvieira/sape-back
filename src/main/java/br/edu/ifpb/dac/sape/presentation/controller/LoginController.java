package br.edu.ifpb.dac.sape.presentation.controller;

import br.edu.ifpb.dac.sape.business.service.LoginService;
import br.edu.ifpb.dac.sape.business.service.TokenService;
import br.edu.ifpb.dac.sape.business.service.UserConverterService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.LoginDTO;
import br.edu.ifpb.dac.sape.presentation.dto.TokenDTO;
import br.edu.ifpb.dac.sape.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@RequestMapping("/api")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;
    private final UserConverterService userConverter;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO dto) {
        try {
            String token = service.login(dto.getUsername(), dto.getPassword());
            User entity = userService.findByRegistration(Long.valueOf(dto.getUsername()));
            UserDTO userDTO = userConverter.userToDto(entity);
            TokenDTO tokenDTO = new TokenDTO(token, userDTO);

            return new ResponseEntity(tokenDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/isValidToken")
    public ResponseEntity<Comparable> isValidToken(@RequestBody TokenDTO tokenDto) {
        try {
            boolean isValidToken = tokenService.isValid(tokenDto.getToken());

            return new ResponseEntity<Comparable>(isValidToken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
