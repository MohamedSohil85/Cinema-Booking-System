package com.mohamed.endpoints;

import com.mohamed.entities.Movie;
import com.mohamed.entities.Review;
import com.mohamed.exceptions.ResourceNotFound;
import com.mohamed.repositories.MovieRepository;
import com.mohamed.repositories.ReviewsRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Path("/api")
public class Reviewsendpoints {

@Inject
ReviewsRepository reviewsRepository;
@Inject
MovieRepository movieRepository;

@Path("/Reviews")
@GET
@Produces(value = MediaType.APPLICATION_JSON)
public List<Review>loadreviews() throws ResourceNotFound{
    List<Review> reviews= reviewsRepository.listAll();
    if(reviews.isEmpty()){
        throw new ResourceNotFound("no reviews");
    }
    return reviews
            .stream()
            .sorted(Comparator.comparingInt(Review::getRating)).collect(Collectors.toList());
}
@Path("/Review/{movieId}")
@POST
@Produces(value = MediaType.APPLICATION_JSON)
@Transactional
public Response saveReview(@Valid Review review, @PathParam("movieId")Long id){
    return movieRepository.findByIdOptional(id).map(movie -> {
        movie.getReviews().add(review);
        review.setDateOfReview(new Date());
        review.setMovie(movie);
        reviewsRepository.persist(review);
        return Response.status(Response.Status.CREATED).build();
    }).orElse(Response.noContent().build());
}
@Path("/Reviews/{movieName}")
@GET
@Produces(value = MediaType.APPLICATION_JSON)
public List<Review> showReviewsofMovie(@PathParam("movieName")String name)throws ResourceNotFound{
     List<Review>reviews=reviewsRepository.listAll();
     List<Movie>movies=movieRepository.listAll();
     if (reviews.isEmpty()){
         throw new ResourceNotFound("no Reviews");
     }

     return reviews.stream().filter(review -> review.getMovie().getMovieName().equalsIgnoreCase(name)).sorted().collect(Collectors.toList());
}
}
