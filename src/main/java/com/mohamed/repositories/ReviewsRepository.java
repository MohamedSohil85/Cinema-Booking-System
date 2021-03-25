package com.mohamed.repositories;

import com.mohamed.entities.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReviewsRepository implements PanacheRepository<Review> {
}
