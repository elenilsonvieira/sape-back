package br.edu.ifpb.dac.sape.presentation.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.sape.business.service.EmailService;
import br.edu.ifpb.dac.sape.business.service.SchedulingService;
import br.edu.ifpb.dac.sape.business.service.SportService;
import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;

@RestController
@RequestMapping("/api/email")
public class EmailController {

	@Autowired
	private SchedulingService schedulingService;
	@Autowired
	private UserService userService;

	@Autowired
	private SportService sportService;
	
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
    	Sport sport = sportService.findById(sportId);
    	
    	List<User> users = userService.findBySportFavorite(sport);
 
    	
    	String subject = "Uma atividade que pode lhe interessar";
    	
    	emailService.notifyAllParticipants(subject, "template-notify-favorite-sport.ftl", (Set<User>) users);
    	
    }
    
    @PostMapping("/notify/approvedscheduling/{schedulingId}")
    public ResponseEntity notifyApprovedPrivatePlaceScheduling(@PathVariable Integer schedulingId ){
    	
    	try {
			
    		Scheduling scheduling = schedulingService.findById(schedulingId);
			User creator = scheduling.getCreator();
			String subject = "Seu Agendamento foi Aprovado";
			emailService.notifyCreator(subject,"template-notify-scheduling-creator", creator);
			
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
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