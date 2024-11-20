package it.lessons.pizzeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long>{
	
	public List<Pizza> findByNomeContaining(String nome);
}
