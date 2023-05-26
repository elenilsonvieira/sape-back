package br.edu.ifpb.dac.sape.business.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freemarkerConfig;
    
    @Autowired
    private SportService sportService;
    
    @Autowired
    private UserService	userService;
    
    

    public void sendEmail(String toEmail, String subject, String templateName, Object nome) {
    	
    	System.out.println("chegou aqui");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            

            Map<String, Object> model = new HashMap<>();
            model.put("name", nome);
            
            Template template = freemarkerConfig.getTemplate(templateName);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(htmlBody, true);

            javaMailSender.send(mimeMessage);
            System.out.println("email enviado com sucesso");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void notifyAllParticipants(String subject, String templateName, Set<User> users) {
    	
    	for (User user : users) {
			if(user.getEmail()!= null) {
				sendEmail(user.getEmail(), subject, templateName, user.getName());
			}
		}
    }
    
    public void notifyCreator(String subject, String templateName, User user) {
    	
    	sendEmail(user.getEmail(), subject, templateName, user.getName());
			
	}
    

    public void notifyFavoriteSportScheduling(Set<User> users) throws Exception {
    
 	
    	String subject = "Uma atividade de seu Esporte Favorito foi Criada!";
    	System.out.println(users.toString());
    	notifyAllParticipants(subject, "template-notify-favorite-sport.ftl", (Set<User>) users);
    	
    }
    
    public void notifySchedulingParticipants(Integer schedulingId, Set<User> participants) throws Exception {
    	 //notifica todos os participantes confirmado
    	
        String subject = "Você demonstrou interesse em participar da prática!";
        
        notifyAllParticipants(subject, "template-notify-scheduling-participants.ftl", participants);

    }

    public void notifyPlaceResponsible(String subject, String templateName, User user) {
    	
    	sendEmail(user.getEmail(), subject, templateName, user.getName());
			
	}
}
