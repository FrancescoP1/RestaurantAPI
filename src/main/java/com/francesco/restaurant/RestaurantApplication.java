package com.francesco.restaurant;

import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.francesco.restaurant.model"})
@EnableJpaRepositories(basePackages = {"com.francesco.restaurant.repository"})
public class RestaurantApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
		//RestaurantTable restTable = new RestaurantTable();
		//restTable.setCapacity(5);
		//restTable.setStatus("FREE");
		//tableRepository.save(restTable);
	}

}
