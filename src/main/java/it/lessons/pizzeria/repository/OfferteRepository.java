package it.lessons.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Offerta;

public interface OfferteRepository extends JpaRepository<Offerta, Long>{

}
