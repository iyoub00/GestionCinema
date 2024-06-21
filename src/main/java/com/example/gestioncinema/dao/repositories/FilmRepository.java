package com.example.gestioncinema.dao.repositories;

import com.example.gestioncinema.dao.entities.Categorie;
import com.example.gestioncinema.dao.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestController
public interface FilmRepository extends JpaRepository<Film,Long> {
    List<Film> findByTitreContainingIgnoreCase(String titre);
    List<Film> findByCategorie(Categorie categorie);
}
