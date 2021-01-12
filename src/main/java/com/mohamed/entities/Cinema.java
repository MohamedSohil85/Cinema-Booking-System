package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cinema extends PanacheEntity {
    private String cinemaName;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Europe/Berlin")
    private LocalDate open;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Europe/Berlin")
    private LocalDate close;
    @OneToMany
    private List<Movie> movies;




}
