package connectionManager;
/**
 * Classe ServerException: eccezione personalizzata per gestire gli errori del server.
 */
public class ServerException extends Exception{
    /**
     * Costruttore di default.
     */
    public ServerException() {
        super("Errore nel server");
    }
    /**
     * Costruttore con messaggio.
     * @param msg String: messaggio di errore.
     */
    public ServerException(String msg){
        super(msg);
    }
}
