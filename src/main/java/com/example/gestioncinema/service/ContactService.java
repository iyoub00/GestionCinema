package com.example.gestioncinema.service;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ContactService {

    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());

    public void sendContactMessage(String name, String email, String message) {
        // Here you can implement the logic to process the contact message
        // For example, send an email or save the message in a database
        LOGGER.info("Name: " + name);
        LOGGER.info("Email: " + email);
        LOGGER.info("Message: " + message);

        // Implement email sending or database storage here
    }
}
