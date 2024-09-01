package br.edu.ifpb.dac.sape.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "SPORTS_PRACTICE", uniqueConstraints = {@UniqueConstraint(columnNames = {"SPORT_NAME"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Sport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPORT_ID")
    private Integer id;

    @Column(name = "SPORT_NAME", nullable = false)
    private String name;


    public Sport() {

    }

    public Sport(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sport other = (Sport) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }


}
