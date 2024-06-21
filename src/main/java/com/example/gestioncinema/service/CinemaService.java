package com.example.gestioncinema.service;

import com.example.gestioncinema.dao.entities.*;
import com.example.gestioncinema.dao.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
@Service
@Transactional
public class CinemaService implements CinemaManager{
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private TicketRepository ticketRepository;


    public List<Film> searchFilms(String query) {
        return filmRepository.findByTitreContainingIgnoreCase(query);
    }

    /* .
     */
    @Override
    public void associateSeancesWithFilms() {
        List<Seance> seances = seanceRepository.findAll();
        List<Film> films = filmRepository.findAll();
        Set<Film> usedFilms = new HashSet<>(); // Keep track of films already used

        Random random = new Random();

        for (Seance seance : seances) {
            // Check if all films have been associated with seances
            if (usedFilms.size() == films.size()) {
                break; // No need to continue, all films have been associated
            }

            Film film;
            do {
                film = films.get(random.nextInt(films.size())); // Get random film
            } while (usedFilms.contains(film)); // Check if the film has already been used

            seance.setFilm(film);
            seanceRepository.save(seance);

            usedFilms.add(film); // Mark film as used
        }
    }


    @Override
    public void initVilles() {
        Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(nameVille -> {
            Ville ville = new Ville();
            ville.setName(nameVille);
            villeRepository.save(ville);
            //villeRepository.save(new Ville(null,v));
        });

    }





    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v -> {
            Stream.of("MegaRama", "Imax", "Founoun", "Chahrazad", "Doualiz")
                    .forEach(namCinema -> {
                        Cinema cinema = new Cinema();
                        cinema.setNom(namCinema);
                        cinema.setNombreSalle(3 + (int) (Math.random() * 7));
                        cinema.setVille(v);
                        cinemaRepository.save(cinema);

                    });
        });


    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i = 0; i < cinema.getNombreSalle(); i++) {
                Salle salle = new Salle();
                salle.setName("Salle" + (i + 1));
                salle.setCinema(cinema);
                salle.setNombrePlace(15 + (int) (Math.random() * 20));
                salleRepository.save(salle);


            }

        });


    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i = 0; i < salle.getNombrePlace(); i++) {
                Place place = new Place();
                place.setNumero(i + 1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });

    }

    @Override
    public void initSeances() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    public void initCategories() {
        Stream.of("Histoire", "Actions", "Fiction", "Drama").forEach(cat -> {
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categorieRepository.save(categorie);
        });

    }

    @Override
    public void initfilms() {
        double[] duress = new double[]{1, 1.5, 2};
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("Games of Thrones", "Peacky Blinders", "Vikings","Banshee","Batman","Lost","Dexter","Spiderman","Taboo")
                .forEach(titreFilm -> {
                    Film film = new Film();
                    film.setTitre(titreFilm);
                    film.setDurree(duress[new Random().nextInt(duress.length)]);
                    film.setPhoto(titreFilm.replaceAll(" ", "") + ".jpg");
                    film.setCategorie(categories.get(new Random().nextInt(categories.size())));
                    filmRepository.save(film);


                });

    }



    @Override
    public void initProjections() {
        double[] prices = new double[]{30, 50, 60, 70, 80, 90, 100};
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    filmRepository.findAll().forEach(film -> {
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                    });
                });
            });
        });


    }

   /*  @Override
    public void initTickets() {
       projectionRepository.findAll().forEach(p->{
            p.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(p.getPrix());
                ticket.setProjection(p);
                ticket.setReserver(false);
                ticketRepository.save(ticket);
            });
        });

    }


    }
   */


}
