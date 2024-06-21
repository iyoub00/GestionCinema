package com.example.gestioncinema;

import com.example.gestioncinema.service.CinemaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestioncinemaApplication implements CommandLineRunner {
    @Autowired
    private CinemaManager cinemaManager;

    public static void main(String[] args) {
        SpringApplication.run(GestioncinemaApplication.class, args);
    }

    @Override

    public void run(String... args) throws Exception {

        cinemaManager.initVilles();
        cinemaManager.initCinemas();
        cinemaManager.initSalles();
        cinemaManager.initPlaces();
        cinemaManager.initSeances();
        cinemaManager.initCategories();
        cinemaManager.initfilms();
        cinemaManager.initProjections();




        cinemaManager.associateSeancesWithFilms();
        //cinemaManager.initTickets();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
