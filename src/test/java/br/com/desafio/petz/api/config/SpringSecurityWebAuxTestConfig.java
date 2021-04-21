package br.com.desafio.petz.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import br.com.desafio.petz.api.security.JwtUser;
import br.com.desafio.petz.api.security.util.JwtTokenUtil;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Bean
	public UserDetailsService userDetailsService(){
		GrantedAuthority grantAdmin = new SimpleGrantedAuthority("ADMIN");
		GrantedAuthority grantUser= new SimpleGrantedAuthority("USUARIO");
		
		UserDetails userDetailsAdmin = (UserDetails) new JwtUser(1L,"admin@petzapi.com", "admin12345", List.of(grantAdmin, grantUser));
//		UserDetails userDetails = (UserDetails) new JwtUser(2L,"user@petzapi.com", "user12345", List.of(grantUser));
		
		return new InMemoryUserDetailsManager(List.of(userDetailsAdmin));
	}
	
	@Bean
    @Primary
	public SecurityConfiguration securityTest() {
		String token;
		try {
			
			UserDetails userDetails = this.userDetailsService().loadUserByUsername("admin@petzapi.com");
			token = this.jwtTokenUtil.obterToken(userDetails);
		} catch (Exception e) {
			token = "";
		}

		return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER,
				"Authorization", ",");
	}

}
