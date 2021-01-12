package com.mohamed.endpoints;

import com.github.javafaker.Faker;
import com.mohamed.entities.Payment;
import com.mohamed.entities.Visitor;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.PaymentRepository;
import com.mohamed.repositories.VisitorRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

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

       Faker faker=new Faker();
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
       visitor.setAddress(faker.address().buildingNumber()+" "+faker.address().streetAddress()+" "+faker.address().cityName()+" "+faker.address().country());
       visitor.setPhoneNumber(faker.phoneNumber().phoneNumber());
       visitor.setEmail(faker.internet().emailAddress());
        visitorRepository.persist(visitor);
        return Response.status(Response.Status.CREATED).build();

    }
    @POST
    @Transactional
    @Path("/Payment/{visitorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePayment(@PathParam("visitorId")Long id, @Valid Payment payment)throws ResourceNotFound{
        return visitorRepository.findByIdOptional(id).map(visitor -> {
            Faker faker=new Faker();
            payment.setAccountNumber(faker.finance().iban());
            payment.setCardholderName(faker.name().fullName());
            payment.setCardType("Paypal");
            payment.setAccountBic(faker.finance().bic());
            payment.setVisitor(visitor);
            visitor.setPayment(payment);
            paymentRepository.persist(payment);
            return Response.status(Response.Status.CREATED).build();
        }).orElseThrow(()->new ResourceNotFound("Object with Id :"+id+" not found"));
    }
}
