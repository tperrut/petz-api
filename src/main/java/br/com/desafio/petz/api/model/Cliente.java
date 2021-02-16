package br.com.desafio.petz.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "Cliente")
@Getter @Setter @NoArgsConstructor
public class Cliente implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Email(message = "Por favor digite um email válido!")
	@Column(nullable = false, unique = true)
	private String email;
	
	@DateTimeFormat(iso = ISO.DATE, pattern="yyyy-MM-dd")
	@Column(name= "data_nascimento", columnDefinition = "DATE")
	private LocalDate dataNascimento;

	@OneToMany(mappedBy="dono", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private List<Pet> pets;
	
	@NotEmpty(message = "Nome não pode ser vazio")
	@Column(nullable = false)
	private String nome;
	
	
	public Cliente(Long id) {
		this.id = id;
	}
	
	public Cliente(String nomeParam, LocalDate dtNascimento, String emailParam) {
		this.nome = nomeParam;
		this.dataNascimento = dtNascimento;
		this.email= emailParam;
	}
	
	
	
}
		
		
