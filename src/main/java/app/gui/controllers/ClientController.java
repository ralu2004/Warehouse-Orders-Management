package app.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import app.bll.ClientBLL;
import app.gui.displayUtils.TableViewBuilder;
import app.model.Client;
import javafx.application.Platform;

import java.util.List;

/**
 * JavaFX controller class responsible for handling user interactions for client operations.
 * <p>
 * Contains methods for adding, updating, deleting, and displaying clients in a {@link TableView}.
 * Interacts with the {@link ClientBLL} layer to perform business logic.
 */
public class ClientController {

    /** Business Logic Layer instance for {@link ClientBLL}*/
    private final ClientBLL clientBLL = new ClientBLL();

    /** Text Field for entering new client's first name */
    @FXML private TextField newFirstNameField;

    /** Text Field for entering new client's last name */
    @FXML private TextField newLastNameField;

    /** Text Field for entering new client's email */
    @FXML private TextField newEmailField;

    /** Text Field for entering new client's address */
    @FXML private TextField newAddressField;

    /** Text Field showing the selected client's ID for editing */
    @FXML private TextField editIdField;

    /** Text Field for editing the selected client's first name */
    @FXML private TextField editFirstNameField;

    /** Text Field for editing the selected client's last name */
    @FXML private TextField editLastNameField;

    /** Text Field for editing the selected client's email */
    @FXML private TextField editEmailField;

    /** Text Field for editing the selected client's address */
    @FXML private TextField editAddressField;

    /** Text Field for entering the client ID to be deleted */
    @FXML private TextField deleteIdField;

    /** TableView displaying all clients */
    @FXML private TableView<Client> clientTable;

    /**
     * Default constructor for ClientController.
     */
    public ClientController() {}

    /**
     * Initializes the controller.
     * Creates and loads the client table.
     */
    @FXML
    public void initialize() {
        clientBLL.createTable();
        loadClientTable();
    }

    /**
     * Loads the client table with data and refreshes it.
     */
    private void loadClientTable() {
        List<Client> clients = clientBLL.findAllClients();
        TableView<Client> newTable = TableViewBuilder.buildTableView(clients, Client.class);

        newTable.setOnMouseClicked(this::handleTableClick);
        clientTable.getColumns().setAll(newTable.getColumns());
        clientTable.setItems(newTable.getItems());
    }

    /**
     * Handles the action of adding a new client.
     * Validates the input fields and inserts the client using {@link ClientBLL}.
     */
    @FXML
    public void handleAddClient() {
        String firstName = newFirstNameField.getText();
        String lastName = newLastNameField.getText();
        String email = newEmailField.getText();
        String address = newAddressField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        Client newClient = new Client(firstName, lastName, email, address);
        try{
            clientBLL.insertClient(newClient);
        } catch (IllegalArgumentException e) {
            Platform.runLater(() -> showAlert(e.getMessage()));
            return;
        }
        clearFields();
        loadClientTable();
    }

    /**
     * Handles the action of updating an existing client.
     * Gathers field values, validates them, and performs the update.
     */
    @FXML
    public void handleUpdateClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("No client selected!");
            return;
        }

        int id = selectedClient.getId();
        String firstName = editFirstNameField.getText();
        String lastName = editLastNameField.getText();
        String email = editEmailField.getText();
        String address = editAddressField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        Client updatedClient = new Client(id, firstName, lastName, email, address);
        try{
            clientBLL.updateClient(updatedClient);
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
        clearFields();
        loadClientTable();
    }

    /**
     * Handles the deletion of a client based on the ID provided in {@code deleteIdField}.
     */
    @FXML
    public void handleDeleteClient() {
        try {
            int clientId = Integer.parseInt(deleteIdField.getText());
            Client clientToDelete = clientBLL.findClientById(clientId);

            if (clientToDelete != null) {
                try {
                    clientBLL.deleteClient(clientId);
                } catch (Exception e) {
                    showAlert("Cannot delete client " + clientId + ", \nbecause they have active orders!\nDelete their orders first!");
                }
                clearFields();
                loadClientTable();
            } else {
                showAlert("Client not found!");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid ID format! Please enter a valid number!");
        }
    }

    /**
     * Handles the selection of a client in the table.
     * Populates the edit fields with the selected client's data.
     *
     * @param event the mouse event triggering this handler
     */
    @FXML
    public void handleTableClick(MouseEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            editIdField.setText(String.valueOf(selectedClient.getId()));
            editFirstNameField.setText(selectedClient.getFirstName());
            editLastNameField.setText(selectedClient.getLastName());
            editEmailField.setText(selectedClient.getEmail());
            editAddressField.setText(selectedClient.getAddress());
        }
    }

    /**
     * Refreshes the clients table with the latest data from the database.
     */
    @FXML
    public void handleRefresh() {
        loadClientTable();
    }

    /**
     * Displays an alert dialog with the specified error message.
     *
     * @param message the message to show
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clears all input fields in the forms.
     */
    private void clearFields() {
        newFirstNameField.clear();
        newLastNameField.clear();
        newEmailField.clear();
        newAddressField.clear();

        editIdField.clear();
        editFirstNameField.clear();
        editLastNameField.clear();
        editEmailField.clear();
        editAddressField.clear();

        deleteIdField.clear();
    }
}
