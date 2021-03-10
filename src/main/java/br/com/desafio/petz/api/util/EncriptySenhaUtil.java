package br.com.desafio.petz.api.util;

public class EncriptySenhaUtil {
	
	public static void main(String[] args) {
		
		
		String senhaEncoded = PasswordUtils.gerarBCrypt("12345");
		System.out.println("Senha encoded: " + senhaEncoded);	

		senhaEncoded = "$2a$10$ZA4vDzmPu5IRP5iAgrZypO4pHzZvN/VkyRuvQSCkzqXIz3gS4Cti6";
//		System.out.println("Senha encoded novamente: " + senhaEncoded);	
		
		System.out.println("Senha válida: " + PasswordUtils.senhaValida("Fulano de Tal", senhaEncoded));	
		
		
	}
	
}
	
