package com.client.kmeansclient.gui;

import connectionManager.ConnessioneSocket;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * Classe resultController: Controller associato alla finestra di visualizzazione dei risultati del clustering.
 * Questa classe si occupa di gestire la visualizzazione dei risultati del clustering e di fornire all'utente la possibilità
 * di ripetere l'iterazione, salvare i risultati su file e tornare al menu principale.
 */
public class resultController extends BaseSceneController {
    /**
     * Percorso del file FXML per la scena di menuCluster.
     */
    private static final String CLUSTERPATH = "/com/client/kmeansclient.gui/menuCluster.fxml";
    /**
     * Variabili di istanza.
     */
    @FXML
    private TextArea resultTextArea;
    @FXML
    private Button BackButton;
    @FXML
    private TextField IterateTextField;
    /**
     * Variabile per ottenere il numero di iterazioni dal server.
     */
    private Object iterations;
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore viene utilizzato per istanziare un oggetto resultController.
     * Non effettua alcuna operazione specifica, ma è necessario per permettere
     * l'istanziazione della classe e l'accesso alla funzionalità fornita da BaseSceneController.
     */
    public resultController() {}
    /**
     * Metodo per aggiornare il contenuto della TextArea dei risultati.
     * @param result Stringa da inserire nella TextArea.
     */
    private void updateResultTextArea(String result) {
            resultTextArea.appendText(result);
    }
    /**
     * Metodo per stampare i risultati del clustering.
     * Questo metodo legge i risultati del clustering dal server e li stampa nella TextArea dei risultati.
     * @param connessioneSocket ConnessioneSocket: connessione al server.
     */
    public void stampaRisultato(ConnessioneSocket connessioneSocket) {
        Object tempIterations; // Variabile temporanea per le iterazioni
        String result; // Variabile per il risultato
        try {
            // Verifica se la connessione al server è stata passata correttamente
            if (connessioneSocket == null || connessioneSocket.getSocket() == null || !connessioneSocket.getSocket().isConnected()) {
                showMessage(Alert.AlertType.ERROR,"Errore","Connessione al server non inizializzata correttamente");
            }

            // Leggi il risultato dal server
            //if (((String) connessioneSocket.getInputStream().readObject()).equals("OK")) {
                // Leggi le iterazioni dal server
                tempIterations = connessioneSocket.getInputStream().readObject();
                // Leggi il risultato dal server
                result = (String) connessioneSocket.getInputStream().readObject();
                // Aggiorna la TextArea dei risultati
                updateResultTextArea("Cluster Generati\n" + result + "\n");
                // Assegna il valore di tempIterations a iterations
                iterations = tempIterations;
            //}
        } catch (IOException | ClassNotFoundException | IllegalStateException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore nella connessione al server"+e.getMessage());
        }
    }
    /**
     * Metodo associato all'azione di click del RepeatButton.
     * Questo metodo ripete l'iterazione del clustering.
     * @throws IOException Eccezione di I/O.
     * @throws ClassNotFoundException  Eccezione di classe non trovata.
     */
    @FXML
    private void repeatIteration() throws IOException, ClassNotFoundException {
        String resp;
        // Verifica se il campo di testo delle iterazioni è vuoto
        if(IterateTextField.getText().isEmpty()){
            showMessage(Alert.AlertType.ERROR,"Errore","Inserisci un numero di iterazioni");
        }
        else{
            // Invia il numero di iterazioni al server
            this.connessioneSocket.getOutputStream().writeObject(2);
            this.connessioneSocket.getOutputStream().writeObject(Integer.parseInt(IterateTextField.getText()));
            resp = (String) this.connessioneSocket.getInputStream().readObject();
            if (resp.equals("OK")) {
                // Aggiorna la TextArea dei risultati
                updateResultTextArea("Iterazione ripetuta correttamente\n");
                // Stampa i risultati
                stampaRisultato(connessioneSocket);
            } else {
                showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: \n" + resp);
            }
        }
    }
    /**
     * Metodo associato all'azione di click del SaveButton.
     * Questo metodo salva i risultati del clustering su file.
     * @throws IOException Eccezione di I/O.
     * @throws ClassNotFoundException Eccezione di classe non trovata.
     */
    @FXML
    private void saveOnFile () throws IOException, ClassNotFoundException {
        String filename;
        // Dice al server di salvare i risultati su file
        this.connessioneSocket.getOutputStream().writeObject(3);
        // Crea il nome del file
        filename = (String) connessioneDatabase.getNomeDatabase()+"_"+connessioneDatabase.getNomeTabella()+"_"+iterations;
        // Invia il nome del file al server
        this.connessioneSocket.getOutputStream().writeObject(filename);
        // Riceve la conferma dal server
        if(((String)connessioneSocket.getInputStream().readObject()).equals("OK")){
            // Aggiorna la TextArea dei risultati
            showMessage(Alert.AlertType.INFORMATION,"Salvataggio","File salvato correttamente");
        }
        else{
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante il salvataggio del file");
        }
    }
    /**
     * Metodo associato all'azione di click del BackButton.
     * Questo metodo torna al menu principale.
     */
    @FXML
    private void backToMenuCluster() {
        // Ottieni il riferimento alla scena corrente
        Stage currentStage = (Stage) BackButton.getScene().getWindow();
        // Creazione di un'istanza di menuClusterController
        menuClusterController menuController = new menuClusterController();
        // Passa alla nuova scena
        switchToNewScene(currentStage, menuController, CLUSTERPATH);
    }
}
