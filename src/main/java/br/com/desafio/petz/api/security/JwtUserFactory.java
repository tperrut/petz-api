package br.com.desafio.petz.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.desafio.petz.api.enuns.PerfilEnum;
import br.com.desafio.petz.api.model.Cliente;

public class JwtUserFactory {
	
	private JwtUserFactory() {
	}

	/**
	 * Faz a conversão e cria um JwtUser baseado nas infos do Cliente.
	 * 
	 * @param cliente
	 * @return JwtUser
	 */
	public static JwtUser create(Cliente cliente) {
		return new JwtUser(cliente.getId(), cliente.getEmail(), cliente.getSenha(),
				mapToGrantedAuthorities(cliente.getPerfil()));
	}

	/**
	 *  Faz a conversão do perfil do user para o fmt do Spring Security.
	 * 
	 * @param perfilEnum
	 * @return List<GrantedAuthority>
	 */
	private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
		return authorities;
	}
}
