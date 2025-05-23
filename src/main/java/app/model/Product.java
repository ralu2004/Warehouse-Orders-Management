package app.model;

import app.annotations.Column;
import app.annotations.Table;

import java.util.Objects;

/**
 * Represents a product entity in the system, containing information about its name, price and available stock.
 */
@Table(name = "products")
public class Product {

    /**
     * Unique identifier of the product.
     */
    @Column(name = "id", type = "INTEGER", primaryKey = true, nullable = false, updatable = false)
    private int id;

    /**
     * Name of the product.
     */
    @Column(name = "name", type = "VARCHAR(300)", nullable = false)
    private String name;

    /**
     * Price of the product.
     */
    @Column(name = "price", type = "DOUBLE PRECISION", nullable = false)
    private double price;

    /**
     * Stock of the product.
     */
    @Column(name = "stock", type = "INTEGER", nullable = false)
    private int stock;

    /**
     * Default constructor for creating a new {@code Product}.
     */
    public Product() {
    }

    /**
     * Constructor for creating a new {@code Product} without an ID.
     *
     * @param name the name of the product
     * @param price the price of the product
     * @param stock the stock of the product
     */
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Constructor for creating a new {@code Product} with an ID.
     *
     * @param id the unique identifier of the product
     * @param name the name of the product
     * @param price the price of the product
     * @param stock the stock of the product
     */
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Gets the product's ID.
     *
     * @return the unique identifier of the product
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the product's ID.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product's name.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the product's price.
     *
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the product's stock.
     *
     * @return the stock of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a formatted string containing product details
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    /**
     * Checks equality between this product and another object.
     *
     * @param o the object to compare with
     * @return true if both products have the same attributes, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(price, product.price) == 0 && stock == product.stock && Objects.equals(name, product.name);
    }

    /**
     * Generates the hash code for the product.
     *
     * @return the hash code based on product attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock);
    }
}
