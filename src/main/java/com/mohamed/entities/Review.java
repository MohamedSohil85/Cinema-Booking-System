package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review extends PanacheEntity {

    private int rating;
    private String comment;
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss" ,timezone = "Europe/Berlin")
    private Date dateOfReview;
    @ManyToOne
    private Movie movie;

}
