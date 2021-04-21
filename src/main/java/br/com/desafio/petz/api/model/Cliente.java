package br.com.desafio.petz.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import br.com.desafio.petz.api.enuns.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 4257771624784337787L;

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
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PerfilEnum perfil;
	
	@Column(nullable = false)
	private String senha;
	
	public Cliente(Long id) {
		this.id = id;
	}
	
	
}
		
		
