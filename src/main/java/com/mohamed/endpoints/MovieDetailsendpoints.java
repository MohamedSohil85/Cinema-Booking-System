package com.mohamed.endpoints;

import com.mohamed.entities.Movie;
import com.mohamed.entities.MovieDetails;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.MovieDetailsRepository;
import com.mohamed.repositories.MovieRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/api")
public class MovieDetailsendpoints {

    @Inject
    MovieRepository movieRepository;
    @Inject
    MovieDetailsRepository movieDetailsRepository;

    @Path("/MovieDetails/{movieId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MovieDetails saveMovieDetails(@PathParam("movieId")Long movieId,MovieDetails movieDetails) throws ResourceNotFound {

        Optional<Movie>movieOptional=movieRepository.findByIdOptional(movieId);
        Movie movie=movieOptional.orElseThrow(()->new ResourceNotFound("Object not found"));
        movieDetails.setMovie(movie);
        movie.setMovieDetails(movieDetails);
        movieDetailsRepository.persist(movieDetails);
        return movieDetails;

    }
}
