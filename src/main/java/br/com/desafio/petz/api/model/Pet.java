package br.com.desafio.petz.api.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import br.com.desafio.petz.api.enuns.EnumTipo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet implements Serializable {

	private static final long serialVersionUID = 2136959624933902915L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@NotEmpty(message = "Nome não pode ser vazio")
	@Column(nullable = false)
	@ApiModelProperty(notes = "Nome do Pet", name="name",required=true,value="Brutus")
	private String nome;

	@Column(nullable = true)
	private String raca;

	@Column(nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private EnumTipo tipo;

	@ManyToOne
	@JoinColumn(name = "id_dono_fk")
	private Cliente dono;

	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "data_nascimento", nullable = true, columnDefinition = "DATE")
	private LocalDate dataNascimento;

	public Pet(@NotEmpty(message = "Nome não pode ser vazio") String nome, String raca, EnumTipo tipo, Cliente dono,
			LocalDate dataNascimento) {
		super();
		this.nome = nome;
		this.raca = raca;
		this.tipo = tipo;
		this.dono = dono;
		this.dataNascimento = dataNascimento;
	}



}
