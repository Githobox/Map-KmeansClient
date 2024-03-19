package com.client.kmeansclient.gui;

import connectionManager.ConnessioneSocket;
import connectionManager.ServerException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
/**
 * ConnessioneController è il controller per la scena di connessione.
 * Questa classe gestisce la connessione al server.
 */
public class ConnessioneController extends BaseSceneController {
    /**
     * Percorso del file FXML per la scena di connessione.
     */
    private static final String CLUSTERPATH = "/com/client/kmeansclient.gui/menuCluster.fxml";
    /**
     * Campo di testo per l'indirizzo IP del server.
     */
    @FXML
    private TextField IpTextField1;

    @FXML
    private TextField PortTextField1;
    /**
     * Pulsante per connettersi al server.
     */
    @FXML
    private Button ConnectButton1;
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore viene utilizzato per istanziare un oggetto ConnessioneController.
     * Non effettua alcuna operazione specifica, ma è necessario per permettere
     * l'istanziazione della classe e l'accesso alla funzionalità fornita da BaseSceneController.
     */
    public ConnessioneController() {}
    /**
     * Gestisce l'evento di clic sul pulsante di connessione.
     * Controllo dell'indirizzo IP e del numero di porta, connessione al server e passaggio alla scena di menuCluster.
     */
    @FXML
    private void handleConnectButton() {
        try {
            String ipAddress = IpTextField1.getText();
            String portNumber = PortTextField1.getText();

            // Controlla se i campi sono vuoti
            if (ipAddress.isEmpty() || portNumber.isEmpty()) {
                showMessage(AlertType.ERROR, "Errore", "Assicurati di inserire un indirizzo IP e un numero di porta.");
                return;
            }
            // Connessione al server
            ConnessioneSocket server = new ConnessioneSocket(ipAddress, Integer.parseInt(PortTextField1.getText()));
            showMessage(AlertType.INFORMATION,"Connessione","Connessione al server riuscita");

            // Inizializza la connessione nel controller corrente
            initializeConnection(server);

            // Creazione di un'istanza di menuClusterController
            menuClusterController menuController = new menuClusterController();

            // Ottieni il riferimento alla scena corrente
            Stage currentStage = (Stage) ConnectButton1.getScene().getWindow();

            // Passa alla nuova scena
            switchToNewScene(currentStage, menuController, CLUSTERPATH);

        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Inserisci un numero valido per la porta");
        } catch (ServerException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore di connessione al server");
        }catch (Exception e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore generale: " + e.getMessage());
        }
    }
    /**
     * Gestisce l'evento di clic sul pulsante di default.
     */
    @FXML
    private void handleDefaultButton() {
        // Imposta i valori di default
        IpTextField1.setText("127.0.0.1");
        PortTextField1.setText("8080");
    }

}
