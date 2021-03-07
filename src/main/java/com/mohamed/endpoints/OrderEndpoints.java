package com.mohamed.endpoints;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import com.mohamed.entities.Orders;
import com.mohamed.entities.Payment;
import com.mohamed.entities.Ticket;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.OrderRepository;
import com.mohamed.repositories.PaymentRepository;
import com.mohamed.repositories.TicketRepository;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Date;
import java.util.Optional;


@Path("/api")
public class OrderEndpoints {

    @Inject
    OrderRepository orderRepository;
    @Inject
    TicketRepository ticketRepository;
    @Inject
    PaymentRepository paymentRepository;

    @Path("/buyTicket/{ticketId}/Payment/{paymentId}")
    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyTicket(@PathParam("ticketId")Long ticketId, @PathParam("paymentId")Long paymentId){
        return ticketRepository.findByIdOptional(ticketId).map(ticket -> {
            Optional<Payment>paymentOptional=paymentRepository.findByIdOptional(paymentId);
            Payment payment=paymentOptional.get();
            Orders order=new Orders();
            order.setPaymentDate(new Date());
            payment.setOrder(order);
            order.setPayment(payment);
            order.setTicket(ticket);
            try {
                barcodeGenerator(ticket);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            orderRepository.persist(order);
            return Response.status(Response.Status.CREATED).build();
        }).orElse(Response.noContent().build());
    }

    public String  barcodeGenerator(@Valid Ticket data ) throws WriterException, IOException {


        String qcodePath = "C:\\Users\\Mimo\\Desktop\\Tickets"+"-BRCode.png";
        PDF417Writer pdf417Writer = new PDF417Writer();
        BitMatrix bitMatrix = pdf417Writer.encode("Cinema :"+data.getMovie().getCinema().getCinemaName()+"\nMovie :"+data.getMovie().getMovieName()+"\n"+
                        "Row :"+data.getRow()+"\n"+"Seat.Nr :"+data.getSeatNumber()+"\n"+"Visitor :"+data.getVisitor().getLastName()
                , BarcodeFormat.PDF_417, 350, 350);
        java.nio.file.Path path = FileSystems.getDefault().getPath(qcodePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "\\Tickets\\"+data.getMovie()+ "-BRCode.png";

    }
}
