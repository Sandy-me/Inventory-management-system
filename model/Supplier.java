package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private final IntegerProperty supplierIdProperty;
    private final StringProperty nameProperty;
    private final StringProperty contactInfoProperty;
    private final StringProperty addressProperty;

    private int supplier_id;
    private String name;
    private String contact_info;
    private String address;

    // Constructor
    public Supplier(int supplier_id, String name, String contact_info, String address) {
        // Initialize standard fields
        this.supplier_id = supplier_id;
        this.name = name;
        this.contact_info = contact_info;
        this.address = address;

        // Initialize JavaFX properties and sync with standard fields
        this.supplierIdProperty = new SimpleIntegerProperty(supplier_id);
        this.nameProperty = new SimpleStringProperty(name);
        this.contactInfoProperty = new SimpleStringProperty(contact_info);
        this.addressProperty = new SimpleStringProperty(address);

        // Add listeners to sync changes between standard fields and properties
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        supplierIdProperty.addListener((obs, oldVal, newVal) -> this.supplier_id = newVal.intValue());
        nameProperty.addListener((obs, oldVal, newVal) -> this.name = newVal);
        contactInfoProperty.addListener((obs, oldVal, newVal) -> this.contact_info = newVal);
        addressProperty.addListener((obs, oldVal, newVal) -> this.address = newVal);
    }

    // Property getters for JavaFX binding
    public IntegerProperty supplierIdProperty() {
        return supplierIdProperty;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public StringProperty contactInfoProperty() {
        return contactInfoProperty;
    }

    public StringProperty addressProperty() {
        return addressProperty;
    }

    // Standard Getters and Setters
    public int getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(int supplier_id) {
        this.supplier_id = supplier_id;
        this.supplierIdProperty.set(supplier_id); // Sync with property
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty.set(name); // Sync with property
    }

    public String getContactInfo() {
        return contact_info;
    }

    public void setContactInfo(String contact_info) {
        this.contact_info = contact_info;
        this.contactInfoProperty.set(contact_info); // Sync with property
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.addressProperty.set(address); // Sync with property
    }

    // CRUD Operations

    // Save a new supplier
    public void save() throws SQLException {
        String sql = "INSERT INTO Suppliers (name, contact_info, address) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contact_info);
            pstmt.setString(3, address);
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.supplier_id = generatedKeys.getInt(1); // Set the generated ID in the current object
                    this.supplierIdProperty.set(this.supplier_id);
                } else {
                    throw new SQLException("Failed to retrieve generated supplier ID.");
                }
            }
        }
    }

    // Update an existing supplier
    public void update() throws SQLException {
        String sql = "UPDATE Suppliers SET name = ?, contact_info = ?, address = ? WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contact_info);
            pstmt.setString(3, address);
            pstmt.setInt(4, supplier_id);
            pstmt.executeUpdate();
        }
    }

    // Delete a supplier
    public void delete() throws SQLException {
        String sql = "DELETE FROM Suppliers WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplier_id);
            pstmt.executeUpdate();
        }
    }

    // Fetch all suppliers
    public static List<Supplier> fetchAll() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_info"),
                        rs.getString("address"));
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }
}



