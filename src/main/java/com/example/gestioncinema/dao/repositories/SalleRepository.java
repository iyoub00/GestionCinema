package com.example.gestioncinema.dao.repositories;

import com.example.gestioncinema.dao.entities.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestController
public interface SalleRepository extends JpaRepository<Salle,Long> {
}
