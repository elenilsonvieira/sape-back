package br.edu.ifpb.dac.sape.presentation.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.sape.business.service.EmailService;
import br.edu.ifpb.dac.sape.business.service.SchedulingService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.presentation.dto.EmailDataDTO;

@RestController
@RequestMapping("/email")
public class EmailController {

//	 @Autowired
//	    private JavaMailSender javaMailSender;
	
	@Autowired
	private SchedulingService schedulingService;
	@Autowired
	private UserService userService;

	
    @Autowired
    private EmailService emailService;

    @PostMapping("/notify/{schedulingId}")
    public void notifySchedulingParticipants( @PathVariable Integer schedulingId) throws Exception {
 
    	Set<User> participants = schedulingService.getSchedulingParticipants(schedulingId);
    	
        String subject = "Você demonstrou interesse em participar da prática!";
        
        emailService.notifyAllParticipants(subject, "template-notify-scheduling-participants.ftl", participants);

    }
    @PostMapping("/notify/favoritesportscheduling/{sportId}")
    public void notifyFavoriteSportScheduling(@PathVariable Integer sportId) throws Exception {
    	
    	Set<User> users = userService.findBySportFavorite(sportId);
 
    	
    	String subject = "teste";
    	
    	emailService.notifyAllParticipants(subject, "template-notify-favorite-sport.ftl", users);
    	
    	
    	//emailService.notifyAllParticipants( subject,"template-test.ftl",users);
    }
//    @PostMapping("/send-email")
//    public void sendEmaild(@RequestBody EmailDataDTO emailData) {
//    	Map<String, Object> model = new HashMap<>();
//    	model.put("name", emailData.getName());
//    	
//    	String toEmail = emailData.getToEmail();
//    	String subject = emailData.getSubject();
//    	
//    	
//    	
//    	emailService.sendEmail(toEmail, subject,"template-test.ftl",model);
//    }
    
//    @GetMapping("/send-test-email")
//    public String sendTestEmail() {
//    	
//    	System.out.println("foi chamado");
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("rafarecen1@gmail.com"); // Defina aqui o endereço de email de destino para o teste
//            message.setSubject("Email de teste");
//            message.setText("Este é um email de teste para um BOi.");
//
//            javaMailSender.send(message);
//
//            return "Email de teste enviado com sucesso!";
//        } catch (Exception e) {
//            return "Erro ao enviar o email de teste: " + e.getMessage();
//        }
//    }
}