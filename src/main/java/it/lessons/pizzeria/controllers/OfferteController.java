package it.lessons.pizzeria.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.pizzeria.model.Offerta;
import it.lessons.pizzeria.repository.OfferteRepository;
import it.lessons.pizzeria.repository.PizzaRepository;
import it.lessons.pizzeria.model.Pizza;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

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

		Offerta offerta = offerteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));

		model.addAttribute("offerta", offerta);

		model.addAttribute("pizze", pizzaRepository.findAll());

		return "offerte/edit";
	}

	@PostMapping("/edit/{id}")
	public String aggiorna(@PathVariable("id") Long id, @Valid @ModelAttribute("offerta") Offerta offerta,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("edit", true);
			return "offerte/edit";
		}

		if (offerta.getOfferDate() == null || offerta.getOfferDate().isBefore(LocalDate.now())) {
			bindingResult.rejectValue("offerDate", "error.offerDate", "La data deve essere nel presente o nel futuro.");
			return "offerte/edit";
		}

		if (!offerta.areDatesValid()) {
			bindingResult.rejectValue("endDate", "error.endDate",
					"La data di fine deve essere successiva a quella di inizio.");
			return "offerte/edit";
		}

		Offerta offertaEsistente = offerteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));
		offertaEsistente.setEndDate(offerta.getEndDate());
		offertaEsistente.setTitolo(offerta.getTitolo());
		offertaEsistente.setValid(offerta.isValid());

		offerteRepository.save(offertaEsistente);
		return "redirect:/pizze/show/" + offertaEsistente.getPizza().getId();
	}

	@GetMapping("/crea")
	public String creaOfferta(Model model) {
		Offerta offerta = new Offerta();
		offerta.setOfferDate(LocalDate.now());
		model.addAttribute("offerta", offerta);
		return "offerte/edit";
	}

	@PostMapping("/crea")
	public String creaForm(@Valid @ModelAttribute("offerte") Offerta offerta, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "offerte/edit";
		}

		offerteRepository.save(offerta);
		return "redirect:/pizze/show/" + offerta.getPizza().getId();
	}

	@PostMapping("/crea/{id}")
	public String edit(@Valid @ModelAttribute("offerte") Offerta offerta, BindingResult bindingResult, Model model) {

		model.addAttribute("edit", true);

		if (bindingResult.hasErrors()) {

			return "offerte/edit";
		}

		if (offerta.getEndDate() == null) {
			bindingResult
					.addError((new FieldError("offerte", "endDate", "La data di fine offerta non può essere vuota")));
			return "offerte/edit";
		}

		if (!offerta.isValid()) {
			bindingResult.addError(new ObjectError("invalidOfferta", "Non posso modificare un'offerta non valida"));

			return "offerte/edit";
		}

		offerta.setValid(false);
		offerteRepository.save(offerta);

		Pizza pizza = offerta.getPizza();

		return "redirect:/pizze/show/" + offerta.getPizza().getId();
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		Offerta offerta = offerteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));

		Long pizzaId = offerta.getPizza().getId();

		offerteRepository.delete(offerta);

		return "redirect:/pizze/show/" + pizzaId + "?success=true";
	}
}
