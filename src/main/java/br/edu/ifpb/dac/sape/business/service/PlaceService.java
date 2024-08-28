package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.PlaceRepository;
import br.edu.ifpb.dac.sape.presentation.exception.MissingFieldException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.sape.presentation.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserService userService;

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public boolean existsById(Integer id) {
        return placeRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return placeRepository.existsByName(name);
    }

    public Place findById(Integer id) {
        if (id == null) {
            throw new MissingFieldException("id");
        }

        return placeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("local", "id", id));
    }

    public Place findByName(String name) throws Exception {
        if (name == null || name.isBlank()) {
            throw new MissingFieldException("nome");
        }

        return placeRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("local", "nome", name));
    }

    public Place save(Place place) throws Exception {
        if (place.getName() == null || place.getName().isBlank()) {
            throw new MissingFieldException("nome", "save");
        }

        if (existsByName(place.getName())) {
            throw new ObjectAlreadyExistsException("Já existe um local com nome " + place.getName());
        }

        return placeRepository.save(place);
    }

    public Place update(Place place) throws Exception {

        if (place.getName() == null || place.getName().isBlank()) {
            throw new MissingFieldException("nome", "update");
        }

        if (place.getId() == null) {
            throw new MissingFieldException("id", "update");
        } else if (!existsById(place.getId())) {
            throw new ObjectNotFoundException("local", "id", place.getId());
        }

        if (existsByName(place.getName())) {
            Place placeSaved = findByName(place.getName());
            if (!Objects.equals(placeSaved.getId(), place.getId())) {
                throw new ObjectAlreadyExistsException("Já existe um local com nome " + place.getName());
            }
        }

        return placeRepository.save(place);
    }

    public void delete(Place place) throws Exception {
        if (place.getId() == null) {
            throw new MissingFieldException("id", "delete");
        } else if (!existsById(place.getId())) {
            throw new ObjectNotFoundException("local", "id", place.getId());
        }

        placeRepository.delete(place);
    }

    public void deleteById(Integer id) throws Exception {
        if (id == null) {
            throw new MissingFieldException("id", "delete");
        } else if (!existsById(id)) {
            throw new ObjectNotFoundException("local", "id", id);
        }

        placeRepository.deleteById(id);
    }

    public List<User> getResponsibles(Integer id) {
        Place place = findById(id);

        if (place == null) {
            throw new ObjectNotFoundException("local", "id", id);
        }

        return new ArrayList<>(place.getResponsibles());
    }


    public Boolean addResponsibles(Integer placeId, User responsible) {
        Place place = findById(placeId);
        if (place == null) {
            return false;
        }

        Set<User> responsibles = place.getResponsibles();

        if (responsibles.contains(responsible)) {
            return false;
        }
        responsibles.add(responsible);
        place.setResponsibles(responsibles);

        placeRepository.save(place);

        return true;
    }

    public Boolean removeResponsibles(Integer placeId, User responsible) {
        Place place = findById(placeId);
        if (place.getResponsibles() != null) {
            if (place.getResponsibles().size() <= 0) {
                return false;
            }
        }

        Set<User> responsibles = place.getResponsibles();

        responsibles.remove(responsible);
        place.setResponsibles(responsibles);

        placeRepository.save(place);

        return true;
    }
}