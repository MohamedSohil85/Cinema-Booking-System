package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Orders extends PanacheEntity {
    @OneToOne
    private Payment payment;
    @ManyToOne
    private Ticket ticket;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss" ,timezone = "Europe/Berlin")
    private Date paymentDate;
}
