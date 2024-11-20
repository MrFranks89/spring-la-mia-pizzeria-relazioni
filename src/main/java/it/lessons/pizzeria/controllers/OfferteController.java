package it.lessons.pizzeria.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.pizzeria.model.Offerta;
import it.lessons.pizzeria.repository.OfferteRepository;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/offerte")
public class OfferteController {

    @Autowired
    private OfferteRepository offerteRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    // Questo è l'endpoint che mostra il modulo per modificare un'offerta
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Recupera l'offerta da modificare
        Offerta offerta = offerteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));

        // Passa l'offerta al modello
        model.addAttribute("offerta", offerta);

        // Passa anche altre informazioni necessarie, ad esempio la lista delle pizze
        model.addAttribute("pizze", pizzaRepository.findAll());

        return "offerte/edit";
    }

    @PostMapping("/crea")
    public String postMethodName(@Valid @ModelAttribute("offerte") Offerta offerta, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "offerte/edit";
        }

        offerteRepository.save(offerta);
        return "redirect:/pizze/show/" + offerta.getPizza().getId();
    }
}
