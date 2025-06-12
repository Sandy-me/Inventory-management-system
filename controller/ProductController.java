package controller;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductController {

    // Adds a new product
    public void addProduct(Product product) throws SQLException {
        product.save();
    }

    // Updates an existing product
    public void updateProduct(Product product) throws SQLException {
        product.update();
    }

    // Deletes a product by its ID
    public void deleteProduct(int product_id) throws SQLException {
        Product product = new Product(product_id, null, 0, null, 0, 0, 0,0); // Temporary Product object with ID only
        product.delete();
    }

    // Retrieves a list of all products
    public List<Product> fetchAllProducts() throws SQLException {
        return Product.fetchAll();
    }

    
}
