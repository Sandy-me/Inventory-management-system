package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory {
    private final IntegerProperty purchaseIdProperty;
    private final IntegerProperty productIdProperty;
    private final IntegerProperty supplierIdProperty;
    private final StringProperty purchaseDateProperty;
    private final IntegerProperty quantityProperty;
    private final DoubleProperty costProperty;

    private int purchase_id;
    private int product_id;
    private int supplier_id;
    private String purchase_date;
    private int quantity;
    private double cost;

    // Constructor
    public PurchaseHistory(int purchase_id, int product_id, int supplier_id, String purchase_date, int quantity, double cost) {
        this.purchase_id = purchase_id;
        this.product_id = product_id;
        this.supplier_id = supplier_id;
        this.purchase_date = purchase_date;
        this.quantity = quantity;
        this.cost = cost;

        // Initialize JavaFX properties and sync with standard fields
        this.purchaseIdProperty = new SimpleIntegerProperty(purchase_id);
        this.productIdProperty = new SimpleIntegerProperty(product_id);
        this.supplierIdProperty = new SimpleIntegerProperty(supplier_id);
        this.purchaseDateProperty = new SimpleStringProperty(purchase_date);
        this.quantityProperty = new SimpleIntegerProperty(quantity);
        this.costProperty = new SimpleDoubleProperty(cost);

        // Add listeners to sync changes between standard fields and properties
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        purchaseIdProperty.addListener((obs, oldVal, newVal) -> this.purchase_id = newVal.intValue());
        productIdProperty.addListener((obs, oldVal, newVal) -> this.product_id = newVal.intValue());
        supplierIdProperty.addListener((obs, oldVal, newVal) -> this.supplier_id = newVal.intValue());
        purchaseDateProperty.addListener((obs, oldVal, newVal) -> this.purchase_date = newVal);
        quantityProperty.addListener((obs, oldVal, newVal) -> this.quantity = newVal.intValue());
        costProperty.addListener((obs, oldVal, newVal) -> this.cost = newVal.doubleValue());
    }

    // Property getters for JavaFX binding
    public IntegerProperty purchaseIdProperty() {
        return purchaseIdProperty;
    }

    public IntegerProperty productIdProperty() {
        return productIdProperty;
    }

    public IntegerProperty supplierIdProperty() {
        return supplierIdProperty;
    }

    public StringProperty purchaseDateProperty() {
        return purchaseDateProperty;
    }

    public IntegerProperty quantityProperty() {
        return quantityProperty;
    }

    public DoubleProperty costProperty() {
        return costProperty;
    }

    // Standard Getters and Setters
    public int getPurchaseId() {
        return purchase_id;
    }

    public void setPurchaseId(int purchase_id) {
        this.purchase_id = purchase_id;
        this.purchaseIdProperty.set(purchase_id); // Sync with property
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
        this.productIdProperty.set(product_id); // Sync with property
    }

    public int getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(int supplier_id) {
        this.supplier_id = supplier_id;
        this.supplierIdProperty.set(supplier_id); // Sync with property
    }

    public String getPurchaseDate() {
        return purchase_date;
    }

    public void setPurchaseDate(String purchase_date) {
        this.purchase_date = purchase_date;
        this.purchaseDateProperty.set(purchase_date); // Sync with property
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.quantityProperty.set(quantity); // Sync with property
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
        this.costProperty.set(cost); // Sync with property
    }

    // CRUD Operations

    // Save a new purchase history entry
    public void save() throws SQLException {
        String sql = "INSERT INTO Purchase_History (product_id, supplier_id, purchase_date, quantity, cost) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, product_id);
            pstmt.setInt(2, supplier_id);
            pstmt.setString(3, purchase_date);
            pstmt.setInt(4, quantity);
            pstmt.setDouble(5, cost);
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.purchase_id = generatedKeys.getInt(1); // Set the generated ID in the current object
                    this.purchaseIdProperty.set(this.purchase_id);
                } else {
                    throw new SQLException("Failed to retrieve generated purchase ID.");
                }
            }
        }
    }

    // Fetch all purchase history records
    public static List<PurchaseHistory> fetchAll() throws SQLException {
        List<PurchaseHistory> purchaseHistories = new ArrayList<>();
        String sql = "SELECT * FROM Purchase_History";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PurchaseHistory purchaseHistory = new PurchaseHistory(
                        rs.getInt("purchase_id"),
                        rs.getInt("product_id"),
                        rs.getInt("supplier_id"),
                        rs.getString("purchase_date"),
                        rs.getInt("quantity"),
                        rs.getDouble("cost"));
                purchaseHistories.add(purchaseHistory);
            }
        }
        return purchaseHistories;
    }
}



