package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freemarkerConfig;


    public void sendEmail(String toEmail, String subject, String templateName, Object nome, Scheduling scheduling) {

        System.out.println("chegou aqui");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);


            Map<String, Object> model = new HashMap<>();
            model.put("name", nome);

            if (scheduling != null) {

                model.put("sport", scheduling.getSport().getName());
                model.put("location", scheduling.getPlace().getName());
                model.put("date", scheduling.getScheduledDate());
                model.put("timeStart", scheduling.getScheduledStartTime());
                model.put("timeFinish", scheduling.getScheduledFinishTime());
                model.put("nameResponsible", scheduling.getCreator().getName());
                model.put("status", scheduling.getStatus());

            }

            Template template = freemarkerConfig.getTemplate(templateName);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(htmlBody, true);

            javaMailSender.send(mimeMessage);
            System.out.println("email enviado com sucesso");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void notifyAllParticipants(String subject, String templateName, Set<User> users, Scheduling scheduling) {

        for (User user : users) {
            if (user.getEmail() != null) {
                sendEmail(user.getEmail(), subject, templateName, user.getName(), scheduling);
            }
        }
    }

    public void notifyCreator(String subject, String templateName, User user, Scheduling scheduling) {

        sendEmail(user.getEmail(), subject, templateName, user.getName(), scheduling);

    }

    public void notifyCreator(Set<User> creator, Scheduling scheduling) {

        String subject = "Sua Atividade foi Aprovada";
        notifyAllParticipants(subject, "template-notify-scheduling-creator.ftl", creator, scheduling);

    }


    public void notifyFavoriteSportScheduling(Set<User> users, Scheduling scheduling) {


        String subject = "Uma atividade de seu Esporte Favorito foi Criada!";
        System.out.println(users.toString());
        notifyAllParticipants(subject, "template-notify-favorite-sport.ftl", (Set<User>) users, scheduling);

    }

    public void notifySchedulingParticipants(Set<User> participants, Scheduling scheduling) {
        //notifica todos os participantes confirmado

        String subject = "Você demonstrou interesse em participar da prática!";

        notifyAllParticipants(subject, "template-notify-scheduling-participants.ftl", participants, scheduling);

    }

    public void notifyPlaceResponsibles(Integer placeId, Set<User> responsibles, Scheduling scheduling) {

        String subject = "Uma atividade foi cadastrada num local de sua responsabilidade";

        notifyAllParticipants(subject, "template-notify-private-scheduling.ftl", (Set<User>) responsibles, scheduling);

    }
}
