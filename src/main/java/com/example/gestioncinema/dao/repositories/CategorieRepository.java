package com.example.gestioncinema.dao.repositories;

import com.example.gestioncinema.dao.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategorieRepository extends JpaRepository<Categorie,Long> {

}
