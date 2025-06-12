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

public class Category {
    private final IntegerProperty categoryIdProperty;
    private final StringProperty categoryNameProperty;

    private int category_id;
    private String category_name;

    // Constructor
    public Category(int category_id, String category_name) {
        // Initialize standard fields
        this.category_id = category_id;
        this.category_name = category_name;

        // Initialize JavaFX properties and sync with standard fields
        this.categoryIdProperty = new SimpleIntegerProperty(category_id);
        this.categoryNameProperty = new SimpleStringProperty(category_name);

        // Add listeners to sync changes between standard fields and properties
        addPropertyListeners();
    }

    private void addPropertyListeners() {
        categoryIdProperty.addListener((obs, oldVal, newVal) -> this.category_id = newVal.intValue());
        categoryNameProperty.addListener((obs, oldVal, newVal) -> this.category_name = newVal);
    }

    // Property getters for JavaFX binding
    public IntegerProperty categoryIdProperty() {
        return categoryIdProperty;
    }

    public StringProperty categoryNameProperty() {
        return categoryNameProperty;
    }

    // Standard Getters and Setters
    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
        this.categoryIdProperty.set(category_id); // Sync with property
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
        this.categoryNameProperty.set(category_name); // Sync with property
    }

    // CRUD Operations

    public void save() throws SQLException {
        String sql = "INSERT INTO Categories (category_name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setString(1, category_name);
            pstmt.executeUpdate();
    
            // Retrieve the generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.category_id = generatedKeys.getInt(1); // Set the generated ID in the current object
                    this.categoryIdProperty.set(this.category_id);
                } else {
                    throw new SQLException("Failed to retrieve generated category ID.");
                }
            }
        }
    }
    
    

    // Update an existing category
    public void update() throws SQLException {
        String sql = "UPDATE Categories SET category_name = ? WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category_name);
            pstmt.setInt(2, category_id);
            pstmt.executeUpdate();
        }
    }

    // Delete a category
    public void delete() throws SQLException {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, category_id);
            pstmt.executeUpdate();
        }
    }

    // Fetch all categories
    public static List<Category> fetchAll() throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM Categories";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"));

                categories.add(category);
            }
        }
        return categories;
    }
}
