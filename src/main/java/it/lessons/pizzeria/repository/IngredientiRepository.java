package it.lessons.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Ingredienti;

public interface IngredientiRepository extends JpaRepository<Ingredienti, Long> {

}
