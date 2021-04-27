package br.com.desafio.petz.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.desafio.petz.api.enuns.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClienteDto {

	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;

	@Email(message = "Por favor digite um email válido!")
	private String email;

	@DateTimeFormat(iso = ISO.TIME, pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;
	
	private PerfilEnum perfil;
	
	@NotEmpty(message = "Senha é obrigatória!")
	private String senha;
	
}
