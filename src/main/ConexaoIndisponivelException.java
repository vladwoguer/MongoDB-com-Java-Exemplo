package main;

/**
 * Exceção a ser lançada caso o MongoDB não esteja disponível.
 * @author Vladwoguer Bezerra
 *
 */
public class ConexaoIndisponivelException extends Exception{
	public ConexaoIndisponivelException(){
		super("O banco de dados não está disponível");
	}
}
