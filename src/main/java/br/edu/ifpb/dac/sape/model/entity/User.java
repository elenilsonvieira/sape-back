package br.edu.ifpb.dac.sape.model.entity;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_REGISTRATION"})})
@Entity
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	// TODO: Verificar se seria mais interessante ter a matrícula como id do usuário
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer id;
	
	@Column(name = "USER_NAME", nullable = false)
	private String name;
	
	@Column(name = "USER_EMAIL", nullable = true)
	private String email;
	
	@Column(name = "USER_REGISTRATION", nullable = false)
	private Long registration;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "USER_SPORTS_FAVORITE",
		    joinColumns = @JoinColumn(name = "USER_ID"),
		    inverseJoinColumns = @JoinColumn(name = "SPORT_ID")
	)
	private List<Sport> sportsFavorite;
	
	public User() {}
	
	public User(Integer id, String name, String email, Long registration) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.registration = registration;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	
	public List<Sport> getSportsFavorite() {
		return sportsFavorite;
	}

	public void setSportsFavorite(List<Sport> sportsFavorite) {
		this.sportsFavorite = sportsFavorite;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getRegistration() {
		return registration;
	}

	public void setRegistration(Long registration) {
		this.registration = registration;
	}

	
	
	@Override
	public int hashCode() {
		return Objects.hash(email, id, name, registration, roles, sportsFavorite);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(registration, other.registration) && Objects.equals(sportsFavorite, other.sportsFavorite);
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", registration=" + registration + ", roles=" + roles
				+ ", sportsFavorite=" + sportsFavorite + "]";
	}

	@Override
	public Collection<Role> getAuthorities() {
		return roles;
	}

	public Collection<Sport> getFavorateSports() {
		return sportsFavorite ;
	}
	
	@Override
	public String getUsername() {
		return String.valueOf(this.registration);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return null;
	}
	
}