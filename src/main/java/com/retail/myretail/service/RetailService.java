package com.retail.myretail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.retail.myretail.exception.EntityNotFoundException;
import com.retail.myretail.model.domain.Price;
import com.retail.myretail.model.domain.Product;
import com.retail.myretail.model.request.ProductPriceUpdateRequest;
import com.retail.myretail.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RetailService {

    @Autowired
    private PriceRepository priceRepository;

    /**
     * Get Product data for given id.
     * <p>
     * Steps:-
     * retrieve the product name from an external API.
     * retrieve product price from data store(mongodb)
     * and return product object containing product information(id, name,price)
     *
     * @param id
     * @return
     */
    public Product getProduct(long id) {

        //retrieve the product name from an external API.
        String name = getProductInfo(id);


        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(getPrice(id));

        return product;
    }

    /**
     * Update Product price in Mongo DB
     * <p>
     * Check if the information about product with given id exists in data store(mongo db)
     * if it does not exist throw EntityNotFound Exception
     * <p>
     * if it exists update the product’s price in the data store.
     *
     * @param id
     * @param productPriceUpdateRequest
     */
    public void updatePrice(long id, ProductPriceUpdateRequest productPriceUpdateRequest) {

        Price price = priceRepository.findById(id);

        if (price == null) {
            throw new EntityNotFoundException();
        }

        price.setValue(productPriceUpdateRequest.getValue());
        price.setCurrCode(productPriceUpdateRequest.getCurrency_code());

        priceRepository.save(price);

    }

    /**
     * Get price information for given id for data store(mongodb)
     *
     * @param id
     * @return
     */
    private Price getPrice(long id) {
        return priceRepository.findById(id);
    }

    /**
     * Performs an HTTP GET to retrieve the product name from an external API.
     *
     * @param id
     * @return
     */
    private String getProductInfo(long id) {
        //URL of external api
        StringBuffer URL = new StringBuffer("http://redsky.target.com/v2/pdp/tcin/");
        URL.append(id);
        URL.append("?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");

        String urlString = String.valueOf(URL);

        // Use RestTemplate to make the HTTP get request and JSONNode to read the name from the retrieved response
        RestTemplate restTemplate = new RestTemplate();
        try {
            JsonNode root = restTemplate.getForObject(urlString, JsonNode.class);
            String name = root.at("/product/item/product_description/title").asText();

            return name;

        } catch(Exception e) {
            //If the external API gives Not Found exception then throw EntityNotFooundException.
            throw new EntityNotFoundException(e.getMessage(), e);
        }
    }
}
