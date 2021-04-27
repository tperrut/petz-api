package br.com.desafio.petz.api.model;

import br.com.desafio.petz.api.dto.ClienteDto;
import br.com.desafio.petz.api.enuns.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

	public static ClienteDto createNewDto(Cliente cliente) {
		return new ClienteDto(cliente.getNome(),
				cliente.getEmail(),
				cliente.getDataNascimento(),
				cliente.getPerfil(), cliente.getSenha());
	}
}
		
		
