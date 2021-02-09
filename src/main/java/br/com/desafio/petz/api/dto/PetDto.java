package br.com.desafio.petz.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.model.enuns.EnumTipo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PetDto {
	
	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;
	
	@NotNull(message="Data Nascimento não pode ser vazia.")
	@DateTimeFormat(iso = ISO.TIME, pattern="yyyy-MM-dd")
	private LocalDate dataNascimento;
	
	@NotEmpty(message = "Dono não pode ser vazio!")
	private String idDono;
	
	@JsonIgnore
	private Cliente dono;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EnumTipo tipo;	
	
	private String raca;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public PetDto(Builder builderPetDto) {
		this.nome = builderPetDto.nome;
		this.dataNascimento= builderPetDto.dataNascimento;
		this.idDono = builderPetDto.idDono;	
		this.dono = builderPetDto.dono;
		this.raca = builderPetDto.raca;
		this.tipo = builderPetDto.tipo; 
	}

	public static final class Builder {
		private Cliente dono;
		private String raca;
		private String nome;
		private String idDono;
		private EnumTipo tipo;
		private LocalDate dataNascimento; 
		
		private Builder() {
		}
		
		public Builder nome(String nome) {
			this.nome = nome;
			return this;
		}
		
		public Builder dono(Cliente dono) {
			this.dono = dono;
			return this;
		}
		
		public Builder idDono(String id) {
			this.idDono= id;
			return this;
		}
		
		public Builder raca(String raca) {
			this.raca= raca;
			return this;
		}
		
		public Builder tipo(EnumTipo e) {
			this.tipo= e;
			return this;
		}
		
		public Builder dataNascimento(LocalDate date) {
			this.dataNascimento = date;
			return this;
		}
		
		public PetDto build() {
			return new PetDto(this);
		}


	}
}
