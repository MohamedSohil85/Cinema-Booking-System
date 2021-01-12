package com.mohamed.repositories;

import com.mohamed.entities.Visitor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VisitorRepository implements PanacheRepository<Visitor> {
}
