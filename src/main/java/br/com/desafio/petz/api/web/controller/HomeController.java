package br.com.desafio.petz.api.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.util.JSONPObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.petz.api.util.ConstanteUtil;

@RestController
public class HomeController {
	
	@Value(value="${spring.server.url}")
	String server;
	
	
	@GetMapping(path="/",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('USER')")
	public Object home() {
		Map<String,String> map= new HashMap<String,String>();
		map.put("msg", ConstanteUtil.WELCOME);
		map.put("doc", this.server+ConstanteUtil.SWAGGER);
		JSONPObject response = new JSONPObject("",map);
		return response.getValue();
	}
}
