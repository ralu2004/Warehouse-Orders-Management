package app.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import app.bll.ProductBLL;
import app.gui.displayUtils.TableViewBuilder;
import app.model.Product;
import java.util.List;

/**
 * JavaFX controller class responsible for handling user interactions for product operations.
 * <p>
 * Contains methods for adding, updating, deleting, and displaying products in a {@link TableView}.
 * Interacts with the {@link ProductBLL} layer to perform business logic.
 */
public class ProductController {

    /** Business Logic Layer instance for {@link ProductBLL} */
    private ProductBLL productBLL = new ProductBLL();

    /** Text field for entering a new product's name */
    @FXML
    private TextField newNameField;

    /** Text field for entering a new product's price */
    @FXML
    private TextField newPriceField;

    /** Text field for entering a new product's stock quantity */
    @FXML
    private TextField newStockField;

    /** Text field showing the selected product's ID for editing */
    @FXML
    private TextField editIdField;

    /** Text field for editing the selected product's name */
    @FXML
    private TextField editNameField;

    /** Text field for editing the selected product's price */
    @FXML
    private TextField editPriceField;

    /** Text field for editing the selected product's stock quantity */
    @FXML
    private TextField editStockField;

    /** Text field for entering the ID of the product to delete */
    @FXML
    private TextField deleteIdField;

    /** TableView displaying the list of products */
    @FXML
    private TableView<Product> productTable;

    /**
     * Default constructor for ProductController.
     */
    public ProductController() {}

    /**
     * Initializes the controller.
     * Creates and loads the products table.
     */
    @FXML
    public void initialize() {
        productBLL.createTable();
        loadProductTable();
    }

    /**
     * Loads the product table with data and refreshes it.
     */
    private void loadProductTable() {
        List<Product> products = productBLL.findAllProducts();
        TableView<Product> newTable = TableViewBuilder.buildTableView(products, Product.class);

        newTable.setOnMouseClicked(this::handleTableClick);
        productTable.getColumns().setAll(newTable.getColumns());
        productTable.setItems(newTable.getItems());
    }

    /**
     * Handles the action of adding a new product.
     * Validates the input fields and inserts the product using {@link ProductBLL}.
     */
    @FXML
    public void handleAddProduct() {
        String name = newNameField.getText();
        double price = Double.parseDouble(newPriceField.getText());
        int stock;

        try {
            stock = Integer.parseInt(newStockField.getText());
        } catch (NumberFormatException e) {
            showAlert("Stock must be a number!");
            return;
        }

        if (name.isEmpty() || newPriceField.getText().isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        Product newProduct = new Product(name, price, stock);
        productBLL.insertProduct(newProduct);
        clearFields();
        loadProductTable();
    }

    /**
     * Handles the action of updating an existing product.
     * Gathers field values, validates them, and performs the update.
     */
    @FXML
    public void handleUpdateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("No product selected!");
            return;
        }

        int id = selectedProduct.getId();
        String name = editNameField.getText();
        double price = Double.parseDouble(editPriceField.getText());
        int stock;

        try {
            stock = Integer.parseInt(editStockField.getText());
        } catch (NumberFormatException e) {
            showAlert("Stock must be a number!");
            return;
        }

        if (name.isEmpty() || editPriceField.getText().isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        Product updatedProduct = new Product(id, name, price, stock);
        productBLL.updateProduct(updatedProduct);
        clearFields();
        loadProductTable();
    }

    /**
     * Handles the deletion of a product based on the ID provided in {@code deleteIdField}.
     */
    @FXML
    public void handleDeleteProduct() {
        int productId;
        try {
            productId = Integer.parseInt(deleteIdField.getText());
            Product productToDelete = productBLL.findProductById(productId);

            if (productToDelete != null) {
                try {
                    productBLL.deleteProduct(productId);
                } catch (Exception e) {
                    showAlert("Cannot delete product " + productId + ", \nbecause it is part of an active order!\nDelete order first!");
                }
                clearFields();
                loadProductTable();
            } else {
                showAlert("Product not found!");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid ID format! Please enter a valid number!");
        }
    }

    /**
     * Handles the selection of a product in the table.
     * Populates the edit fields with the selected product's data.
     *
     * @param event the mouse event triggering this handler
     */
    @FXML
    public void handleTableClick(MouseEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            editIdField.setText(String.valueOf(selectedProduct.getId()));
            editNameField.setText(selectedProduct.getName());
            editPriceField.setText(String.valueOf(selectedProduct.getPrice()));
            editStockField.setText(String.valueOf(selectedProduct.getStock()));
        }
    }

    /**
     * Refreshes the products table with the latest data from the database.
     */
    @FXML
    public void handleRefresh() {
        loadProductTable();
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
        newNameField.clear();
        newPriceField.clear();
        newStockField.clear();

        editIdField.clear();
        editNameField.clear();
        editPriceField.clear();
        editStockField.clear();

        deleteIdField.clear();
    }
}