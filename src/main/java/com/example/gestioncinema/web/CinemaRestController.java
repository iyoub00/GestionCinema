package com.example.gestioncinema.web;

import com.example.gestioncinema.dao.entities.*;
import com.example.gestioncinema.dao.repositories.*;
import com.example.gestioncinema.service.CinemaService;
import com.example.gestioncinema.service.ContactService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private ContactService contactService;
    private final Random random = new Random();

    @GetMapping(path = "/imageFilm/{id}")
    public ResponseEntity<byte[]> image(@PathVariable(name = "id") Long id) throws IOException {
        Film film = filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Film not found"));
        String photoName = film.getPhoto();
        Path path = Paths.get(System.getProperty("user.home") + "/cinema/images/" + photoName);
        byte[] imageBytes = Files.readAllBytes(path);
        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }



    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String ListFilms(Model model) {
        List<Film> films = filmRepository.findAll();
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("categorie");
        model.addAttribute("categories", categories);
        model.addAttribute("ListFilms",films);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/categories")
    public String Categorie(Model model, @RequestParam(name = "genreId")Long id){
        Optional<Categorie> categorie =  categorieRepository.findById(id);
        if(categorie.isEmpty()){
            return "redirect:/";
        }
        List<Film> listFilms =  filmRepository.findByCategorie(categorie.get());
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("categorie", categorie.get().getName());
        model.addAttribute("ListFilms", listFilms);
        model.addAttribute("categories", categories);
        return "/index";
    }

    @GetMapping("/search")
    public String searchFilms(@RequestParam("query") String query, Model model) {
        List<Film> films = cinemaService.searchFilms(query);
        model.addAttribute("ListFilms", films);
        return "index"; // ou la vue appropriée pour afficher les résultats
    }

    @GetMapping("/Reservation/film={id}")
    public String Reservation(Model model, @PathVariable (name = "id")Long id) {
        Optional<Film> film = filmRepository.findById(id);
        if (film.isEmpty())
            return "/error";
        List<Ville>villes = villeRepository.findAll();
        List<Cinema> cinemaList = cinemaRepository.findAll();
        List<Salle> salles = salleRepository.findAll();
        model.addAttribute("salles", salles);
        model.addAttribute("cinemas",cinemaList);
        model.addAttribute("ville", villes);
        model.addAttribute("Film",film.get());
        int prix = random.nextBoolean() ? 70 : 80; // Choisit 70 ou 80 aléatoirement
        model.addAttribute("prix", prix);
        return "reservation";
    }

    @PostMapping("/Reservation/film={filmId}")
    @Transactional
    public String payerTickets(@RequestParam(name="villeId") Long villeId,
                               @PathVariable(name="filmId") Long filmId,
                               @RequestParam(name="cinemaId") Long cinemaId,
                               @RequestParam(name="salleId") Long salleId,
                               @RequestParam(name="name") String name) {
        Optional<Ville> ville = villeRepository.findById(villeId);
        Optional<Film> film = filmRepository.findById(filmId);
        Optional<Cinema> cinema = cinemaRepository.findById(cinemaId);
        Optional<Salle> salle = salleRepository.findById(salleId);

        if (film.isEmpty() || ville.isEmpty() || salle.isEmpty() || cinema.isEmpty()) {
            return "redirect:/index";
        }

        List<Place> places = placeRepository.findPlaceBySalle(salle.get());
        Random random = new Random();
        int prix = random.nextBoolean() ? 70 : 80; // Choisit 70 ou 80 aléatoirement
        for (Place place : places) {
            if (!place.isReserve()) {
                Ticket ticket = new Ticket();
                ticket.setPrix(prix);
                ticket.setNomClient(name);
                ticket.setReserver(true);
                ticket.setPlace(place);
                ticketRepository.save(ticket);
                return "redirect:/index";
            }
        }
        System.out.println("Aucune place disponible");
        return "redirect:/index";
    }

    @GetMapping("/horaires")
    public String afficherHoraires(Model model) {
        List<Seance> seances = seanceRepository.findAll();
        model.addAttribute("seances", seances);
        return "horaires";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<Film> films = filmRepository.findAll();
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("films", films);
        model.addAttribute("categories", categories);
        return "admin";
    }

    @GetMapping("/admin/addFilmForm")
    public String showAddFilmForm(Model model) {
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("categories", categories);
        return "addfilm"; // Ensure this matches the name of your template
    }

    @PostMapping("/admin/addFilm")
    public String addFilm(@RequestParam("titre") String titre,
                          @RequestParam("description") String description,
                          @RequestParam("photo") MultipartFile photo,
                          @RequestParam("categorieId") Long categorieId) throws IOException {
        Film film = new Film();
        film.setTitre(titre);
        film.setDescription(description);

        if (!photo.isEmpty()) {
            String photoName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            film.setPhoto(photoName);
            File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName);
            photo.transferTo(file);
        }

        Categorie categorie = categorieRepository.findById(categorieId).orElse(null);
        if (categorie != null) {
            film.setCategorie(categorie);
        } else {
            throw new RuntimeException("Categorie not found");
        }

        filmRepository.save(film);
        return "redirect:/admin";
    }

    @GetMapping("/tarifs")
    public String showTarifsPage(Model model) {
        // Ajout des plages horaires et des prix correspondants
        String tarifJour = "70 DH (de 12h00 à 17h00)";
        String tarifSoir = "80 DH (de 19h00 à 21h00)";
        model.addAttribute("tarifJour", tarifJour);
        model.addAttribute("tarifSoir", tarifSoir);
        return "tarifs"; // Renvoie le nom de la vue Thymeleaf pour la page des tarifs
    }

    @GetMapping("/admin/editFilm/{id}")
    public String editFilm(@PathVariable Long id, Model model) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            List<Categorie> categories = categorieRepository.findAll();
            model.addAttribute("film", film);
            model.addAttribute("categories", categories);
            return "editFilm"; // Assurez-vous que le template "editFilm.html" existe dans votre répertoire de vues
        } else {
            return "redirect:/admin"; // Redirection vers la page d'administration si le film n'est pas trouvé
        }
    }

    @PostMapping("/admin/updateFilm/{id}")
    public String updateFilm(@PathVariable Long id,
                             @RequestParam("titre") String titre,
                             @RequestParam("description") String description,
                             @RequestParam("photo") MultipartFile photo,
                             @RequestParam("categorieId") Long categorieId) throws IOException {
        Film film = filmRepository.findById(id).orElse(null);
        if (film != null) {
            film.setTitre(titre);
            film.setDescription(description);
            if (!photo.isEmpty()) {
                String photoName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                film.setPhoto(photoName);
                File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName);
                photo.transferTo(file);
            }
            Categorie categorie = categorieRepository.findById(categorieId).orElse(null);
            if (categorie != null) {
                film.setCategorie(categorie);
            }
            filmRepository.save(film);
        }
        return "redirect:/admin"; // Redirige vers la page d'administration après la mise à jour
    }

    @DeleteMapping("/admin/deleteFilm/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contact")
    public String showContactForm() {
        return "contact"; // Renvoie le nom de la vue Thymeleaf pour la page de contact
    }

    @PostMapping("/contact")
    public String submitContactForm(@RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("message") String message,
                                    RedirectAttributes redirectAttributes) {
        // Appeler le service pour traiter le message de contact
        contactService.sendContactMessage(name, email, message);
        // Ajouter un attribut flash pour afficher un message de succès
        redirectAttributes.addFlashAttribute("successMessage", "Votre message a été envoyé avec succès !");
        // Rediriger vers la page de contact
        return "redirect:/contact";
    }
}

@Data
class TicketForm {
    private String nomClients;
    private List<Long> tickets = new ArrayList<>();
}
