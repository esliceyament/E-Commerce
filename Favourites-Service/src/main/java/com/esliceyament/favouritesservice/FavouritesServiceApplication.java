package com.esliceyament.favouritesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FavouritesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavouritesServiceApplication.class, args);
    }

}
