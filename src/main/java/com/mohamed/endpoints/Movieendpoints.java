package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.mohamed.entities.Cinema;
import com.mohamed.entities.Movie;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.models.Genre;
import com.mohamed.repositories.CinemaRepository;
import com.mohamed.repositories.MovieRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
public class Movieendpoints {
    @Inject
    MovieRepository movieRepository;
    @Inject
    CinemaRepository cinemaRepository;

    @Path("/Movie/{cinemaId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveNewMovie(@PathParam("cinemaId")Long id) throws ResourceNotFound {
        Cinema cinema=cinemaRepository.findByIdOptional(id).orElseThrow(()->new ResourceNotFound("Object not found"));
        Movie movie=new Movie();
        movie.setGenre(Genre.ROMANCE);
        movie.setMovieName("Titanic");
        movie.setPrice(45);
        movie.setCinema(cinema);
        cinema.getMovies().add(movie);
        movieRepository.persist(movie);
        return Response.status(Response.Status.CREATED).build();

    }
}
