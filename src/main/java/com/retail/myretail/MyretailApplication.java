package com.retail.myretail;

import com.retail.myretail.model.domain.Price;
import com.retail.myretail.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyretailApplication implements CommandLineRunner{

	@Autowired
	private PriceRepository priceRepository;

	public static void main(String[] args) {
		SpringApplication.run(MyretailApplication.class, args);
	}

	//pre-populate some price data in data store(mongodb)
	@Override
	public void run(String... args) throws Exception {
		Price price = new Price();
		price.setId(13860428L);
		price.setCurrCode("USD");
		price.setValue(25.49);
		priceRepository.save(price);
	}
}
