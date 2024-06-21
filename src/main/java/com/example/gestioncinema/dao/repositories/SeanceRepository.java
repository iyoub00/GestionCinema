package com.example.gestioncinema.dao.repositories;

import com.example.gestioncinema.dao.entities.Film;
import com.example.gestioncinema.dao.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestController
public interface SeanceRepository extends JpaRepository<Seance,Long> {
    List<Seance> findByFilm(Film film);
}
