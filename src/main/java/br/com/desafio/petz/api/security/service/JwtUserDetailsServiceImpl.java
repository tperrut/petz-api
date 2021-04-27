package br.com.desafio.petz.api.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.desafio.petz.api.model.Cliente;
import br.com.desafio.petz.api.security.JwtUserFactory;
import br.com.desafio.petz.api.service.ClienteService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private ClienteService clienteService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Cliente> cliente = clienteService.buscarPorEmail(username);

		if (cliente.isPresent()) {
			return JwtUserFactory.create(cliente.get());
		}

		throw new UsernameNotFoundException("Email não encontrado.");
	}

	

}
