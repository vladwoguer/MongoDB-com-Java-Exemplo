package main;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Controlador do Database.
 * 
 * @author Vladwoguer Bezerra
 */
public class DatabaseController {
	private static final String COLECAO_AGENDA = "minhaAgenda";
	private static final String BANCO = "test";
	private static final String URL_MONGO = "localhost";
	private static final int PORTA = 27017;
	private static MongoClient banco;

	/**
	 * Recupera os contatos que tenham nome igual ao passado como par�metro.
	 * 
	 * @param nome
	 *            Nome a ser buscado
	 * @return Uma lista de contatos que satisfa�am.
	 */
	public static List<Contato> recuperarContatosPorNome(String nome) {
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

	/**
	 * Recupera todos os contatos.
	 * 
	 * @return Uma lista com todos os contatos.
	 */
	public static List<Contato> recuperarTodosContatos() {
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

	/**
	 * Salva o contato passado como par�metro.
	 * 
	 * @param novoContato
	 *            Contato a ser persitido.
	 */
	public static void salvarContato(Contato novoContato) {
		DBCollection coll = getColecao();
		BasicDBObject contato = new BasicDBObject("nome", novoContato.getNome())
				.append("telefone", novoContato.getTelefone());
		coll.insert(contato);
	}

	/**
	 * Retorna a cole��o minhaAgenda.
	 * 
	 * @return Um BDCollection da cole��o.
	 */
	private static DBCollection getColecao() {
		// Inicializa o Banco de dados
		DB db = banco.getDB(BANCO);
		DBCollection coll = db.getCollection(COLECAO_AGENDA);
		return coll;
	}

	/**
	 * Inst�ncia a conex�o com o MongoDB
	 * 
	 * @throws ConexaoIndisponivelException
	 *             Se a conex�o com o banco n�o estiver dipon�vel
	 */
	public static void iniciarMongo() throws ConexaoIndisponivelException {
		try {
			if (banco == null) {
				banco = new MongoClient(URL_MONGO, PORTA);
			}
		} catch (UnknownHostException e) {
			throw new ConexaoIndisponivelException();
		}
	}
}
