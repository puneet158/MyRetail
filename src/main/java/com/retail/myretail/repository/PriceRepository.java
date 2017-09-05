package com.retail.myretail.repository;


import com.retail.myretail.model.domain.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<Price,String> {

    Price findById(Long id);
}


