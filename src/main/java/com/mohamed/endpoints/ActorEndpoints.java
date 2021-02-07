package com.mohamed.endpoints;

import com.mohamed.entities.Actors;
import com.mohamed.entities.Movie;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.ActorRepository;
import com.mohamed.repositories.MovieRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/api")
public class ActorEndpoints {

    @Inject
    ActorRepository actorRepository;
    @Inject
    MovieRepository movieRepository;

    @POST
    @Path("/saveActorByMovieId/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Actors saveActorById(@PathParam("movieId")Long movieId, Actors actors)throws ResourceNotFound{
        return movieRepository.findByIdOptional(movieId).map(movie -> {
            movie.getStarring().add(actors);
            actors.setMovie(movie);
            actorRepository.persist(actors);
            return actors;
        }).orElseThrow(()->new ResourceNotFound("Object not found"));
    }

    @GET
    @Path("/Actors/{actorName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Actors findActorByName(@PathParam("actorName")String name)throws ResourceNotFound{
        List<Actors>actors=actorRepository.listAll();
        if (actors.isEmpty()){
            throw new ResourceNotFound("List is Empty");
        }
        Actors actor=Actors.find("actorName",name).singleResult();
        return actor;
    }
   @GET
   @Path("/Actors")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<Actors> loadActors()throws ResourceNotFound{
      List<Actors>actorsList=actorRepository.listAll();
      if (actorsList.isEmpty()){
          throw new ResourceNotFound("List is Empty");
      }
      return actorsList;
   }
}
