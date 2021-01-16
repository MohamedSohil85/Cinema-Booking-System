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
import javax.validation.constraints.Size;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment extends PanacheEntity {

    private String accountBic;
    private String accountNumber;
    @Size(min = 3,message = "3 character at Least")
    private String passWord;
    @Size(min = 3,message = "3 character at Least")
    private String cardType;
    @Size(min = 3,message = "3 character at Least")
    private String cardholderName;
    @OneToOne
    @JsonIgnore
    private Visitor visitor;
    @OneToOne
    private Orders order;
}
