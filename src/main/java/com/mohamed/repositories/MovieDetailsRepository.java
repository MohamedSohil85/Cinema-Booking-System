package com.mohamed.repositories;

import com.mohamed.entities.MovieDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieDetailsRepository implements PanacheRepository<MovieDetails> {
}
