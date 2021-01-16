package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime open;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "Europe/Berlin")
    private LocalDateTime close;
    @OneToMany(mappedBy = "cinema",cascade = CascadeType.ALL)
    private List<Movie> movies;




}
