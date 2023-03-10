package com.gk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.gk.model.Inventory;
import com.gk.repository.InventoryRepository;

@SpringBootApplication
@EnableEurek

public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args->{
			Inventory inventory = new Inventory();
			inventory.setSkuCode("Iphone-14-pro-max");
			inventory.setQuantity(50);
			
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("Iphone-14-pro");
			inventory1.setQuantity(0);
			
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
