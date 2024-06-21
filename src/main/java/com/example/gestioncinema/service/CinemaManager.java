package com.example.gestioncinema.service;

public interface CinemaManager {
    public void initVilles();
    public void initCinemas();
    public void initSalles();
    public void initPlaces();
    public void initSeances();

    public void initCategories();
    public void initfilms();
    public void initProjections();




    public void associateSeancesWithFilms();
    //public void initTickets();

}
