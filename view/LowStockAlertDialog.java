package view;

import controller.LowStockAlertController;
import model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class LowStockAlertDialog extends VBox {

    private TableView<Product> lowStockTable;
    private LowStockAlertController controller;
    private ObservableList<Product> lowStockList;
    private ObservableList<Product> acknowledgedList; // To track acknowledged products

    public LowStockAlertDialog() throws SQLException {
        this.getStyleClass().add("panel");

        // Initialize the controller
        controller = new LowStockAlertController(this);

        // Initialize data
        lowStockList = FXCollections.observableArrayList(controller.fetchLowStockProducts());
        acknowledgedList = FXCollections.observableArrayList(); // List for acknowledged products

        // Header label
        Label headerLabel = new Label("Low Stock Alert");
        headerLabel.getStyleClass().add("header-label");

        // Table to display low-stock products
        lowStockTable = new TableView<>();
        lowStockTable.setItems(lowStockList);
        lowStockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);

        TableColumn<Product, Integer> stockLevelColumn = new TableColumn<>("Stock Level");
        stockLevelColumn.setCellValueFactory(cellData -> cellData.getValue().quantityInStockProperty().asObject());
        stockLevelColumn.setPrefWidth(100);

        TableColumn<Product, Integer> reorderLevelColumn = new TableColumn<>("Reorder Level");
        reorderLevelColumn.setCellValueFactory(cellData -> cellData.getValue().reorderLevelProperty().asObject());
        reorderLevelColumn.setPrefWidth(100);

        TableColumn<Product, String> acknowledgeColumn = new TableColumn<>("Acknowledge");
        acknowledgeColumn.setCellFactory(column -> createAcknowledgeCell());

        // Add columns to the table
        lowStockTable.getColumns().addAll(nameColumn, stockLevelColumn, reorderLevelColumn, acknowledgeColumn);

        // Placeholder message when there are no low-stock items
        lowStockTable.setPlaceholder(new Label("No low-stock products available"));

        // Button to refresh the low stock list
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshLowStockList());

        // Layout settings
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, lowStockTable, refreshButton);

        // Load initial low-stock data
        loadLowStockData();

        // Show initial notifications
        showRealTimeNotifications();
    }

    private void loadLowStockData() {
        try {
            List<Product> lowStockProducts = controller.fetchLowStockProducts();
            lowStockList.setAll(lowStockProducts);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load low stock products: " + e.getMessage());
        }
    }

    private void refreshLowStockList() {
        loadLowStockData();
    }

    // Method to show real-time notification pop-ups
    private void showRealTimeNotifications() {
        for (Product product : lowStockList) {
            if (!acknowledgedList.contains(product)) {
                showNotification(product);
            }
        }
    }

    // Show notification pop-up for a low-stock product
    private void showNotification(Product product) {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setTitle("Low Stock Alert");
        notification.setHeaderText(null);
        notification.setContentText("Low stock alert for: " + product.getName() +
                " - Stock Level: " + product.getQuantityInStock());
        notification.showAndWait();
    }

    // Creates a cell with a checkbox for acknowledgment
    private TableCell<Product, String> createAcknowledgeCell() {
        return new TableCell<>() {
            private final CheckBox acknowledgeCheckBox = new CheckBox();

            {
                acknowledgeCheckBox.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    if (acknowledgeCheckBox.isSelected()) {
                        acknowledgedList.add(product);
                    } else {
                        acknowledgedList.remove(product);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(acknowledgeCheckBox);
                    Product product = getTableView().getItems().get(getIndex());
                    acknowledgeCheckBox.setSelected(acknowledgedList.contains(product));
                }
            }
        };
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Low Stock Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Overloaded showAlert method (two parameters)
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}



