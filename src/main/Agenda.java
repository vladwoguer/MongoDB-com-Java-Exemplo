package main;

import static main.DatabaseController.recuperarContatosPorNome;
import static main.DatabaseController.recuperarTodosContatos;
import static main.DatabaseController.salvarContato;
import static main.DatabaseController.iniciarMongo;

import java.util.Scanner;

/**
 * Exemplo de Agenda com Mongo, curso de Arquitetura Web.
 * 
 * @author Vladwoguer Bezerra / Sp-Lab 2014
 */
public class Agenda {
	public static void main(String[] args) {
		try {
			iniciarMongo(); /*
							 * Aqui inicializa o banco verificando se ele está
							 * disponível
							 */
			controleFluxo();
		} catch (ConexaoIndisponivelException e) {
			System.out.println("Erro 01: Favor verificar conexão com MongoDB");
		}
	}

	/**
	 * Lê do teclado a opção do usuário.
	 */
	private static void controleFluxo() {
		int controle;
		do {
			exibirMenu();
			controle = getTeclado().nextInt();
			switch (controle) {
			case 1:
				exibirContatos();
				break;
			case 2:
				inserirContato();
				break;
			case 3:
				buscarContato();
				break;

			case 4:
				; // para não exibir a msg caso digite 4
				break;

			default:
				System.out.println("Digite um valor válido");
				break;
			}
		} while (controle != 4);
	}

	// Metodos da interface texto

	/**
	 * Busca o contato no banco.
	 */
	private static void buscarContato() {
		System.out.println("Digite o nome do contato:");
		String nome = getTeclado().nextLine();
		for (Contato contato : recuperarContatosPorNome(nome)) {
			System.out.println(contato.toString());
		}
	}

	/**
	 * Insere um contato.
	 */
	private static void inserirContato() {
		System.out.println("Digite o nome do contato:");
		String nome = getTeclado().nextLine();
		System.out.println("Digite o telefone do contato:");
		String telefone = getTeclado().nextLine();

		// Persiste os dados
		salvarContato(new Contato(nome, telefone));
	}

	/**
	 * Exibe todos os contatos.
	 */
	private static void exibirContatos() {
		for (Contato contato : recuperarTodosContatos()) {
			System.out.println(contato.toString());
		}
	}

	/**
	 * Retorna um scanner de teclado ou da entrada padrão.
	 */
	private static Scanner getTeclado() {
		return new Scanner(System.in);
	}

	/**
	 * Exibe o menu da aplicação.
	 */
	private static void exibirMenu() {
		System.out.println("1-Listar Contatos");
		System.out.println("2-Inserir Contato");
		System.out.println("3-Buscar  Contato");
		System.out.println("4 Sair");
	}
}
