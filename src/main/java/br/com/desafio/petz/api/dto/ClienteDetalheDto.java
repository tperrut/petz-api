package br.com.desafio.petz.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter 
public class ClienteDetalheDto {
	
	private Long id;
	private String nome;
	private String email;
	private LocalDate dataNascimento;
	
	private ClienteDetalheDto(BuilderDetalheDto builder) {
		this.nome = builder.nome;
		this.dataNascimento = builder.dataNascimento;
		this.id = builder.id;
		this.email = builder.email;
	}

	
	public static BuilderDetalheDto builder() {
		return new BuilderDetalheDto();
	}

	public static final class BuilderDetalheDto {
		private String nome;
		private String email;
		private LocalDate dataNascimento;
		private Long id;
		

		private BuilderDetalheDto() {
		}

		public BuilderDetalheDto email(String email) {
			this.email = email;
			return this;
		}
		
		public BuilderDetalheDto nome(String name) {
			this.nome = name;
			return this;
		}

		public BuilderDetalheDto dataNascimento(LocalDate nascimento) {
			this.dataNascimento = nascimento;
			return this;
		}

		public BuilderDetalheDto id(Long id) {
			this.id = id;
			return this;
		}

		public ClienteDetalheDto build() {
			return new ClienteDetalheDto(this);
		}

	

	}
	
	
}
