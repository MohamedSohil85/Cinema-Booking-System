package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohamed.models.SeatStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket extends PanacheEntity {

    private int row;
    private int seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;
    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Visitor visitor;
    private int capatcity;

}
