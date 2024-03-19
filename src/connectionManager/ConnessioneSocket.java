package connectionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe ConnessioneSocket: Classe per la gestione della connessione al server.
 */
public class ConnessioneSocket {
    /**
     * Variabili di istanza.
     */
    private final Socket socket;
    private final String ip;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    /**
     * Costruttore della classe ConnessioneSocket.
     * @param ip String: indirizzo IP del server.
     * @param port int: porta del server.
     * @throws ServerException Eccezione lanciata in caso di errore durante la connessione al server.
     */
    public ConnessioneSocket(String ip, int port) throws ServerException {
        try{
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            this.ip = ip;
        }
        catch (UnknownHostException e) {
            throw new ServerException("Host Sconosiuto");
        } catch (ConnectException e){
            throw new ServerException("Impossibile stabilire la connessione al server");
        } catch (IOException e){
            throw new ServerException("Errore durante la connesione al server");
        }
    }
    /**
     * Metodo per ottenere il socket.
     * @return Socket: socket del server.
     */
    public Socket getSocket(){
        return socket;
    }
    /**
     * Metodo per ottenere l'indirizzo IP del server.
     * @return String: indirizzo IP del server.
     */
    public String getIp(){
        return ip;
    }
    /**
     * Metodo per ottenere il flusso di output.
     * @return out: flusso di output.
     */
    public ObjectOutputStream getOutputStream() {
        return out;
    }
    /**
     * Metodo per ottenere il flusso di input.
     * @return in: flusso di input.
     */
    public ObjectInputStream getInputStream() {
        return in;
    }
    /**
     * Metodo per chiudere la connessione.
     * @throws ServerException Eccezione lanciata in caso di errore durante la chiusura della connessione.
     */
    public void close() throws ServerException {
        try{
            out.close();
            in.close();
            socket.close();
        } catch (IOException e){
            throw new ServerException("Errore durante la chiusura della connessione");
        }
    }
}
