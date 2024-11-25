package it.lessons.pizzeria.model;

import java.util.List;


import it.lessons.pizzeria.model.Offerta;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pizze")
public class Pizza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Il nome non può essere nullo")
	@NotBlank(message = "Il nome non può essere vuoto")
	private String nome;

	@NotNull(message = "La descrizione non può essere nulla")
	@NotBlank(message = "La descrizione non può essere vuota")
	private String descrizione;

	private String foto;

	@NotNull(message = "Il prezzo non può essere nullo")
	@Min(value = 1, message = "Il prezzo non può essere inferiore a 0")
	private Integer prezzo;
	
	@OneToMany(mappedBy = "pizza")
	private List<Offerta> offerte;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "pizze_ingredienti",
			joinColumns = @JoinColumn(name = "pizza.id"),
			inverseJoinColumns = @JoinColumn(name = "ingredienti.id")
			)
	private List<Ingredienti> ingredienti;

	public List<Offerta> getOfferte() {
		return offerte;
	}

	public void setOffers(List<Offerta> offerte) {
		this.offerte = offerte;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Integer prezzo) {
		this.prezzo = prezzo;
	}

	@Override
	public String toString() {
		return "Pizza [id=" + id + ", nome=" + nome + ", descrizione=" + descrizione + ", foto=" + foto + ", prezzo="
				+ prezzo + "]";
	}

	public List<Ingredienti> getIngredienti() {
		return ingredienti;
	}

	public void setIngredienti(List<Ingredienti> ingredienti) {
		this.ingredienti = ingredienti;
	}


}
