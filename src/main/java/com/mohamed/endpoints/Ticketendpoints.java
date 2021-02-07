package com.mohamed.endpoints;

import com.mohamed.entities.Movie;
import com.mohamed.entities.Ticket;
import com.mohamed.entities.Visitor;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.models.Constants;
import com.mohamed.models.SeatStatus;

import com.mohamed.repositories.MovieRepository;
import com.mohamed.repositories.TicketRepository;
import com.mohamed.repositories.VisitorRepository;

import io.quarkus.panache.common.Parameters;

import javax.inject.Inject;
import javax.transaction.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Path("/api")
public class Ticketendpoints {

    @Inject
    TicketRepository ticketRepository;
    @Inject
    MovieRepository movieRepository;
    @Inject
    VisitorRepository visitorRepository;


    @GET
    @Path("/Tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> loadTickets() throws ResourceNotFound {
        List<Ticket> tickets = ticketRepository.listAll();
        if (tickets.isEmpty()) {
            throw new ResourceNotFound("Object not found");
        }
        return tickets;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/Ticket/VisitorId/{visitorId}/Movie/{movieId}")
    public Response bookTicket(@PathParam("visitorId") Long visitorId, @PathParam("movieId") Long movieId, Ticket ticket) throws ResourceNotFound {
        Movie movie = movieRepository.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFound("Object not found"));
        Visitor visitor = visitorRepository.findByIdOptional(visitorId)
                .orElseThrow(() -> new ResourceNotFound("Object not found"));
        List<Ticket> tickets = ticketRepository.listAll();
        int counter = Constants.capacity;
        for (int i = 0; i < tickets.size(); i++) {
            if ((ticket.getRow() > Constants.row_number) || (ticket.getSeatNumber() > Constants.seat_number_per_row)) {
                throw new ResourceNotFound("the entered Seat or Row Number not Found");
            }
            if ((tickets.get(i).getSeatNumber() == ticket.getSeatNumber())
                    && (tickets.get(i).getRow() == ticket.getRow())
                    && (tickets.get(i).getMovie().equals(movie))) {
                return Response.ok("the Seat you have entered is Occupied !").build();
            }
            if (!tickets.get(i).getMovie().equals(movie)) {
                int counter_ = ticket.getCapatcity();
                counter_--;
                ticket.setCapatcity(counter_);
            } else

                counter--;
            ticket.setCapatcity(counter);
        }
        ticket.setSeatStatus(SeatStatus.occupied);
        visitor.getTickets().add(ticket);
        movie.getTickets().add(ticket);
        ticket.setSeatStatus(SeatStatus.occupied);
        ticket.setVisitor(visitor);
        ticket.setMovie(movie);

        ticketRepository.persist(ticket);
        return Response.ok(ticket).build();
    }


    @DELETE
    @Path("/cancelBookingByTicketId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteTicket(@PathParam("id") Long id) throws ResourceNotFound {
        List<Ticket> tickets = ticketRepository.listAll();
        if (tickets.isEmpty()) {
            return Response.noContent().build();
        }
        Optional<Ticket> ticketOptional = ticketRepository.findByIdOptional(id);
        if (!ticketOptional.isPresent()) {
            return Response.noContent().build();
        }
        Ticket ticket = ticketOptional.get();
        ticketRepository.deleteById(id);
        return Response.ok("Ticket of Visitor :" + ticket.getVisitor().getLastName() + "\n Row.Nr :" + ticket.getRow() + " and\n Seat Nr. :" + ticket.getSeatNumber() + " is now available").build();
    }

    @GET
    @Path("/checkTicketByMovieId/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Ticket> findTicketsByMovieId(@PathParam("movieId") Long movieId) throws ResourceNotFound {
        Optional<Movie> movieOptional = movieRepository.findByIdOptional(movieId);
        if (!movieOptional.isPresent()) {
            throw new ResourceNotFound("Object not found ");
        }
        List<Ticket> tickets = ticketRepository.stream("select row AS Row, seatNumber AS Seat ,movie from Ticket where movie_id = : movieId", Parameters.with("movieId", movieId)).collect(Collectors.toList());

        if (tickets.isEmpty()) {
            throw new ResourceNotFound("Empty list");
        }
        return tickets;
    }

}


