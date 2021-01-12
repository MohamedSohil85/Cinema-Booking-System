package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import com.mohamed.entities.Movie;
import com.mohamed.entities.Ticket;
import com.mohamed.entities.Visitor;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.models.Constants;
import com.mohamed.models.SeatStatus;
import com.mohamed.repositories.CinemaRepository;
import com.mohamed.repositories.MovieRepository;
import com.mohamed.repositories.TicketRepository;
import com.mohamed.repositories.VisitorRepository;
import io.quarkus.mailer.Mailer;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.listAll;

@Path("/api")
public class Ticketendpoints {

    @Inject
    TicketRepository ticketRepository;
    @Inject
    MovieRepository movieRepository;
    @Inject
    VisitorRepository visitorRepository;
    @Inject
    Mailer mailer;

    @GET
    @Path("/Tickets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket>loadTickets() throws ResourceNotFound{
        List<Ticket>tickets= ticketRepository.listAll();
        if(tickets.isEmpty()){
            throw new ResourceNotFound("Object not found");
        }
        return tickets.stream().sorted().collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/Ticket/{movieId}")
    public Response saveTicket(@PathParam("movieId")Long movieId,Ticket ticket)throws ResourceNotFound{
        return movieRepository.findByIdOptional(movieId).map(movie -> {

            for(int i=0;i<Constants.seat_number_per_row;i++){
                for(int j=0;j<Constants.row_number;j++)
                ticket.setRow(j);
                ticket.setSeatNumber(i);
                ticket.setSeatStatus(SeatStatus.available);
                ticket.setMovie(movie);
                movie.getTickets().add(ticket);
                ticketRepository.persist(ticket);

            }
            return Response.status(Response.Status.CREATED).build();
        }).orElseThrow(()->new ResourceNotFound("Object not found!"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/Ticket/{ticketId}/VisitorId/{visitorId}/Movie/{movieId}")
    public Response sellTicket(@PathParam("ticketId")Long ticketId,@PathParam("visitorId")Long visitorId,@PathParam("movieId")Long movieId) throws ResourceNotFound{
       Movie movie=movieRepository.findByIdOptional(movieId).orElseThrow(()->new ResourceNotFound("Object not found"));
       Ticket ticket=ticketRepository.findByIdOptional(ticketId).orElseThrow(()->new ResourceNotFound("Object not found"));
       Visitor visitor=visitorRepository.findByIdOptional(visitorId).orElseThrow(()->new ResourceNotFound("Object not found"));
       if(ticket.getSeatStatus().equals(SeatStatus.occupied))
       {
           throw new ResourceNotFound("Seat is Occupied");
       }
        visitor.getTickets().add(ticket);
        movie.getTickets().add(ticket);
       ticket.setSeatStatus(SeatStatus.occupied);
       ticket.setVisitor(visitor);

       ticketRepository.persist(ticket);

        return Response.status(Response.Status.CREATED).build();
    }
    public String  barcodeGenerator(@Valid Ticket data ) throws WriterException, IOException {


        String qcodePath = "C:\\Users\\Mimo\\Desktop\\Tickets"+"-BRCode.png";
        PDF417Writer pdf417Writer = new PDF417Writer();
        BitMatrix bitMatrix = pdf417Writer.encode("Movie :"+data.getMovie()+"\n"+
                "Row :"+data.getRow()+"\n"+"Seat.Nr :"+data.getSeatNumber()+"\n"+"Visitor :"+data.getVisitor()
                , BarcodeFormat.PDF_417, 350, 350);
        java.nio.file.Path path = FileSystems.getDefault().getPath(qcodePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "\\Tickets\\"+data.getMovie()+ "-BRCode.png";

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/findSeatByMovieName/{movieName}")
    public Response findSeat(@PathParam("movieName")String movieName)throws ResourceNotFound{
        Predicate<Ticket>ticketPredicate=movies-> movies.getSeatStatus().equals(SeatStatus.available);

        List<Ticket>tickets=ticketRepository
                .listAll()
                .stream()
                .filter(ticket -> ticket.getMovie().getMovieName().equalsIgnoreCase(movieName)).filter(ticketPredicate).collect(Collectors.toList());
        if (tickets.isEmpty()){
            throw new ResourceNotFound("Object not found");
        }
        return Response.ok(tickets).build();
    }

}
