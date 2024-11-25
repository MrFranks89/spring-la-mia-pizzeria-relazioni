package it.lessons.pizzeria.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.pizzeria.model.Ingredienti;
import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.IngredientiRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/ingredienti")
public class IngredientiController {

		@Autowired
		private IngredientiRepository ingredientiRepository;
		
		@GetMapping
		public String index(Model model) {
			
			List<Ingredienti> all= ingredientiRepository.findAll();
			
			model.addAttribute("ingredienti", all);
			model.addAttribute("ing", new Ingredienti());
			
			return "ingredienti/index";
			
		}
		
		@PostMapping("/create")
		public String create(@Valid @ModelAttribute(name = "ing") Ingredienti ingredienti,
				BindingResult bindingresult, Model model) {
			
			if(bindingresult.hasErrors()) {

				List<Ingredienti> all= ingredientiRepository.findAll();
				
				model.addAttribute("ingredienti", all);
				model.addAttribute("ing", new Ingredienti());
				
				
				return "/ingredienti/index";
			}
			
			ingredientiRepository.save(ingredienti);
			
			return "redirect:/ingredienti";
		}
		
		@PostMapping("/delete/{id}")
		public String delete(@PathVariable Long id, Model model) {
			
			Ingredienti ingredienti = ingredientiRepository.findById(id).get();
			
			for(Pizza pizza : ingredienti.getPizza()) {
				pizza.getIngredienti().remove(ingredienti);
			}
			
			ingredientiRepository.deleteById(id);
			
			return "redirect:/ingredienti";
		}
}
