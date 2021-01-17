package com.mohamed.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Visitor extends PanacheEntity {

    @Size(min = 3,message = "min 3 character")
    private String lastName;
    @Size(min = 3 ,message = "min 3 character")
    private String firstName;
    @Size(min = 3,message = "min 3 character")
    private String address;
    @Size(min = 7,message = "min 3 character")
    private String phoneNumber;
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$")
    private String email;
    private String token;
    @OneToOne
    @JsonIgnore
    private Payment payment;
    @JsonIgnore
    @OneToMany(mappedBy = "visitor")
    private List<Ticket>tickets;
}
