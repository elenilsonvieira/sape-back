package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Sport;
import br.edu.ifpb.dac.sape.model.entity.User;
import br.edu.ifpb.dac.sape.model.repository.UserRepository;
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

	public Set<User> findBySportFavorite(Sport sport) throws Exception {
		
		List<User> users = findAll();
		Set<User> usersContainSportsFavorite = new HashSet<User>();
	
		for (int i = 0; i < users.size(); i++) {
			if(users.get(i).getFavorateSports().size() > 0) {
				if(users.get(i).getSportsFavorite().get(i).getName().equals(sport.getName())) {
					System.out.println("added");
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
	
	public User findByRegistration(Long registration) throws Exception {
		if (registration == null) {
			throw new MissingFieldException("matrícula");
		}
		
		return userRepository.findByRegistration(registration)
				.orElseThrow(() -> new ObjectNotFoundException("usuário", "matrícula", registration));
	}

	public User save(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("nome", "save");
		}
		
		if (existsByRegistration(user.getRegistration())) {
			throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
		}
		
		return userRepository.save(user);
	}
	
	public User update(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("nome", "update");
		}
		
		if (user.getId() == null) {
			throw new MissingFieldException("id", "update");
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
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("usuário", "id", user.getId());
		}
		
		userRepository.delete(user);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
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
			throw new UsernameNotFoundException("Não pode ser encontrado nenhum usuário com matrícula :" + username);
		}
	}
	
	public void addSportsFavorite(Integer userId, Integer sportId) {
        User user = userRepository.findById(userId).orElseThrow(
        		() -> new IllegalArgumentException("Usuário não encontrado"));
        
        Sport sport = null;
        
		try {
			sport = sportService.findById(sportId);
			
//			if(user.getFavorateSports().contains(sport.getName())) {
//				throw new IllegalArgumentException("Esporte já favoritado!");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		  if (user.getSportsFavorite() == null) {
		         user.setSportsFavorite(new ArrayList<>());
		    }
		
		
        user.getFavorateSports().add(sport);
        

        userRepository.save(user);
    }
	
	/**
	 * This method removes a sport from the favorite sports list of the user
	 * @param userId
	 * @param sportId
	 */
	public void removeSportsFavorite(Integer userId, Integer sportId) {
        //Here we recover the user by its id 
		User user = userRepository.findById(userId).orElseThrow(
        		() -> new IllegalArgumentException("Usuário não encontrado"));
		


        		
        //this variable will receive the sport the user wants to remove
        Sport removedSport = null;
        
        try {
        	//here we get all the favourite sports in user
			List<Sport> userFavouriteSports = user.getSportsFavorite();
			// then we iterate over the list to find the favorite sport comparing the sportID in the method 
			//parameters to the sports id in the favorite sports list and save it in removedSport variable
			for(Sport favouriteSports : userFavouriteSports) {
				if(favouriteSports.getId() == sportId) {
					removedSport = favouriteSports;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
        user.getFavorateSports().remove(removedSport);
        //last step is saving our changes
        userRepository.save(user);
    }

}