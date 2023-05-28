package br.edu.ifpb.dac.sape.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.edu.ifpb.dac.sape.business.service.EmailService;
import br.edu.ifpb.dac.sape.model.entity.User;

@Component
public class EmailSender {
    
    @Autowired
    private EmailService emailService;
    
    @Async
    public void sendEmail(String toEmail, String subject, String templateName, Object nome) {
        emailService.sendEmail(toEmail, subject, templateName, nome);
    }
    
    @Async
    public void notifyAllParticipants(String subject, String templateName, Set<User> users) {
        emailService.notifyAllParticipants(subject, templateName, users);
    }
    
    @Async
    public void notifyCreator(String subject, String templateName, User user) {
        emailService.notifyCreator(subject, templateName, user);
    }
    
    @Async
    public void notifyCreator(Integer schedulingId, Set<User> users) throws Exception{
        emailService.notifyCreator(schedulingId, users);
    }
    
    @Async
    public void notifyFavoriteSportScheduling(Set<User> users) throws Exception {
        emailService.notifyFavoriteSportScheduling(users);
    }
    
    @Async
    public void notifySchedulingParticipants(Integer schedulingId, Set<User> users) throws Exception {
        emailService.notifySchedulingParticipants(schedulingId, users);
    }
    
    @Async
    public void notifyPlaceResponsibles(Integer placeId, Set<User> users) throws Exception {
        emailService.notifyPlaceResponsibles(placeId, users);
    }
}