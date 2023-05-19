package br.edu.ifpb.dac.sape.presentation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.sape.business.service.EmailService;
import br.edu.ifpb.dac.sape.presentation.dto.EmailDataDTO;

@RestController
@RequestMapping("/email")
public class EmailController {

//	 @Autowired
//	    private JavaMailSender javaMailSender;

	
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public void sendEmail(@RequestBody EmailDataDTO emailData) {
    	Map<String, Object> model = new HashMap<>();
        model.put("name", emailData.getName());
        
        String toEmail = emailData.getToEmail();
        String subject = emailData.getSubject();
        
        

        emailService.sendEmail(toEmail, subject,"template-test.ftl",model);
    }
    
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