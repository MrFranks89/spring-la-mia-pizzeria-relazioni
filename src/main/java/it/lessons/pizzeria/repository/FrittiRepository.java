package it.lessons.pizzeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Fritti;

public interface FrittiRepository extends JpaRepository<Fritti, Long>{
	
	public List<Fritti> findByNomeContaining(String nome);
}
