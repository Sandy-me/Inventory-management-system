package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty productIdProperty;
    private final StringProperty nameProperty;
    private final IntegerProperty categoryIdProperty;
    private final StringProperty skuProperty;
    private final IntegerProperty quantityInStockProperty;
    private final IntegerProperty reorderLevelProperty;
    private final IntegerProperty supplierIdProperty;
    private final IntegerProperty batchIdProperty;

    private int product_id;
    private String name;
    private int category_id;
    private String sku;
    private int quantity_in_stock;
    private int reorder_level;
    private int supplier_id;
    private int batch_id;

    // Constructor
    public Product(int product_id, String name, int category_id, String sku, int quantity_in_stock, int reorder_level, int supplier_id, int batch_id) {
        // Initialize standard fields
        this.product_id = product_id;
        this.name = name;
        this.category_id = category_id;
        this.sku = sku;
        this.quantity_in_stock = quantity_in_stock;
        this.reorder_level = reorder_level;
        this.supplier_id = supplier_id;
        this.batch_id = batch_id;

        // Initialize JavaFX properties and sync with standard fields
        this.productIdProperty = new SimpleIntegerProperty(product_id);
        this.nameProperty = new SimpleStringProperty(name);
        this.categoryIdProperty = new SimpleIntegerProperty(category_id);
        this.skuProperty = new SimpleStringProperty(sku);
        this.quantityInStockProperty = new SimpleIntegerProperty(quantity_in_stock);
        this.reorderLevelProperty = new SimpleIntegerProperty(reorder_level);
        this.supplierIdProperty = new SimpleIntegerProperty(supplier_id);
        this.batchIdProperty = new SimpleIntegerProperty(batch_id);

        // Add listeners to sync changes between standard fields and properties
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        nameProperty.addListener((obs, oldVal, newVal) -> this.name = newVal);
        skuProperty.addListener((obs, oldVal, newVal) -> this.sku = newVal);
        categoryIdProperty.addListener((obs, oldVal, newVal) -> this.category_id = newVal.intValue());
        quantityInStockProperty.addListener((obs, oldVal, newVal) -> this.quantity_in_stock = newVal.intValue());
        reorderLevelProperty.addListener((obs, oldVal, newVal) -> this.reorder_level = newVal.intValue());
        supplierIdProperty.addListener((obs, oldVal, newVal) -> this.supplier_id = newVal.intValue());
        batchIdProperty.addListener((obs, oldVal, newVal) -> this.batch_id = newVal.intValue());

    }

    // Property getters for JavaFX binding
    public IntegerProperty productIdProperty() {
        return productIdProperty;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public IntegerProperty categoryIdProperty() {
        return categoryIdProperty;
    }

    public StringProperty skuProperty() {
        return skuProperty;
    }

    public IntegerProperty quantityInStockProperty() {
        return quantityInStockProperty;
    }

    public IntegerProperty reorderLevelProperty() {
        return reorderLevelProperty;
    }

    public IntegerProperty supplierIdProperty() {
        return supplierIdProperty;
    }

    public IntegerProperty batchIdProperty() {
        return batchIdProperty;
    }

    // Traditional getters and setters
    public int getProductId() {
        return product_id;
    }


    public void setProductId(int product_id) {
        this.product_id = product_id;
        this.productIdProperty.set(product_id); // Sync with JavaFX property
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty.set(name); // Sync with JavaFX property
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
        this.categoryIdProperty.set(category_id); // Sync with JavaFX property
    }

    public int getBatchId() {
        return batch_id;
    }
    public void setBatchId(int batch_id) {
        this.batch_id = batch_id;
        this.batchIdProperty.set(batch_id); // Sync with JavaFX property
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
        this.skuProperty.set(sku); // Sync with JavaFX property
    }

    public int getQuantityInStock() {
        return quantity_in_stock;
    }

    public void setQuantityInStock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
        this.quantityInStockProperty.set(quantity_in_stock); // Sync with JavaFX property
    }

    public int getReorderLevel() {
        return reorder_level;
    }

    public void setReorderLevel(int reorder_level) {
        this.reorder_level = reorder_level;
        this.reorderLevelProperty.set(reorder_level); // Sync with JavaFX property
    }

    public int getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(int supplier_id) {
        this.supplier_id = supplier_id;
        this.supplierIdProperty.set(supplier_id); // Sync with JavaFX property
    }


public void save() throws SQLException{
    String sql = "INSERT INTO Products (name, category_id, sku, quantity_in_stock, reorder_level, supplier_id, batch_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
     
    try(Connection conn = DatabaseConnection.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1, name);
        pstmt.setInt(2, category_id);
        pstmt.setString(3, sku);
        pstmt.setInt(4, quantity_in_stock);
        pstmt.setInt(5, reorder_level);
        pstmt.setInt(6, supplier_id);
        pstmt.setInt(7, batch_id);


        pstmt.executeUpdate();
    }
}

public void update() throws SQLException{
    String sql = "UPDATE Products SET name = ?, category_id = ?, sku = ?, quantity_in_stock = ?, reorder_level = ?, supplier_id = ?,batch_id = ? WHERE product_id = ?";

    try(Connection conn = DatabaseConnection.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setString(1, name);
        pstmt.setInt(2, category_id);
        pstmt.setString(3, sku);
        pstmt.setInt(4, quantity_in_stock);
        pstmt.setInt(5, reorder_level);
        pstmt.setInt(6, supplier_id);
        pstmt.setInt(8, batch_id);
        pstmt.setInt(7, product_id);

        pstmt.executeUpdate();
    }
}

public void delete() throws SQLException{
    String sql = "DELETE FROM Products WHERE product_id = ?";

    try(Connection conn = DatabaseConnection.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setInt(1, product_id);

        pstmt.executeUpdate();
    }
}

public static List<Product> fetchAll() throws SQLException {
    List<Product> products = new ArrayList<>();

    String sql = "SELECT * FROM Products";

    try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getInt("category_id"),
                    rs.getString("sku"),
                    rs.getInt("quantity_in_stock"),
                    rs.getInt("reorder_level"),
                    rs.getInt("supplier_id"),
                    rs.getInt("batch_id"));

            products.add(product);
        }
    }
    return products;

}


public static List<Product> fetchLowStock() throws SQLException {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT * FROM Products WHERE quantity_in_stock < reorder_level";  // Query products below reorder level

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Product product = new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getInt("category_id"),
                rs.getString("sku"),
                rs.getInt("quantity_in_stock"),
                rs.getInt("reorder_level"),
                rs.getInt("supplier_id"),
                rs.getInt("batch_id"));

            products.add(product);
        }
    }
    return products;
}

}

