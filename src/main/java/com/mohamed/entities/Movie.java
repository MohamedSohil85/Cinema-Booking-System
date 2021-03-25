package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohamed.models.Genre;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie extends PanacheEntity {

    @NotEmpty(message = "Enter Movie Name")
    private String movieName;
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss" ,timezone = "Europe/Berlin")
    private LocalDateTime movieStart;
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss" ,timezone = "Europe/Berlin")
    private LocalDateTime movieend;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private double price;
    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Actors> starring;
    @OneToMany
    List<Review>reviews;
    @ManyToOne
    @JsonIgnore
    private Cinema cinema;
    @OneToOne
    private MovieDetails movieDetails;
    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ticket>tickets;
}
