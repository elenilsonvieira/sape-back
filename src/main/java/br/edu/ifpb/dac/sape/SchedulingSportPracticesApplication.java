package br.edu.ifpb.dac.sape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.dac.sape.business.service.RoleService;
import br.edu.ifpb.dac.sape.presentation.controller.EmailController;
import br.edu.ifpb.dac.sape.presentation.dto.EmailDataDTO;

@SpringBootApplication
@EnableWebMvc
public class SchedulingSportPracticesApplication implements WebMvcConfigurer, CommandLineRunner {

	@Autowired
	private RoleService roleService;
//	@Autowired
//	private EmailController email;
	
	public static void main(String[] args) {
		SpringApplication.run(SchedulingSportPracticesApplication.class, args);
		
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
	}

	@Override
	public void run(String... args) throws Exception {

		roleService.createDefaultValues();
	}
	
}