package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MovieDetails extends PanacheEntity {

    private String director;
    private String producer;
    private String runnigTime;
    private String country;
    private String language;
    @JsonIgnore
    @OneToOne(mappedBy = "movieDetails",cascade = CascadeType.ALL)
    private Movie movie;
}
