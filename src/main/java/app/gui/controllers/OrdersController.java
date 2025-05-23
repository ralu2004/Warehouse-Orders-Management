package app.gui.controllers;

import app.bll.BillBLL;
import app.bll.ClientBLL;
import app.bll.OrderBLL;
import app.bll.ProductBLL;
import app.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import app.bll.*;
import app.gui.displayUtils.TableViewBuilder;
import app.model.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * JavaFX controller class responsible for handling user interactions for orders operations.
 * <p>
 * Contains methods for adding, and displaying orders in a {@link TableView}.
 * Interacts with the whole business logic layer.
 */
public class OrdersController {

    /** Business Logic Layer instance for {@link ClientBLL} */
    private ClientBLL clientBLL = new ClientBLL();

    /** Business Logic Layer instance for {@link ProductBLL} */
    private ProductBLL productBLL = new ProductBLL();

    /** Business Logic Layer instance for {@link OrderBLL} */
    private OrderBLL orderBLL = new OrderBLL();

    /** Business Logic Layer instance for {@link BillBLL} */
    private BillBLL billBLL = new BillBLL();

    /** ComboBox for selecting a client when placing an order */
    @FXML
    private ComboBox<Client> clientComboBox;

    /** ComboBox for selecting a product when placing an order */
    @FXML
    private ComboBox<Product> productComboBox;

    /** TextField for entering the quantity of the selected product to order */
    @FXML
    private TextField quantityField;

    /** TextField for entering the ID of an order to delete */
    @FXML
    private TextField deleteOrderField;

    /** TableView displaying detailed information about existing orders */
    @FXML
    private TableView<OrderDetails> detailedOrdersTable;

    /**
     * Default constructor for OrdersController.
     */
    public OrdersController() {}

    /**
     * Initializes the controller.
     * Creates and loads the clients list, products list and the detailed orders table.
     */
    @FXML
    public void initialize() {

        orderBLL.createTable();
        billBLL.createTable();
        loadClients();
        loadProducts();
        loadDetailedOrders();
    }

    /**
     * Loads the clients from the database.
     */
    private void loadClients() {
        List<Client> clients = clientBLL.findAllClients();
        clientComboBox.setItems(FXCollections.observableArrayList(clients));
    }

    /**
     * Loads the products from the database.
     */
    private void loadProducts() {
        List<Product> products = productBLL.findAllProducts();
        productComboBox.setItems(FXCollections.observableArrayList(products));
    }

    /**
     * Loads the detailed orders table with data and refreshes it.
     */
    private void loadDetailedOrders() {
        List<OrderDetails> detailedOrders = orderBLL.getDetailedOrders();
        TableView<OrderDetails> newTable = TableViewBuilder.buildTableView(detailedOrders, OrderDetails.class);

        newTable.setOnMouseClicked(this::handleTableClick);
        detailedOrdersTable.getColumns().setAll(newTable.getColumns());
        detailedOrdersTable.setItems(newTable.getItems());
    }

    /**
     * Handles the generation of a new order and its corresponding bill.
     */
    @FXML
    public void handleGenerateOrder() {
        handleNewOrder();
        handleNewBill();
    }

    /**
     * Handles the generation of a new order.
     */
    private void handleNewOrder() {
        Client selectedClient = clientComboBox.getSelectionModel().getSelectedItem();
        Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();
        String quantityText = quantityField.getText();

        if (selectedClient == null || selectedProduct == null || quantityText.isEmpty()) {
            showAlert("Please select a client, a product, and enter a quantity.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showAlert("Quantity must be greater than zero.");
                return;
            }
            if (quantity > selectedProduct.getStock()) {
                showAlert("Not enough stock. Available stock: " + selectedProduct.getStock());
                return;
            } else {
                productBLL.decreaseStock(selectedProduct.getId(), quantity);
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric quantity.");
            return;
        }

        double totalPrice = selectedProduct.getPrice() * quantity;
        Timestamp orderDate = Timestamp.valueOf(java.time.LocalDateTime.now());

        Order newOrder = new Order(selectedClient.getId(), selectedProduct.getId(), quantity, totalPrice, orderDate);
        try {
            orderBLL.insertOrder(newOrder);
            loadDetailedOrders();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to generate order.");
        }
    }

    /**
     * Handles the generation of a new bill corresponding to the last placed order.
     */
    private void handleNewBill() {
        int id = orderBLL.getLastId();
        Order order = orderBLL.findOrderById(id);
        if (order == null || order.getId() == 0) {
            showAlert("Invalid order! Cannot generate bill.");
            return;
        }

        Bill newBill = new Bill(null, order.getId(), order.getTotalPrice(), order.getOrderDate());

        try {
            billBLL.generateBill(newBill);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to generate bill.");
        }
    }

    /**
     * Handles the deletion of an order based on the ID provided in {@code deleteOrderField}.
     */
    @FXML
    public void handleDeleteOrder() {
        int orderId;
        try {
            orderId = Integer.parseInt(deleteOrderField.getText());
            Order orderToDelete = orderBLL.findOrderById(orderId);

            if (orderToDelete != null) {
                orderBLL.deleteOrder(orderId);
                clearFields();
                loadDetailedOrders();
            } else {
                showAlert("Order not found!");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid ID format! Please enter a valid number!");
        }
    }

    /**
     * Handles the selection of a detailed order in the table.
     * Displays a pop-up with the corresponding {@link Bill} details.
     *
     * @param event the mouse event triggering this handler
     */
    @FXML
    public void handleTableClick(MouseEvent event) {
        OrderDetails orderDetails = detailedOrdersTable.getSelectionModel().getSelectedItem();

        if (orderDetails != null) {
            Order order = orderBLL.findOrderById(orderDetails.getOrderId());

            if (order != null) {
                Bill bill = billBLL.getBillByOrderId(order.getId());

                if (bill != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTimestamp = bill.timestamp().toLocalDateTime().format(formatter);

                    String billContent = String.format("Bill ID: %d\n", bill.id()) +
                            String.format("Order ID: %d\n", order.getId()) +
                            String.format("Date: %s\n", formattedTimestamp) +
                            String.format("Total Amount: $%.2f\n", bill.amount());

                    Alert displayBill = new Alert(Alert.AlertType.INFORMATION);
                    displayBill.setTitle("BILL DETAILS");
                    displayBill.setHeaderText("Bill Details for Order #" + order.getId());
                    displayBill.setContentText(billContent);
                    displayBill.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No Bill Found");
                    alert.setHeaderText("No bill found for this order.");
                    alert.setContentText("The order does not have an associated bill.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Refreshes the detailed orders table with the latest data from the database.
     */
    @FXML
    public void handleRefresh() {
        loadDetailedOrders();
        loadClients();
        loadProducts();
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
        quantityField.clear();
        deleteOrderField.clear();
        clientComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
    }
}