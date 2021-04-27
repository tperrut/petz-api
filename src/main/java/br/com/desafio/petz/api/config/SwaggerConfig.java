package br.com.desafio.petz.api.config;

import br.com.desafio.petz.api.security.JwtUser;
import br.com.desafio.petz.api.security.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.desafio.petz.api")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Swagger API")
				.description("Documentação da API PETZ com Swagger").version("0.0.1").build();
	}
	
	@Bean
    public UserDetailsService userDetailsService(){
		GrantedAuthority grantAdmin = new SimpleGrantedAuthority("ADMIN");
		GrantedAuthority grantUser= new SimpleGrantedAuthority("USUARIO");

		UserDetails userDetailsAdmin =  new JwtUser(1L,"admin@petzapi.com", "admin12345", Arrays.asList(grantAdmin));
		UserDetails userDetails = new JwtUser(2L,"user@petzapi.com", "user12345", Arrays.asList(grantUser));
		
		return new InMemoryUserDetailsManager(Arrays.asList(userDetailsAdmin,userDetails));
	}

	@Bean
	public SecurityConfiguration security(JwtTokenUtil tokenUtil, UserDetailsService userDetailsService ) {
		String token;
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername("admin@petzapi.com");
			token = tokenUtil.obterToken(userDetails);
		} catch (Exception e) {
			token = "";
		}

		return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER,
				"Authorization", ",");
	}
}