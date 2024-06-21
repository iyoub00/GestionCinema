package com.example.gestioncinema.dao.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double numero;
    private double longitude;
    private double altitude;
    @ManyToOne
    private Salle salle;
    @OneToOne(mappedBy = "place")
    private Ticket  ticket;
    private boolean reserve;
}
