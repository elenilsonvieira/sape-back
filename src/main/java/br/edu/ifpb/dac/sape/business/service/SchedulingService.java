package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.enums.StatusScheduling;
import br.edu.ifpb.dac.sape.model.repository.SchedulingRepository;
import br.edu.ifpb.dac.sape.presentation.exception.MaximumParticipantCapacityExceededException;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.sape.presentation.exception.RuleViolationException;
import br.edu.ifpb.dac.sape.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class SchedulingService {

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private EmailSender emailSender;

    public List<Scheduling> findAll() {
        List<Scheduling> list = schedulingRepository.findAllByStatus(StatusScheduling.CONFIRMED);
        return schedulingsBeginingToday(list);
    }

    public List<Scheduling> findAll(Scheduling filter) {
        filter.setStatus(StatusScheduling.CONFIRMED);
        Example<Scheduling> exp = Example.of(filter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(StringMatcher.CONTAINING));

        List<Scheduling> list = schedulingRepository.findAll(exp);

        return schedulingsBeginingToday(list);
    }

    public List<Scheduling> findAllRresponsibleAndCreator(Long registration) {
        List<Scheduling> list = schedulingRepository.findAll();
        List<Scheduling> list2 = new ArrayList<>();
        for (Scheduling s : list) {
            if (s.getCreator().getRegistration().equals(registration)) {
                list2.add(s);
            } else if (s.getStatus() == StatusScheduling.CONFIRMED) {
                list2.add(s);
            }
        }
        return list2;
    }

    public List<Scheduling> findAllByPlaceId(Integer id) {
        return schedulingRepository.findAllByPlaceId(id);
    }

    public List<Scheduling> findAllByPlaceIdAndSportId(Integer placeId, Integer sportId) {
        return schedulingRepository.findAllByPlaceIdAndSportId(placeId, sportId);
    }

    public List<Scheduling> findAllByPlaceIdAndScheduledDate(Integer placeId, LocalDate scheduledDate) {
        return schedulingRepository.findAllByPlaceIdAndScheduledDate(placeId, scheduledDate);
    }

    public List<Scheduling> findAllBySportId(Integer id) {
        return schedulingRepository.findAllBySportId(id);
    }

    public List<Scheduling> findAllBySportIdAndScheduledDate(Integer sportId, LocalDate scheduledDate) {
        return schedulingRepository.findAllBySportIdAndScheduledDate(sportId, scheduledDate);
    }

    public boolean existsById(Integer id) {
        return schedulingRepository.existsById(id);
    }

    public List<Scheduling> existsByStatus(String id) {
        return schedulingRepository.findAllByStatus(StatusScheduling.PENDING);
    }

    public Scheduling findById(Integer id) {
        if (id == null) {
            throw new MissingFieldException("id");
        }

        return schedulingRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("agendamento", "id", id));
    }

    public Scheduling save(Scheduling scheduling) {

        Set<User> users = null;
        try {
            users = userService.findBySportFavorite(scheduling.getSport());
        } catch (Exception e) {

            e.printStackTrace();
        }

        if (scheduling.getPlace().isPublic()) {
            if (users != null) {
                emailSender.notifyFavoriteSportScheduling(users, scheduling);
            }

        }
        if (scheduling.getStatus() == StatusScheduling.PENDING) {
            Integer placeId = scheduling.getPlace().getId();
            Set<User> responsibles = scheduling.getPlace().getResponsibles();
            emailSender.notifyPlaceResponsibles(placeId, responsibles, scheduling);
        }

        return schedulingRepository.save(scheduling);
    }

    public void delete(Scheduling scheduling) {
        if (!existsById(scheduling.getId())) {
            throw new ObjectNotFoundException("agendamento", "id", scheduling.getId());
        }

        schedulingRepository.delete(scheduling);
    }

    public void deleteById(Integer id, String userRegistration) {
        if (id == null) {
            throw new MissingFieldException("id", "delete");
        }

        Scheduling scheduling = findById(id);
        Long creatorRegistration = scheduling.getCreator().getRegistration();
        if (!userRegistration.equals(creatorRegistration.toString())) {
            throw new RuleViolationException("Apenas quem criou pode excluir o agendamento!");
        }

        schedulingRepository.delete(scheduling);
    }

    public Set<User> getSchedulingParticipants(Integer id) {
        Scheduling scheduling = findById(id);

        return scheduling.getParticipants();
    }

    public List<Scheduling> getSchedulingsByUserRegistration(Long userRegistration) {
        User user = userService.findByRegistration(userRegistration);
        return schedulingRepository.findAllByParticipantsContaining(user);
    }

    public List<Scheduling> getAllSchedulingPendingByPlaceResponsible(User responsible) {
        List<Scheduling> schedulings = schedulingRepository.findAllByStatus(StatusScheduling.PENDING);
        List<Scheduling> schedulingsPending = new ArrayList<>();

        for (Scheduling scheduling : schedulings) {
            if (scheduling.getPlace().getResponsibles().contains(responsible)) {
                schedulingsPending.add(scheduling);
            }

        }

        return schedulingsPending;
    }

    public void addSchedulingParticipant(Integer schedulingId, User user) {
        Scheduling scheduling = findById(schedulingId);
        Set<User> participants = scheduling.getParticipants();
        if (participants != null) {
            int maximumCapacityParticipants = scheduling.getPlace().getMaximumCapacityParticipants();
            if (participants.size() >= maximumCapacityParticipants) {
                throw new MaximumParticipantCapacityExceededException();
            } else {
                participants.add(user);
                emailSender.notifySchedulingParticipants(participants, scheduling);
                save(scheduling);
            }
        }
    }

    public boolean removeSchedulingParticipant(Integer schedulingId, User user) {
        Scheduling scheduling = findById(schedulingId);

        if (scheduling.getParticipants() != null) {
            if (scheduling.getParticipants().size() <= 0) {
                return false;
            }
        }

        scheduling.setParticipants(new HashSet<>());
        Set<User> setUser = new HashSet<>(scheduling.getParticipants());

        setUser.remove(user);
        scheduling.setParticipants(setUser);
        save(scheduling);
        return true;
    }

    private List<Scheduling> schedulingsBeginingToday(List<Scheduling> list) {
        Collections.sort(list, new ComparatorSchedulingDate());
        Collections.reverse(list);

        List<Scheduling> selectedList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getScheduledDate().isBefore(LocalDate.now())) {
                break;
            } else {
                selectedList.add(list.get(i));
            }
        }

        Collections.reverse(selectedList);

        return selectedList;
    }

    public boolean approvePrivatePlaceScheduling(Scheduling scheduling) {

        if (scheduling.getPlace().getResponsibles() == null) {
            return false;

        }
        scheduling.setStatus(StatusScheduling.CONFIRMED);

        Set<User> setCreator = new HashSet<>();
        User creator = scheduling.getCreator();
        setCreator.add(creator);

        if (creator.getEmail() != null) {
            Set<User> usersSportFavorite = new HashSet<>();
            usersSportFavorite.addAll(userService.findBySportFavorite(scheduling.getSport()));

            emailSender.notifyFavoriteSportScheduling(usersSportFavorite, scheduling);

            emailSender.notifyCreator(setCreator, scheduling);
        }

        save(scheduling);
        return true;

    }


    public Scheduling update(Integer id, Scheduling entity) {

        Scheduling getScheduling = this.findById(id);

        getScheduling.setCreator(entity.getCreator());
        getScheduling.setParticipants(entity.getParticipants());
        getScheduling.setPlace(entity.getPlace());
        getScheduling.setScheduledDate(entity.getScheduledDate());
        getScheduling.setScheduledFinishTime(entity.getScheduledFinishTime());
        getScheduling.setScheduledStartTime(entity.getScheduledStartTime());
        getScheduling.setSport(entity.getSport());
        getScheduling.setStatus(entity.getStatus());
        getScheduling.setWillBePresent(entity.getWillBePresent());

        return this.save(getScheduling);
    }


}
