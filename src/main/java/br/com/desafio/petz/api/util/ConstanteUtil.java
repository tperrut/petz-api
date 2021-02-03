package br.com.desafio.petz.api.util;

public class ConstanteUtil {
	
	//Home
	public static final String WELCOME = "Welcome to PETZ-API";
	public static final String SWAGGER = "swagger-ui.html";
	
	//Logger
	public static final String CANCELAR_CLIENTE = "[CANCELAR_CLIENTE] [id] : ";
	public static final String DETALHAR_CLIENTE = "[DETALHAR_CLIENTE] [id] : ";
	public static final String PAGAR_CLIENTE = "[PAGAR_CLIENTE] [id] : ";
	public static final String CRIAR_CLIENTE = "[CRIAR_CLIENTE] [cliente] : ";
	public static final String FIND_BY_NAME = "[FIND_BY_NAME] [cliente] : ";
	public static final String LISTAR_CLIENTES = "[LISTAR_CLIENTES]";
	public static final String LISTAR_CLIENTES_PAGED =  "[LISTAR_CLIENTES_PAGED]";
	public static final String USER_DETAIL_NOME = "[USER_DETAIL] nome: ";

	//exception
	public static final String CLIENTE_NOT_FOUND = "CLIENTE não encontrado para o ID: ";
	public static final String CLIENTE_NOT_FOUND_BY_CLIENTE ="CLIENTE não encontrado para o CLIENTE ";

	//Erro 
	public static final String ERRO ="Erro: ";
	public static final String INTERNAL_SERVER_ERROR="INTERNAL_SERVER_ERROR : [{}]";
	public static final String ERRO_CLIENTE_POR_NOME= "Erro ao consultar CLIENTE por [nome={}]. Contate o admin!";
	public static final String ERRO_LISTAR_CLIENTE= " ao listar CLIENTEs. Contate o admin!";
	public static final String ERRO_PAGAR = " Contate o admin! Erro ao Pagar CLIENTE [ID={}]. ";
	public static final String ERRO_CRIAR_CLIENTE=" Contate o admin! Erro ao criar CLIENTE .";
	public static final String ERRO_CANCELAR_CLIENTE=" Contate o admin! Erro ao Cancelar CLIENTE [ID={}].";
	public static final String ERRO_NOT_PENDING = "CLIENTE [ID=%s] deve estar com o status PENDING";
	


}	