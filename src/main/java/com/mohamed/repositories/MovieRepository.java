package com.mohamed.repositories;

import com.mohamed.entities.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {


    public Optional<Movie> findByMovie(String  name) {
        return Movie.find("movieName",name).singleResultOptional();
    }
}
