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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        movie.setGenre(Genre.FAMILY);
        movie.setMovieName("Jurassic Park 1");
        LocalDateTime localDateTime=LocalDateTime.now();
        movie.setMovieStart(localDateTime);
        movie.setMovieend(localDateTime.plusHours(3));
        movie.setPrice(50);
        movie.setCinema(cinema);
        cinema.getMovies().add(movie);
        movieRepository.persist(movie);
        return Response.status(Response.Status.CREATED).build();

    }
    @GET
    @Path("/findMovieByKeyword/{searchKey}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findMoviesByKeyword(@PathParam("searchKey")String keyword){
        List<Movie> movieList=movieRepository.listAll().stream().filter(movie -> movie.getMovieName().startsWith(keyword)).collect(Collectors.toList());
        return Response.ok(movieList).build();
    }
}
