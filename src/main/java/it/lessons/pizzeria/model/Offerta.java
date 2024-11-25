package it.lessons.pizzeria.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

	
	@Entity
	public class Offerta{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@NotNull(message = "La data di inizio offerta non può essere vuota")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@FutureOrPresent
		private LocalDate offerDate;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@FutureOrPresent
		private LocalDate endDate;
		
		@NotNull(message = "L'offerta deve avere un nome")
		private String titolo;
		
		
		@ManyToOne
		@JoinColumn (name="pizza_id", nullable = false)
		private Pizza pizza;
		
		private boolean valid = true;
		
		 public boolean isValid() {
			return valid;
		}


		public void setValid(boolean valid) {
			this.valid = valid;
		}

		
		public boolean areDatesValid() {
		    return endDate != null && offerDate != null && endDate.isAfter(offerDate);
		}

		public Offerta() {
			this.offerDate = LocalDate.now();
		    }


		public Offerta(Long id, LocalDate offerDate, LocalDate endDate, String titolo, Pizza pizza) {
			super();
			this.id = id;
			this.offerDate = offerDate;
			this.endDate = endDate;
			this.titolo = titolo;
			this.pizza = pizza;
		}


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public LocalDate getOfferDate() {
			return offerDate;
		}


		public void setOfferDate(LocalDate offerDate) {
			this.offerDate = offerDate;
		}


		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}


		public String getTitolo() {
			return titolo;
		}


		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}


		public Pizza getPizza() {
			return pizza;
		}


		public void setPizza(Pizza pizza) {
			this.pizza = pizza;
		}
	}

