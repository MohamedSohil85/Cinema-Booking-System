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
import java.time.LocalDate;

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
        Faker faker=new Faker();
        cinema.setAddress(faker.address().buildingNumber()+" "+faker.address().cityName()+" "+faker.address().country());
        cinema.setCinemaName("CinemaMAXX");
        LocalDate startDate=LocalDate.of(2021,12,20);
        LocalDate closeDate=LocalDate.of(2021,12,20);
        cinema.setOpen(startDate);
        cinema.setClose(closeDate);
        cinemaRepository.persist(cinema);
        return Response.status(Response.Status.CREATED).build();

    }


}
