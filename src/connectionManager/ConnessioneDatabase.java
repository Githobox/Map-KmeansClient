package connectionManager;

/**
 * Classe ConnessioneDatabase: Classe per la gestione della connessione al database.
 * Questa classe contiene i parametri necessari per la connessione al database e i metodi per ottenere i valori dei parametri.
 */
public class ConnessioneDatabase {
    /**
     * Variabili di istanza.
     */
    private final String indirizzoServer;
    private final String nomeDatabase;
    private final String nomeTabella;
    private final String utenteDatabase;
    private final String passwordDatabase;

    /**
     * Costruttore della classe ConnessioneDatabase.
     * @param indirizzoServer String: indirizzo del server.
     * @param nomeDatabase String: nome del database.
     * @param nomeTabella String: nome della tabella.
     * @param utenteDatabase String: utente del database.
     * @param passwordDatabase String: password del database.
     */
    public ConnessioneDatabase(String indirizzoServer, String nomeDatabase, String nomeTabella,String utenteDatabase, String passwordDatabase) {
        this.indirizzoServer = indirizzoServer;
        this.nomeDatabase = nomeDatabase;
        this.nomeTabella = nomeTabella;
        this.utenteDatabase = utenteDatabase;
        this.passwordDatabase = passwordDatabase;
    }
    /**
     * Metodo per ottenere l'indirizzo del server.
     * @return String: indirizzo del server.
     */
    public String getIndirizzoServer() {
        return indirizzoServer;
    }
    /**
     * Metodo per ottenere il nome del database.
     * @return String: nome del database.
     */
    public String getNomeDatabase() {
        return nomeDatabase;
    }
    /**
     * Metodo per ottenere il nome della tabella.
     * @return String: nome della tabella.
     */
    public String getNomeTabella() {
        return nomeTabella;
    }
    /**
     * Metodo per ottenere l'utente del database.
     * @return String: utente del database.
     */
    public String getUtenteDatabase() {
        return utenteDatabase;
    }
    /**
     * Metodo per ottenere la password del database.
     * @return String: password del database.
     */
    public String getPasswordDatabase() {
        return passwordDatabase;
    }
}
