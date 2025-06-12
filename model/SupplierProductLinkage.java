package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierProductLinkage {
    private int supplier_product_id;
    private int supplier_id;
    private int product_id;

    // Constructor
    public SupplierProductLinkage(int supplier_product_id, int supplier_id, int product_id) {
        this.supplier_product_id = supplier_product_id;
        this.supplier_id = supplier_id;
        this.product_id = product_id;
    }

    // Getters and Setters
    public int getSupplierProductId() {
        return supplier_product_id;
    }

    public void setSupplierProductId(int supplier_product_id) {
        this.supplier_product_id = supplier_product_id;
    }

    public int getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    // CRUD Operations

    // Save a new supplier-product linkage
    public void save() throws SQLException {
        String sql = "INSERT INTO Supplier_Product_Linkage (supplier_id, product_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplier_id);
            pstmt.setInt(2, product_id);
            pstmt.executeUpdate();
        }
    }

    // Delete a supplier-product linkage
    public void delete() throws SQLException {
        String sql = "DELETE FROM Supplier_Product_Linkage WHERE supplier_product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplier_product_id);
            pstmt.executeUpdate();
        }
    }

     // Fetch all supplier-product linkages
    public static List<SupplierProductLinkage> fetchAll() throws SQLException {
        List<SupplierProductLinkage> linkages = new ArrayList<>();
        String sql = "SELECT * FROM Supplier_Product_Linkage";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                SupplierProductLinkage linkage = new SupplierProductLinkage(
                    rs.getInt("supplier_product_id"),
                    rs.getInt("supplier_id"),
                    rs.getInt("product_id")
                );
                linkages.add(linkage);
            }
        }
        return linkages;
    }


}


