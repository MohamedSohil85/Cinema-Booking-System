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
import java.util.Arrays;
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
        return tickets;
    }




    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/Ticket/VisitorId/{visitorId}/Movie/{movieId}")
    public Response sellTicket(@PathParam("visitorId")Long visitorId,@PathParam("movieId")Long movieId,Ticket ticket) throws ResourceNotFound, IOException, WriterException {
       Movie movie=movieRepository.findByIdOptional(movieId).orElseThrow(()->new ResourceNotFound("Object not found"));
       Visitor visitor=visitorRepository.findByIdOptional(visitorId).orElseThrow(()->new ResourceNotFound("Object not found"));
       List<Ticket>tickets=ticketRepository.listAll();
        int counter=Constants.capacity;
        for(int i=0;i<tickets.size();i++){
            if((ticket.getRow()>Constants.row_number)) {
                return Response.ok("wrong Row !").build();
            }
                if ((tickets.get(i).getSeatNumber() == ticket.getSeatNumber()) && (tickets.get(i).getRow()==ticket.getRow()))
                {
                return Response.ok("Wrong SeatNr.,or Occupied !").build();
            }
                if(ticket.getCapatcity()>0){
                counter--;
                 ticket.setCapatcity(counter);}
        }



        ticket.setSeatStatus(SeatStatus.occupied);

        visitor.getTickets().add(ticket);
        movie.getTickets().add(ticket);
        ticket.setSeatStatus(SeatStatus.occupied);
        ticket.setVisitor(visitor);
         ticket.setMovie(movie);
        barcodeGenerator(ticket);
       ticketRepository.persist(ticket);

        return Response.ok(ticket).build();
    }
    public String  barcodeGenerator(@Valid Ticket data ) throws WriterException, IOException {


        String qcodePath = "C:\\Users\\Mimo\\Desktop\\Tickets"+"-BRCode.png";
        PDF417Writer pdf417Writer = new PDF417Writer();
        BitMatrix bitMatrix = pdf417Writer.encode("Movie :"+data.getMovie().getMovieName()+"\n"+
                "Row :"+data.getRow()+"\n"+"Seat.Nr :"+data.getSeatNumber()+"\n"+"Visitor :"+data.getVisitor().getLastName()
                , BarcodeFormat.PDF_417, 350, 350);
        java.nio.file.Path path = FileSystems.getDefault().getPath(qcodePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "\\Tickets\\"+data.getMovie()+ "-BRCode.png";

    }
    @DELETE
    @Path("/deleteAllTickets")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteTickets(){
        List<Ticket>tickets=ticketRepository.listAll();
        if(tickets.isEmpty()){
            return Response.noContent().build();
        }
         Ticket.deleteAll();
        return Response.ok("all Objects deleted !").build();
    }
}
