package com.client.kmeansclient.gui;

import connectionManager.ConnessioneDatabase;
import connectionManager.ConnessioneSocket;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;

/**
 * Classe astratta che rappresenta un controller di scena base.
 * Questa classe contiene metodi comuni per la gestione delle scene.
 */
public abstract class BaseSceneController {
    /**
     * Titolo della applicazione.
     */
    private static final String APPLICATION_TITLE = "Kmeans Client";
    /**
     * Percorso immagine icona.
     */
    private static final String ICON_PATH = "/images/icona.png";
    /**
     * Dimensione di default della larghezza.
     */
    private static final int DEFAULT_WIDTH = 800;
    /**
     * Dimensione di default della altezza.
     */
    private static final int DEFAULT_HEIGHT = 600;
    /**
     * Connessione al server
     */
    protected ConnessioneSocket connessioneSocket;
    /**
     * Connessione al database
     */
    protected ConnessioneDatabase connessioneDatabase;
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore è privato per evitare l'istanziazione diretta di oggetti BaseSceneController.
     * Le sottoclassi dovrebbero fornire i propri costruttori per inizializzare eventuali stati necessari.
     */
    protected BaseSceneController() {}
    /**
     * Inizializza la connessione al server.
     *
     * @param connessioneSocket Connessione al server
     */
    public void initializeConnection(ConnessioneSocket connessioneSocket) {
        this.connessioneSocket = connessioneSocket;
    }
    /**
     * Inizializza la connessione al database.
     *
     * @param connessioneDatabase Connessione al database
     */
    public void initializeDatabase(ConnessioneDatabase connessioneDatabase) {
        this.connessioneDatabase = connessioneDatabase;
    }
    /**
     * Cambia la scena corrente con una nuova scena.
     *
     * @param currentStage Stage corrente
     * @param controller   Controller della nuova scena
     * @param fxmlPath     Percorso del file FXML della nuova scena
     * @param <T>          Tipo del controller della nuova scena
     */
    public <T extends BaseSceneController> void switchToNewScene(Stage currentStage, T controller, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Ottieni il controller del nuovo stage dinamicamente
            T newController = loader.getController();

            // Passa la connessione socket al controller del nuovo stage
            newController.initializeConnection(connessioneSocket);

            // Configura la nuova scena
            Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            //imposta il metodo di chiusura della connessione
            currentStage.setOnCloseRequest(event -> closeConnection());
            // Configura il nuovo stage
            Image icon = new Image(getClass().getResourceAsStream(ICON_PATH));
            currentStage.setTitle(APPLICATION_TITLE);
            currentStage.getIcons().add(icon);
            currentStage.setResizable(true);
            currentStage.setScene(scene);
            currentStage.show();

            if (newController instanceof resultController resultController) {
                resultController.initializeDatabase(connessioneDatabase);
                resultController.stampaRisultato(connessioneSocket);
            }
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante il caricamento della nuova scena");
        }
    }
    /**
     * Chiude la connessione al server.
     */
    public void closeConnection() {
        if(connessioneSocket != null) {
            try{
                this.connessioneSocket.getOutputStream().writeObject(5);
            } catch (IOException e) {
                showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante la chiusura della connessione al server");
            }
        }
    }
    /**
     * Mostra un messaggio di alert.
     * Serve per mostrare messaggi di errore o conferma.
     *
     * @param alertType Tipo di alert
     * @param title     Titolo dell'alert
     * @param message   Messaggio dell'alert
     */
    protected void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON_PATH)));

        alert.showAndWait();
    }
}

