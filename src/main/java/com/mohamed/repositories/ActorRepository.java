package com.mohamed.repositories;

import com.mohamed.entities.Actors;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActorRepository implements PanacheRepository<Actors> {
}
