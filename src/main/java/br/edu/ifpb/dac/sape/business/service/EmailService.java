package br.edu.ifpb.dac.sape.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.edu.ifpb.dac.sape.model.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freemarkerConfig;

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
}
