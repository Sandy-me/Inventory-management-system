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

public class Batch {
    private final IntegerProperty batchIdProperty;
    private final IntegerProperty productIdProperty;
    private final StringProperty expiryDateProperty;
    private final IntegerProperty quantityInBatchProperty;

    private int batch_id;
    private int product_id;
    private String expiry_date;
    private int quantity_in_batch;

    // Constructor
    public Batch(int batch_id, int product_id, String expiry_date, int quantity_in_batch) {
        this.batch_id = batch_id;
        this.product_id = product_id;
        this.expiry_date = expiry_date;
        this.quantity_in_batch = quantity_in_batch;

        // Initialize JavaFX properties and sync with standard fields
        this.batchIdProperty = new SimpleIntegerProperty(batch_id);
        this.productIdProperty = new SimpleIntegerProperty(product_id);
        this.expiryDateProperty = new SimpleStringProperty(expiry_date);
        this.quantityInBatchProperty = new SimpleIntegerProperty(quantity_in_batch);

        // Add listeners to sync changes between standard fields and properties
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        batchIdProperty.addListener((obs, oldVal, newVal) -> this.batch_id = newVal.intValue());
        productIdProperty.addListener((obs, oldVal, newVal) -> this.product_id = newVal.intValue());
        expiryDateProperty.addListener((obs, oldVal, newVal) -> this.expiry_date = newVal);
        quantityInBatchProperty.addListener((obs, oldVal, newVal) -> this.quantity_in_batch = newVal.intValue());
    }

    // Property getters for JavaFX binding
    public IntegerProperty batchIdProperty() {
        return batchIdProperty;
    }

    public IntegerProperty productIdProperty() {
        return productIdProperty;
    }

    public StringProperty expiryDateProperty() {
        return expiryDateProperty;
    }

    public IntegerProperty quantityInBatchProperty() {
        return quantityInBatchProperty;
    }

    // Standard Getters and Setters
    public int getBatchId() {
        return batch_id;
    }

    public void setBatchId(int batch_id) {
        this.batch_id = batch_id;
        this.batchIdProperty.set(batch_id); // Sync with property
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
        this.productIdProperty.set(product_id); // Sync with property
    }

    public String getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
        this.expiryDateProperty.set(expiry_date); // Sync with property
    }

    public int getQuantityInBatch() {
        return quantity_in_batch;
    }

    public void setQuantityInBatch(int quantity_in_batch) {
        this.quantity_in_batch = quantity_in_batch;
        this.quantityInBatchProperty.set(quantity_in_batch); // Sync with property
    }

    // CRUD Operations

    // Save a new batch
    public void save() throws SQLException {
        String sql = "INSERT INTO Batch (product_id, expiry_date, quantity_in_batch) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, product_id);
            pstmt.setString(2, expiry_date);
            pstmt.setInt(3, quantity_in_batch);
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.batch_id = generatedKeys.getInt(1); // Set the generated ID in the current object
                    this.batchIdProperty.set(this.batch_id);
                } else {
                    throw new SQLException("Failed to retrieve generated batch ID.");
                }
            }
        }
    }

    // Update method
    public void update() throws SQLException {
        String sql = "UPDATE Batches SET product_id = ?, expiry_date = ?, quantity_in_batch = ? WHERE batch_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product_id);
            pstmt.setString(2, expiry_date);
            pstmt.setInt(3, quantity_in_batch);
            pstmt.setInt(4, batch_id);
            pstmt.executeUpdate();
        }
    }

    // Delete method
    public void delete() throws SQLException {
        String sql = "DELETE FROM Batches WHERE batch_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, batch_id);
            pstmt.executeUpdate();
        }
    }

    // Fetch all batches
    public static List<Batch> fetchAll() throws SQLException {
        List<Batch> batches = new ArrayList<>();
        String sql = "SELECT * FROM Batches";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Batch batch = new Batch(
                        rs.getInt("batch_id"),
                        rs.getInt("product_id"),
                        rs.getString("expiry_date"),
                        rs.getInt("quantity_in_batch"));
                batches.add(batch);
            }
        }
        return batches;
    }

    // Fetch batches by product_id
    public static List<Batch> fetchByProductId(int product_id) throws SQLException {
        List<Batch> batches = new ArrayList<>();
        String sql = "SELECT * FROM Batches WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Batch batch = new Batch(
                            rs.getInt("batch_id"),
                            rs.getInt("product_id"),
                            rs.getString("expiry_date"),
                            rs.getInt("quantity_in_batch"));
                    batches.add(batch);
                }
            }
        }
        return batches;
    }
}



