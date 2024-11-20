package it.lessons.pizzeria.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.pizzeria.model.Fritti;
import it.lessons.pizzeria.repository.FrittiRepository;


@Controller
@RequestMapping("/fritti")
public class FrittiController {

	@Autowired
	private FrittiRepository frittiRepo;

	@GetMapping
	public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
		
		List<Fritti> allFritti = frittiRepo.findAll();
		
		
		if(keyword !=null && !keyword.isBlank()) {
			allFritti = frittiRepo.findByNomeContaining(keyword);
			model.addAttribute("keyword", keyword);
		} else {
			allFritti = frittiRepo.findAll();
		}
		
		model.addAttribute("fritti", allFritti);

		return "/fritti/index";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable(name = "id") Long id, Model model) {
	    
	    Optional<Fritti> frittiOptional = frittiRepo.findById(id); // contenitore
	    
	    if (frittiOptional.isPresent()) {
	        model.addAttribute("fritti", frittiOptional.get()); // tira fuori il contenuto dal contenitore
	        return "fritti/show";
	    } else {
	        return "redirect:/fritti";
	    }
	}

	
}
