package controller;

import model.Category;
import java.sql.SQLException;
import java.util.List;

public class CategoryController {
    // Add category
    public void addCategory(Category category) throws SQLException {
        category.save();
    }

    // Update category
    public void updateCategory(Category category) throws SQLException {
        category.update();
    }

    // Delete category
    public void deleteCategory(Category category) throws SQLException {
        category.delete();
    }

    // Fetch all categories
    public List<Category> fetchAllCategories() throws SQLException {
        return Category.fetchAll();
    }

}
