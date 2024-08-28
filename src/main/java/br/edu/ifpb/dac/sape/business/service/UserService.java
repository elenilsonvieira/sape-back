package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
import br.edu.ifpb.dac.sape.presentation.exception.FavoriteSportException;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SportService sportService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    public boolean existsByRegistration(Long registration) {
        return userRepository.existsByRegistration(registration);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("usuário", "id", id));
    }

    public Set<User> findBySportFavorite(Sport sport) {

        List<User> users = findAll();
        Set<User> usersContainSportsFavorite = new HashSet<User>();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getFavorateSports().size() > 0) {
                if (users.get(i).getSportsFavorite().get(i).getName().equals(sport.getName())) {
                    usersContainSportsFavorite.add(users.get(i));
                }
            }

        }
        return usersContainSportsFavorite;
    }

    public Optional<User> findByName(String name) throws Exception {
        if (name == null || name.isBlank()) {
            throw new MissingFieldException("nome");
        }

        if (!userRepository.existsByName(name)) {
            throw new ObjectNotFoundException("usuário", "nome", name);
        }
        return userRepository.findByName(name);
    }

    public User findByRegistration(Long registration) {
        if (registration == null) {
            throw new MissingFieldException("matrícula");
        }

        return userRepository.findByRegistration(registration)
                .orElseThrow(() -> new ObjectNotFoundException("usuário", "matrícula", registration));
    }

    public User save(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new MissingFieldException("nome");
        }

        if (existsByRegistration(user.getRegistration())) {
            throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
        }

        return userRepository.save(user);
    }

    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new MissingFieldException("nome");
        }

        if (user.getId() == null) {
            throw new MissingFieldException("id");
        } else if (!existsById(user.getId())) {
            throw new ObjectNotFoundException("usuário", "id", user.getId());
        }

        if (existsByRegistration(user.getRegistration())) {
            User userSaved = findByRegistration(user.getRegistration());
            if (userSaved.getId() != (user.getId().intValue())) {
                throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
            }
        }

        return userRepository.save(user);
    }

    public void delete(User user) throws Exception {
        if (user.getId() == null) {
            throw new MissingFieldException("id");
        } else if (!existsById(user.getId())) {
            throw new ObjectNotFoundException("usuário", "id", user.getId());
        }

        userRepository.delete(user);
    }

    public void deleteById(Integer id) {
        if (id == null) {
            throw new MissingFieldException("id");
        } else if (!existsById(id)) {
            throw new ObjectNotFoundException("usuário", "id", id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = findByRegistration(Long.parseLong(username));
            return user;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Não foi encontrado nenhum usuário com matrícula " + username);
        }
    }

    public void addSportsFavorite(Integer userId, Integer sportId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("usuário", "id", userId));

        List<Sport> sportsFavorite = user.getSportsFavorite();
        Sport sport = sportService.findById(sportId);
        if (sportsFavorite.contains(sport)) {
            throw new FavoriteSportException();
        }

        sportsFavorite.add(sport);

        userRepository.save(user);
    }

    public void removeSportsFavorite(Integer userId, Integer sportId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("usuário", "id", userId));

        Sport removedSport = null;

        List<Sport> userFavouriteSports = user.getSportsFavorite();
        for (Sport favouriteSports : userFavouriteSports) {
            if (Objects.equals(favouriteSports.getId(), sportId)) {
                removedSport = favouriteSports;
            }
        }

        user.getFavorateSports().remove(removedSport);
        userRepository.save(user);
    }

}