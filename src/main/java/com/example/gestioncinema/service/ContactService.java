package com.example.gestioncinema.service;

import org.springframework.stereotype.Service;

@Service
public class ContactService {
    public void sendContactMessage(String name, String email, String message) {
        // Ici, vous pouvez implémenter la logique pour traiter le message de contact
        // Par exemple, envoyer un email ou enregistrer le message dans une base de données
        System.out.println("Nom: " + name);
        System.out.println("Email: " + email);
        System.out.println("Message: " + message);

        // Implémentation d'envoi de mail ou de stockage en base de données ici
    }
}
