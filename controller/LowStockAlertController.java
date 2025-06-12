package controller;

import model.Product;
import view.LowStockAlertDialog;  // Import the LowStockAlertDialog class
import java.sql.SQLException;
import java.util.List;

public class LowStockAlertController {

    private LowStockAlertDialog lowStockAlertDialog; // Instance of LowStockAlertDialog

    // Constructor to initialize the dialog
    public LowStockAlertController(LowStockAlertDialog lowStockAlertDialog) {
        this.lowStockAlertDialog = lowStockAlertDialog;
    }

    // Fetch all products below the reorder level
    public List<Product> fetchLowStockProducts() throws SQLException {
        return Product.fetchLowStock(); // Assuming fetchLowStock() returns products with low stock
    }

    // Generate low-stock alert message
    public void alertLowStock(List<Product> lowStockProducts) {
        if (lowStockProducts.isEmpty()) {
            lowStockAlertDialog.showAlert("No low stock products.");
        } else {
            StringBuilder alertMessage = new StringBuilder();
            
            for (Product product : lowStockProducts) {
                // Append the product details to the alert message
                alertMessage.append("Low stock alert for: ")
                            .append(product.getName())
                            .append(" - Stock Level: ")
                            .append(product.getQuantityInStock())
                            .append("\n");
            }
            
            // Show the alert message in the LowStockAlertDialog
            lowStockAlertDialog.showAlert(alertMessage.toString());
        }
    }
}

    

