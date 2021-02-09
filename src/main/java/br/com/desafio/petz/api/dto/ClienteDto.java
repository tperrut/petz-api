package br.com.desafio.petz.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {

	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;

	@NotEmpty(message = "Email não pode ser vazio")
	private String email;

	@DateTimeFormat(iso = ISO.TIME, pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;

}
