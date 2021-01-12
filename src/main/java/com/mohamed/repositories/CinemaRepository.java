package com.mohamed.repositories;

import com.mohamed.entities.Cinema;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CinemaRepository implements PanacheRepository<Cinema> {
}
