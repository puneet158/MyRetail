package com.retail.myretail.model.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    private Long id;

    private String name;

    @JsonProperty("current_price")
    private Price price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
