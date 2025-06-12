import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.ProductPanel;
import view.CategoryPanel;
import view.SupplierPanel;
import view.PurchaseHistoryPanel;
import view.BatchPanel;
import view.LowStockAlertDialog;

import java.sql.SQLException;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create a TabPane for navigation
        TabPane tabPane = new TabPane();

        // Set up each tab with their respective panels
        Tab productTab = new Tab("Product Panel");
        productTab.setClosable(false);
        Tab categoryTab = new Tab("Category Panel");
        categoryTab.setClosable(false);
        Tab supplierTab = new Tab("Supplier Panel");
        supplierTab.setClosable(false);
        Tab purchaseHistoryTab = new Tab("Purchase History Panel");
        purchaseHistoryTab.setClosable(false);
        Tab batchTab = new Tab("Batch Panel");
        batchTab.setClosable(false);
        Tab lowStockAlertTab = new Tab("Low Stock Alert");
        lowStockAlertTab.setClosable(false);

        // Load the panels for each tab
        loadProductPanel(productTab);
        loadCategoryPanel(categoryTab);
        loadSupplierPanel(supplierTab);
        loadPurchaseHistoryPanel(purchaseHistoryTab);
        loadBatchPanel(batchTab);
        loadLowStockAlert(lowStockAlertTab);

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(productTab, categoryTab, supplierTab, purchaseHistoryTab, batchTab, lowStockAlertTab);

        // Set the TabPane as the center component of the BorderPane
        root.setCenter(tabPane);

        // Create and set the scene
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("resources/css/styles.css").toExternalForm());

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadProductPanel(Tab productTab) {
        try {
            ProductPanel productPanel = new ProductPanel();
            productTab.setContent(productPanel);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Product Panel: " + ex.getMessage());
        }
    }

    private void loadCategoryPanel(Tab categoryTab) {
        try {
            CategoryPanel categoryPanel = new CategoryPanel();
            categoryTab.setContent(categoryPanel);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Category Panel: " + ex.getMessage());
        }
    }

    private void loadSupplierPanel(Tab supplierTab) {
        try {
            SupplierPanel supplierPanel = new SupplierPanel();
            supplierTab.setContent(supplierPanel);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Supplier Panel: " + ex.getMessage());
        }
    }

    private void loadPurchaseHistoryPanel(Tab purchaseHistoryTab) {
        try {
            PurchaseHistoryPanel purchaseHistoryPanel = new PurchaseHistoryPanel();
            purchaseHistoryTab.setContent(purchaseHistoryPanel);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Purchase History Panel: " + ex.getMessage());
        }
    }

    private void loadBatchPanel(Tab batchTab) {
        try {
            BatchPanel batchPanel = new BatchPanel();
            batchTab.setContent(batchPanel);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Batch Panel: " + ex.getMessage());
        }
    }

    private void loadLowStockAlert(Tab lowStockAlertTab) {
        try {
            LowStockAlertDialog lowStockAlertDialog = new LowStockAlertDialog();
            lowStockAlertTab.setContent(lowStockAlertDialog);
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to load Low Stock Alert: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

