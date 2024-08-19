package br.edu.ifpb.dac.sape.util;

import br.edu.ifpb.dac.sape.business.service.EmailService;
import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmailSender {

    @Autowired
    private EmailService emailService;

    @Async
    public void sendEmail(String toEmail, String subject, String templateName, Object nome, Scheduling scheduling) {
        emailService.sendEmail(toEmail, subject, templateName, nome, scheduling);
    }

    @Async
    public void notifyAllParticipants(String subject, String templateName, Set<User> users, Scheduling scheduling) {
        emailService.notifyAllParticipants(subject, templateName, users, scheduling);
    }

    @Async
    public void notifyCreator(String subject, String templateName, User user) {
        emailService.notifyCreator(subject, templateName, user, null);
    }

    @Async
    public void notifyCreator(Set<User> users, Scheduling scheduling) throws Exception {
        emailService.notifyCreator(users, scheduling);
    }

    @Async
    public void notifyFavoriteSportScheduling(Set<User> users, Scheduling scheduling) throws Exception {
        emailService.notifyFavoriteSportScheduling(users, scheduling);
    }

    @Async
    public void notifySchedulingParticipants(Set<User> users, Scheduling scheduling) throws Exception {
        emailService.notifySchedulingParticipants(users, scheduling);
    }

    @Async
    public void notifyPlaceResponsibles(Integer placeId, Set<User> users, Scheduling scheduling) throws Exception {
        emailService.notifyPlaceResponsibles(placeId, users, scheduling);
    }
}