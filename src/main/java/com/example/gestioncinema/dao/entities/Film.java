package com.example.gestioncinema.dao.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private double durree;
    private String Auteur;
    private String description;
    private String photo;
    private Date dateProduction;
    @ManyToMany(mappedBy = "films" )
    private List<Salle> salles = new ArrayList<>() ;
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)

    private Collection<Projection> projections;
    @ManyToOne
    private Categorie categorie;
    @OneToOne(mappedBy = "film")
    private Seance seance;


}
