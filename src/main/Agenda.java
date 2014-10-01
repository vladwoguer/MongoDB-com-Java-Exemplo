package main;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * Exemplo de Agenda com Mongo
 * 
 * @author Vladwoguer Bezerra Date mm/dd/yyyy 09/30/2014
 */
public class Agenda {
	private static final String COLECAO_AGENDA = "minhaAgenda";
	private static final String BANCO = "test";
	private static final String URL_MONGO = "localhost";
	private static final int PORTA = 27017;
	private static MongoClient banco;

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int controle;
		try {
			iniciarMongo();
			do {
				exibirMenu();
				controle = teclado.nextInt();
				switch (controle) {
				case 1:
					exibirContatos();
					break;
				case 2:
					inserirContato();
					break;
				case 3:
					System.out.println("Digite o nome do contato:");
					buscarContato(getTeclado().nextLine());
					break;

				case 4:
					;
					break;

				default:
					System.out.println("Digite um valor válido");
					break;
				}
			} while (controle != 4);

		} catch (ConexaoIndisponivelException e) {
			System.out.println("Erro 01: Favor verificar conexão com MongoDB");
		}
	}

	// Metodos da interface texto

	private static void buscarContato(String nome) {
		for (Contato contato : recuperarContatosPorNome(nome)) {
			System.out.println(contato.toString());
		}
	}

	private static void inserirContato() {
		System.out.println("Digite o nome do contato:");
		String nome = getTeclado().nextLine();
		System.out.println("Digite o telefone do contato:");
		String telefone = getTeclado().nextLine();

		// Persiste os dados
		salvarContato(new Contato(nome, telefone));
	}

	private static void exibirContatos() {
		for (Contato contato : recuperarTodosContatos()) {
			System.out.println(contato.toString());
		}
	}

	private static Scanner getTeclado() {
		return new Scanner(System.in);
	}

	private static void exibirMenu() {
		System.out.println("1-Listar Contatos");
		System.out.println("2-Inserir Contato");
		System.out.println("3-Buscar  Contato");
		System.out.println("4 Sair");
	}

	// Metodos que Modificam o BD
	
	private static List<Contato> recuperarContatosPorNome(String nome) {
		List<Contato> contatos = new LinkedList<Contato>();
		BasicDBObject query = new BasicDBObject("nome", nome);
		DBCursor cursor = getColecao().find(query);
		try {
			while (cursor.hasNext()) {
				DBObject o = cursor.next();
				contatos.add(new Contato((String) o.get("nome"), (String) o
						.get("telefone")));
			}
		} finally {
			cursor.close();
		}
		return contatos;
	}

	private static List<Contato> recuperarTodosContatos() {
		List<Contato> contatos = new LinkedList<Contato>();
		DBCursor cursor = getColecao().find();
		try {
			while (cursor.hasNext()) {
				DBObject o = cursor.next();
				contatos.add(new Contato((String) o.get("nome"), (String) o
						.get("telefone")));
			}
		} finally {
			cursor.close();
		}
		return contatos;
	}

	private static void salvarContato(Contato novoContato) {
		DBCollection coll = getColecao();
		BasicDBObject contato = new BasicDBObject("nome", novoContato.getNome())
				.append("telefone", novoContato.getTelefone());
		coll.insert(contato);
	}

	private static DBCollection getColecao() {
		// Inicializa o Banco de dados
		DB db = banco.getDB(BANCO);
		DBCollection coll = db.getCollection(COLECAO_AGENDA);
		return coll;
	}

	private static void iniciarMongo() throws ConexaoIndisponivelException {
		try {
			if (banco == null) {
				banco = new MongoClient(URL_MONGO, PORTA);
			}
		} catch (UnknownHostException e) {
			throw new ConexaoIndisponivelException();
		}
	}
}
