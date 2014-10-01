package main;

/**
 * Exce��o a ser lan�ada caso o MongoDB n�o esteja dispon�vel.
 * @author Vladwoguer Bezerra
 *
 */
public class ConexaoIndisponivelException extends Exception{
	public ConexaoIndisponivelException(){
		super("O banco de dados n�o est� dispon�vel");
	}
}
