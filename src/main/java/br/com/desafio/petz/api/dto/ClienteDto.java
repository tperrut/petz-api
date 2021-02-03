package br.com.desafio.petz.api.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClienteDto {
	
	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;
	
	@NotEmpty(message = "Email não pode ser vazio")
	private String email;

	@NotNull(message="Data Nascimento não pode ser vazia.")
	@DateTimeFormat(iso = ISO.TIME, pattern="yyyy-MM-dd")
	private LocalDate dataNascimento;
	
//	@JsonInclude(JsonInclude.Include.NON_EMPTY)
//	private List<PetDto> pets;
	
}
