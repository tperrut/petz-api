package br.com.desafio.petz.api.util;

public class EncriptySenhaUtil {

	public static void main(String[] args) {
		String senhaEncoded = PasswordUtils.gerarBCrypt("123456");
		System.out.println("Senha encoded: " + senhaEncoded);	
		
		senhaEncoded = PasswordUtils.gerarBCrypt("123456");
		System.out.println("Senha encoded novamente: " + senhaEncoded);	

		System.out.println("Senha válida: " + PasswordUtils.senhaValida("123456", senhaEncoded));	
	
	}
	
}
	
