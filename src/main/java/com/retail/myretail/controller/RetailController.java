package com.retail.myretail.controller;


import com.retail.myretail.model.domain.Product;
import com.retail.myretail.model.request.ProductPriceUpdateRequest;
import com.retail.myretail.service.RetailService;
import com.retail.myretail.validator.ProductPriceUpdateRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class RetailController {

    @Autowired
    private RetailService retailService;

    /**
     * Endpoint to respond to HTTP GET request at /products/{id} and delivers product data as JSON
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getById(@PathVariable long id) {
        return new ResponseEntity(retailService.getProduct(id), HttpStatus.OK);
    }

    /**
     * Endpoint to respond to an HTTP PUT request and updates the product’s price in the data store.
     *
     * @param id
     * @param productPriceUpdateRequest
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestBody @Valid ProductPriceUpdateRequest productPriceUpdateRequest) {
        retailService.updatePrice(id, productPriceUpdateRequest);
        return new ResponseEntity(retailService.getProduct(id), HttpStatus.OK);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ProductPriceUpdateRequestValidator());
    }
}
