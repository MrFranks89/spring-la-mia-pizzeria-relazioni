package it.lessons.pizzeria.controllers;

import java.time.LocalDate;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.pizzeria.model.Offerta;
import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizze")
public class PizzaController {

	@Autowired
	private PizzaRepository pizzaRepo;

	@GetMapping
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
		
		List<Pizza> allPizze = pizzaRepo.findAll();
		
		
		if(keyword !=null && !keyword.isBlank()) {
			allPizze = pizzaRepo.findByNomeContaining(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			allPizze = pizzaRepo.findAll();
		}
		
		if(keyword == null || keyword.isBlank() || keyword.equals("null")) {
			model.addAttribute("pizzaUrl","/pizze");
		} else {
			model.addAttribute("pizzaUrl","/pizze?keyword=" + keyword);
		}
		

		model.addAttribute("pizze", allPizze);

		return "pizze/index";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable(name = "id") Long id, Model model) {

		Optional<Pizza> pizzaOptional = pizzaRepo.findById(id); // contenitore

		if (pizzaOptional.isPresent()) {
			model.addAttribute("pizza", pizzaOptional.get()); // tira fuori il contenuto dal contenitore
		}
		

		return "/pizze/show";
	}
	
	 @GetMapping("/create")
	 public String create(Model model) {
		 model.addAttribute("pizza", new Pizza());
		 
		 return "pizze/create";
	 }
	 
	 @PostMapping("/create")
	 public String store(@Valid @ModelAttribute("pizza") Pizza formPizza,
			 BindingResult bindingResult,
			 Model model, RedirectAttributes redirectAttributes) {
		 
		 if(bindingResult.hasErrors())  {
			 return "pizze/create";
			 
		 }
		 
		 pizzaRepo.save(formPizza);
		 
		 redirectAttributes.addFlashAttribute("successMessage", "Pizza Creata!");
		 
		 return "redirect:/pizze";
	 }
	 
	 @GetMapping("/edit/{id}")
	 public String edit(@PathVariable Long id,  Model model) {
		 
		 model.addAttribute("pizza", pizzaRepo.findById(id).get());
		 return "pizze/edit";
	 }
	 
	 @PostMapping("/edit/{id}")
	 public String update(@PathVariable Long id, 
			 @Valid @ModelAttribute("pizza") Pizza formPizza,
			 BindingResult bindingResult,
			 Model model, RedirectAttributes redirectAttributes) {
		 
		 if(bindingResult.hasErrors())  {
			 return "pizze/edit";
			 
		 }
		 
		 Pizza pizza = pizzaRepo.findById(id).orElseThrow();
		 new RuntimeException("Pizza non trovata");
		 
		 if(!formPizza.getNome().equals(pizza.getNome())) {
			 bindingResult.addError(new ObjectError("nome", "Il nome non può essere cambiato"));
			 return "pizze/edit";
		 }
		 
		 if (!formPizza.getDescrizione().equals(pizza.getDescrizione())) {
		        bindingResult.rejectValue("descrizione", "error.descrizione", "La descrizione non può essere cambiata");
		    }

		    if (bindingResult.hasErrors()) {
		        return "pizze/edit";
		    }
		 
		 pizzaRepo.save(formPizza);
		 
		 redirectAttributes.addFlashAttribute("successMessage", "Pizza Modificata!");
		 
		 return "redirect:/pizze";
	 }
	 
	 @PostMapping("/delete/{id}")
	 public String delete(@PathVariable("id") Long id) {

		 pizzaRepo.deleteById(id);
	 	
	 	return "redirect:/pizze";
	 }
	 
	 @GetMapping("/{id}/offerte")
	 public String offerta(@PathVariable Long id, Model model) {
		 
		  Pizza pizza = pizzaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Pizza non trovata"));

		    // Crea e configura l'offerta
		    Offerta offerta = new Offerta();
		    offerta.setPizza(pizza);
		    offerta.setOfferDate(LocalDate.now());

		    // Aggiungi l'offerta al modello
		    model.addAttribute("offerta", offerta);

		    return "offerte/edit"; // Ritorna la vista
	 }
	 
}
