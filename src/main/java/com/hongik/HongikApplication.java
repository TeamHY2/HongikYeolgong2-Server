package com.hongik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HongikApplication {

	public static void main(String[] args) {
		SpringApplication.run(HongikApplication.class, args);
	}

}
