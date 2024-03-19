package com.client.kmeansclient.gui;

import connectionManager.ConnessioneDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * Controller per la finestra di menuCluster.fxml
 */
public class menuClusterController extends BaseSceneController {
    /**
     * Percorso del file FXML per la scena di risultato.
     */
    private static final String RESULTPATH = "/com/client/kmeansclient.gui/result.fxml";
    /**
     * Percorso del file FXML per la scena di menuConnect.
     */
    private static final String MENUCONNECTPATH = "/com/client/kmeansclient.gui/menuUI.fxml";
    /**
     * Variabili di istanza.
     */
    @FXML
    private TextField DataBaseTextField;
    @FXML
    private TextField TabellaTextField;
    @FXML
    private TextField UtenteTextField;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private TextField IterateTextField;
    @FXML
    private Button DatabaseButton;
    @FXML
    private Button FileButton;
    @FXML
    private Button BackButton;
    /**
     * Oggetto resul che servirà per il cambio di scena.
     */
    private resultController result;
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore viene utilizzato per istanziare un oggetto menuClusterController.
     * Non effettua alcuna operazione specifica, ma è necessario per permettere
     * l'istanziazione della classe e l'accesso alla funzionalità fornita da BaseSceneController.
     */
    public menuClusterController() {}
    /**
     * Gestisce l'evento di clic sul pulsante Database.
     * Questo metodo invia i dati del database al server.
     * Effettua il learning dal database e mostra i risultati.
     */
    @FXML
    private void handleDatabase() {
        try {
            String database = DataBaseTextField.getText();
            String tableName = TabellaTextField.getText();
            String utente = UtenteTextField.getText();
            String password = PasswordTextField.getText();
            String iterations = IterateTextField.getText();

            // Controlla se i campi sono vuoti
            if (database.isEmpty() || tableName.isEmpty() || utente.isEmpty() || password.isEmpty() || iterations.isEmpty()) {
                showMessage(Alert.AlertType.ERROR, "Errore", "Assicurati di compilare tutti i campi.");
                return;
            }

            connessioneDatabase = new ConnessioneDatabase(this.connessioneSocket.getIp(), database, tableName, utente, password);
            String resp = connessioneServerDataBase(connessioneDatabase);
            System.out.println(resp);
            if (resp.equals("OK")) {

                this.connessioneSocket.getOutputStream().writeObject(2);
                this.connessioneSocket.getOutputStream().writeObject(Integer.parseInt(IterateTextField.getText()));
                resp = (String) this.connessioneSocket.getInputStream().readObject();
                if(resp.equals("OK")) {
                    result = new resultController();
                    Stage currentStage = (Stage) DatabaseButton.getScene().getWindow();
                    switchToNewScene(currentStage, result, RESULTPATH);
                }
                else {
                    showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: \n" + resp);
                }
            } else {
                showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: " + resp);
            }
        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Numero di iterazioni non valido.");
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione del database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Gestisce l'evento di clic sul pulsante File.
     * Questo metodo invia i dati del database al server.
     * Effettua il learning da file e mostra i risultati.
     */
    @FXML
    private void handleFile() {
        try {
            String database = DataBaseTextField.getText();
            String tableName = TabellaTextField.getText();
            String utente = UtenteTextField.getText();
            String password = PasswordTextField.getText();
            String iterations = IterateTextField.getText();

            // Controlla se i campi sono vuoti
            if (database.isEmpty() || tableName.isEmpty() || utente.isEmpty() || password.isEmpty() || iterations.isEmpty()) {
                showMessage(Alert.AlertType.ERROR, "Errore", "Assicurati di compilare tutti i campi.");
                return;
            }

            connessioneDatabase = new ConnessioneDatabase(this.connessioneSocket.getIp(), database, tableName, utente, password);
            String resp = connessioneServerDataBase(connessioneDatabase);
            if (resp.equals("OK")) {
                connessioneSocket.getOutputStream().writeObject(4);
                String filename = database + "_" + tableName + "_" + iterations;
                int iter = Integer.parseInt(iterations);
                connessioneSocket.getOutputStream().writeObject(filename);
                connessioneSocket.getOutputStream().writeObject(iter);
                resp = (String) connessioneSocket.getInputStream().readObject();
                if(resp.equals("OK")) {
                    result = new resultController();
                    Stage currentStage = (Stage) FileButton.getScene().getWindow();
                    switchToNewScene(currentStage, result, RESULTPATH);
                }
                else {
                    showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: \n" + resp);
                }

            } else {
                showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: " + resp);
            }
        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Numero di iterazioni non valido.");
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione del di ritrovamento del file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Inizializza la connessione al database del server.
     *
     * @param connessioneDatabase Connessione al database
     */
    private String connessioneServerDataBase(ConnessioneDatabase connessioneDatabase) {

        try {
            this.connessioneSocket.getOutputStream().writeObject(1);
            this.connessioneSocket.getOutputStream().writeObject(this.connessioneSocket.getIp());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getNomeDatabase());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getNomeTabella());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getUtenteDatabase());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getPasswordDatabase());

            String resp = (String) this.connessioneSocket.getInputStream().readObject();
            if (resp.equals("OK")) {
                showMessage(Alert.AlertType.INFORMATION,"DataSet",(String) this.connessioneSocket.getInputStream().readObject());
            } else {
                showMessage(Alert.AlertType.ERROR,"Errore",resp);
            }
            return resp;
        } catch (IOException | ClassNotFoundException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione del database: " + e.getMessage());
            return null;
        }
    }
    /**
     * Gestisce l'evento di clic sul pulsante Indietro.
     * Questo metodo torna alla scena di menuConnect.
     */
    @FXML
    private void backToMenuConnect() {
        // Ottieni il riferimento alla scena corrente
        Stage currentStage = (Stage) BackButton.getScene().getWindow();
        // Creazione di un'istanza di menuClusterController
        ConnessioneController connessioneController = new ConnessioneController();
        // Passa alla nuova scena
        switchToNewScene(currentStage, connessioneController, MENUCONNECTPATH);
    }
}
