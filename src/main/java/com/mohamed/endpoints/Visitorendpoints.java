package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.mohamed.entities.Payment;
import com.mohamed.entities.Visitor;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.PaymentRepository;
import com.mohamed.repositories.VisitorRepository;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api")
public class Visitorendpoints {

    @Inject
    VisitorRepository visitorRepository;
    @Inject
    PaymentRepository paymentRepository;

    @Path("/Visitor")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Transactional
    public Response createVisitor(){
        Visitor visitor=new Visitor();

       Faker faker=new Faker(new Locale("DE"));
       List<Visitor> visitors=visitorRepository.listAll();
       for(Visitor temp:visitors){
           if(temp.getLastName().equalsIgnoreCase(visitor.getLastName()))
               if(temp.getFirstName().equalsIgnoreCase(visitor.getFirstName())){
                  return Response.ok("object already found ").build();
               }
       }
       String token=UUID.randomUUID().toString();
       visitor.setToken(token);
       visitor.setFirstName(faker.name().firstName());
       visitor.setLastName(faker.name().lastName());
       visitor.setAddress(faker.address().buildingNumber()+" "+faker.address().streetAddress()+" "+faker.address().cityName()+" "+faker.address().zipCode());
       visitor.setPhoneNumber(faker.phoneNumber().phoneNumber());
       visitor.setEmail(faker.internet().emailAddress());
        visitorRepository.persist(visitor);
        return Response.status(Response.Status.CREATED).build();

    }
    @POST
    @Transactional
    @Path("/Payment/{visitorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePayment(@PathParam("visitorId")Long id)throws ResourceNotFound{
        return visitorRepository.findByIdOptional(id).map(visitor -> {
            Payment payment=new Payment();
            Faker faker=new Faker();
            payment.setAccountNumber("DE 51 5444 8444 0000 2145 17");
            payment.setCardholderName(faker.name().fullName());
            payment.setCardType("Paypal");
            payment.setAccountBic("125cHHLLXXX");
            payment.setVisitor(visitor);
            visitor.setPayment(payment);
            paymentRepository.persist(payment);
            return Response.status(Response.Status.CREATED).build();
        }).orElseThrow(()->new ResourceNotFound("Object with Id :"+id+" not found"));
    }
    @GET
    @Path("/Visitors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Visitor findVisitorById(@PathParam("id")Long id)throws ResourceNotFound{
        return visitorRepository
                .findByIdOptional(id).stream().findAny()
                .orElseThrow(()->new ResourceNotFound("Visitor with Id "+id+" not found"));
    }
    @GET
    @Path("/Visitors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Visitor>loadVisitors() throws ResourceNotFound {
        List<Visitor> visitors = visitorRepository.stream("from Visitor", Sort.descending("lastname")).collect(Collectors.toList());

        if (visitors.isEmpty()) {
            throw new ResourceNotFound("List is Empty");
        }
        return visitors;
    }
}
