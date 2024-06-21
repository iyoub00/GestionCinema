package com.example.gestioncinema.service;

import com.example.gestioncinema.dao.entities.Film;

import java.util.List;

public interface CinemaManager {

    void initVilles();

    void initCinemas();

    void initSalles();

    void initPlaces();

    void initSeances();

    void initCategories();

    void initfilms();

    void initProjections();

    List<Film> searchFilms(String query);

    void associateSeancesWithFilms();

    // Uncomment if you want to implement ticket initialization
    // void initTickets();
}
