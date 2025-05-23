package app.model;

import app.annotations.Column;
import app.annotations.Table;

import java.sql.Timestamp;

/**
 * Represent a detailed order in the system, resulted from joining Order, Client and Product.
 */
@Table(name = "OrderDetails")
public class OrderDetails {

    /**
     * Unique identifier of the order.
     */
    @Column(name = "order_id", type = "INT")
    private int orderId;

    /**
     * Name of t client who placed the order.
     */
    @Column(name = "client_name", type = "VARCHAR(200)")
    private String clientFullName;

    /**
     * Name of the ordered product.
     */
    @Column(name = "product_name", type = "VARCHAR(300)")
    private String productName;

    /**
     * Quantity of the ordered product.
     */
    @Column(name = "quantity", type = "INT")
    private int quantity;

    /**
     * Total price of the ordered product.
     */
    @Column(name = "total_price", type = "DOUBLE PRECISION")
    private double totalPrice;

    /**
     * Date and time when the order was placed.
     */
    @Column(name = "order_date", type = "TIMESTAMP")
    private Timestamp orderDate;

    /**
     * Constructor for creating a detailed order.
     *
     * @param orderId the unique identifier of the order
     * @param clientFullName the name of the client placing the order
     * @param productName the name of the product being ordered
     * @param quantity the quantity of the product ordered
     * @param totalPrice the total price of the order
     * @param orderDate the date and time when the order was placed
     */
    public OrderDetails(int orderId, String clientFullName, String productName, int quantity, double totalPrice, Timestamp orderDate) {
        this.orderId = orderId;
        this.clientFullName = clientFullName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    /**
     * Gets the order ID.
     *
     * @return the unique identifier of the order
     */
    public int getOrderId() { return orderId; }
}