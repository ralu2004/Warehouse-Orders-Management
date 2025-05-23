package app.model;

import app.annotations.Column;
import app.annotations.Table;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents an order in the system, storing details about the client, product, quantity, total price, and order date.
 */
@Table(name = "orders")
public class Order {

    /**
     * Unique identifier of the order.
     */
    @Column(name = "id", type = "INT", primaryKey = true, nullable = false, updatable = false)
    private int id;

    /**
     * ID of the client who placed the order.
     */
    @Column(name = "client_id", type = "INT", nullable = false, foreignKeyTable = "clients", foreignKeyColumn = "id")
    private int clientId;

    /**
     * ID of the ordered product.
     */
    @Column(name = "product_id", type = "INT", nullable = false, foreignKeyTable = "products", foreignKeyColumn = "id")
    private int productId;

    /**
     * Quantity of the product ordered.
     */
    @Column(name = "quantity", type = "INT", nullable = false)
    private int quantity;

    /**
     * Total price of the order.
     */
    @Column(name = "total_price", type = "DOUBLE PRECISION", nullable = false)
    private double totalPrice;

    /**
     * Date and time when the order was placed.
     */
    @Column(name = "order_date", type = "TIMESTAMP", nullable = false)
    private Timestamp orderDate;

    /**
     * Default constructor for creating a new {@code Order}.
     */
    public Order() {}

    /**
     * Constructor for creating a new {@code Order} without an ID.
     *
     * @param clientId the ID of the client placing the order
     * @param productId the ID of the product being ordered
     * @param quantity the quantity of the product ordered
     * @param totalPrice the total price of the order
     * @param orderDate the date and time when the order was placed
     */
    public Order(int clientId, int productId, int quantity, double totalPrice, Timestamp orderDate) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    /**
     * Constructor for creating a new {@code Order} with an ID.
     *
     * @param id the unique identifier of the order
     * @param clientId the ID of the client placing the order
     * @param productId the ID of the product being ordered
     * @param quantity the quantity of the product ordered
     * @param totalPrice the total price of the order
     * @param orderDate the date and time when the order was placed
     */
    public Order(int id, int clientId, int productId, int quantity, double totalPrice, Timestamp orderDate) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    /**
     * Gets the order ID.
     *
     * @return the unique identifier of the order
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the order ID.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the total price of the order.
     *
     * @return the total price of the order
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Gets the timestamp of when the order was placed.
     *
     * @return the order date
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /**
     * Returns a string representation of the order.
     *
     * @return a formatted string containing order details
     */
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }

    /**
     * Checks equality between this order and another object.
     *
     * @param o the object to compare with
     * @return true if both orders have the same attributes, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && clientId == order.clientId && productId == order.productId &&
                quantity == order.quantity && Double.compare(totalPrice, order.totalPrice) == 0 &&
                Objects.equals(orderDate, order.orderDate);
    }

    /**
     * Generates the hash code for the order.
     *
     * @return the hash code based on order attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, productId, quantity, totalPrice, orderDate);
    }
}