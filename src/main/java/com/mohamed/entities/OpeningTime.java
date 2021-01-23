package com.mohamed.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@Entity
public class OpeningTime extends PanacheEntity {

    private String Day;
    private String openTime;
    private String closeTime;
    @ManyToOne
    private Cinema cinema;
}
