package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.mohamed.entities.Cinema;
import com.mohamed.repositories.CinemaRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Locale;

@Path("/api")
public class Cinemaendpoints {

    @Inject
    CinemaRepository cinemaRepository;

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


}
