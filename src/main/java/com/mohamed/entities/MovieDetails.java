package com.mohamed.entities;

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

    @NotEmpty(message = "insert Director's Name")
    private String director;
    @NotEmpty(message = "insert Producer's Name")
    private String producer;
    @NotEmpty(message = "insert RunnigTime")
    private String runnigTime;
    @NotEmpty(message = "insert Country")
    private String country;
    @NotEmpty(message = "insert Language")
    private String language;
    @OneToOne(mappedBy = "movieDetails",cascade = CascadeType.ALL)
    private Movie movie;
}
