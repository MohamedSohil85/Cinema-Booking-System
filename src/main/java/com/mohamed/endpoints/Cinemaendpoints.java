package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.mohamed.entities.Cinema;
import com.mohamed.entities.Movie;
import com.mohamed.entities.OpeningTime;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.CinemaRepository;
import com.mohamed.repositories.MovieRepository;
import com.mohamed.repositories.OpeningTimeRepository;
import io.quarkus.panache.common.Parameters;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api")
public class Cinemaendpoints {

    @Inject
    CinemaRepository cinemaRepository;
    @Inject
    MovieRepository movieRepository;
    @Inject
    OpeningTimeRepository openingTimeRepository;

    @Path("/Cinema")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveCinema(){
        Cinema cinema=new Cinema();
        Faker faker=new Faker(new Locale("DE"));
        cinema.setAddress(faker.address().buildingNumber()+" "+faker.address().streetAddress()+" "+faker.address().cityName()+" "+faker.address().zipCode());

        cinema.setCinemaName("CinemaMAXX");
        LocalDateTime startDate=LocalDateTime.now();

        LocalDateTime closeDate=startDate.plus(Duration.ofHours(8));
        cinema.setOpen(startDate);
        cinema.setClose(closeDate);
        cinemaRepository.persist(cinema);
        return Response.status(Response.Status.CREATED).build();

    }

    @GET
    @Path("/showCinemaProgramm/{cinemaId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Movie> showMoviesByCinemaId(@PathParam("cinemaId")Long cinemaId)throws ResourceNotFound{
        Optional<Cinema>cinemaOptional =cinemaRepository.findByIdOptional(cinemaId);
        if (!cinemaOptional.isPresent()){
            throw new ResourceNotFound("Object not found");
        }
        List<Cinema>cinemas=cinemaRepository.listAll();
        if (cinemas.isEmpty()){
            throw new ResourceNotFound("List is Empty");
        }
        List<Movie>movies=movieRepository.stream("select movieName,movieStart, genre from movie where cinema_id = :cinemaId", Parameters.with("cinemaId",cinemaId)).collect(Collectors.toList());
        return movies;

    }
   @POST
   @Path("/OpeningTime/{cinemaId}")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   @Transactional
    public Response saveOpeningTime(@PathParam("cinemaId")Long cinemaId, OpeningTime openingTime) throws ResourceNotFound {
       Optional<Cinema>cinemaOptional =cinemaRepository.findByIdOptional(cinemaId);
       if (!cinemaOptional.isPresent()){
           throw new ResourceNotFound("Object not found");
       }
       Cinema cinema=cinemaOptional.get();
       cinema.getOpeningTimes().add(openingTime);
       openingTime.setCinema(cinema);
       openingTimeRepository.persist(openingTime);
       return Response.ok(openingTime).build();
   }

}
