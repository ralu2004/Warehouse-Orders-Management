package app.model;

import app.annotations.Column;
import app.annotations.Table;

import java.util.Objects;

/**
 * Represents a client entity in the system, containing personal (name) and contact (email, address) information.
 */
@Table(name = "clients")
public class Client {

    /**
     * Unique identifier of the client.
     */
    @Column(name = "id", type = "INTEGER", primaryKey = true, nullable = false, updatable = false)
    private int id;

    /**
     * First name of the client.
     */
    @Column(name = "first_name", type = "VARCHAR(100)", nullable = false)
    private String firstName;

    /**
     * Last name of the client.
     */
    @Column(name = "last_name", type = "VARCHAR(100)", nullable = false)
    private String lastName;

    /**
     * Email address of the client.
     */
    @Column(name = "email", type = "VARCHAR(150)", nullable = false)
    private String email;

    /**
     * Physical address of the client.
     */
    @Column(name = "address", type = "VARCHAR(300)", nullable = false)
    private String address;

    /**
     * Default constructor for a new {@code Client}.
     */
    public Client() {}

    /**
     * Constructor for creating a new {@code Client} without an ID.
     *
     * @param firstName the first name of the client
     * @param lastName the last name of the client
     * @param email the email of the client
     * @param address the physical address of the client
     */
    public Client(String firstName, String lastName, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    /**
     * Constructor for creating a new {@code Client} with an ID.
     *
     * @param id the unique identifier of the client
     * @param firstName the first name of the client
     * @param lastName the last name of the client
     * @param email the email of the client
     * @param address the physical address of the client
     */
    public Client(int id, String firstName, String lastName, String email, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    /**
     * Gets the client's ID.
     *
     * @return the unique identifier of the client
     */
    public int getId() { return id; }

    /**
     * Sets the client's ID.
     *
     * @param id the new unique identifier
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the client's first name.
     *
     * @return the first name of the client
     */
    public String getFirstName() { return firstName; }

    /**
     * Gets the client's last name.
     *
     * @return the last name of the client
     */
    public String getLastName() { return lastName; }

    /**
     * Gets the client's email.
     *
     * @return the email of the client
     */
    public String getEmail() { return email; }

    /**
     * Gets the client's address.
     *
     * @return the physical address of the client
     */
    public String getAddress() { return address; }

    /**
     * Returns a string representation of the client.
     *
     * @return a formatted string containing client details
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * Checks equality between this client and another object.
     *
     * @param o the object to compare with
     * @return true if both clients have the same attributes, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return id == client.id &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(email, client.email) &&
                Objects.equals(address, client.address);
    }

    /**
     * Generates the hash code for the client.
     *
     * @return the hash code based on client attributes
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, address);
    }
}