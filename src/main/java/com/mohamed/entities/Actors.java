package com.mohamed.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Actors extends PanacheEntity {

    @NotEmpty(message = "Name must not be Empty")
    private String actorName;
    @NotEmpty(message = "Country must not be empty")
    private String country;
    @ManyToOne
    private Movie movie;
}
