package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.ProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import model.Product;

public class ProductPanel extends VBox {

    private TableView<Product> productTable;
    private TextField nameField, skuField, quantityField, reorderLevelField, categoryIdField, supplierIdField, batchIdField;
    private Button addButton, updateButton, deleteButton, clearButton;
    private ProductController productController;
    private ObservableList<Product> productList;

    public ProductPanel() throws SQLException{
        this.getStyleClass().add("panel");  // Assign CSS class to the panel

        // Initialize controller and data
        productController = new ProductController();
        productList = FXCollections.observableArrayList(productController.fetchAllProducts());

        // Header label
        Label headerLabel = new Label("Product Management");
        headerLabel.getStyleClass().add("header-label");

        // Set up form layout with labels and fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("form-label");
        nameField = new TextField();
        nameField.setPromptText("Enter Product's Name");
        nameField.getStyleClass().add("text-field");

        Label skuLabel = new Label("SKU:");
        skuLabel.getStyleClass().add("form-label");
        skuField = new TextField();
        skuField.setPromptText("Enter Stock Keeping Unit");
        skuField.getStyleClass().add("text-field");

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("form-label");
        quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");
        quantityField.getStyleClass().add("text-field");

        Label reorderLevelLabel = new Label("Reorder Level:");
        reorderLevelLabel.getStyleClass().add("form-label");
        reorderLevelField = new TextField();
        reorderLevelField.setPromptText("Enter Reorder Level");
        reorderLevelField.getStyleClass().add("text-field");

        Label categoryIdLabel = new Label("Category ID:");
        categoryIdLabel.getStyleClass().add("form-label");
        categoryIdField = new TextField();
        categoryIdField.setPromptText("Enter Category ID");
        categoryIdField.getStyleClass().add("text-field");

        Label supplierIdLabel = new Label("Supplier ID:");
        supplierIdLabel.getStyleClass().add("form-label");
        supplierIdField = new TextField();
        supplierIdField.setPromptText("Enter Supplier ID");
        supplierIdField.getStyleClass().add("text-field");

        Label batchIdLabel = new Label("Batch ID:");
        batchIdLabel.getStyleClass().add("form-label");
        batchIdField = new TextField();
        batchIdField.setPromptText("Enter Batch ID");
        batchIdField.getStyleClass().add("text-field");

        // Adding labels and fields to the form grid with spacing
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(skuLabel, 0, 1);
        form.add(skuField, 1, 1);
        form.add(quantityLabel, 0, 2);
        form.add(quantityField, 1, 2);
        form.add(reorderLevelLabel, 0, 3);
        form.add(reorderLevelField, 1, 3);
        form.add(categoryIdLabel, 0, 4);
        form.add(categoryIdField, 1, 4);
        form.add(supplierIdLabel, 0, 5);
        form.add(supplierIdField, 1, 5);
        form.add(batchIdLabel, 0, 6);
        form.add(batchIdField, 1, 6);


        //for the text fields
        nameField.getStyleClass().add("text-field");
        skuField.getStyleClass().add("text-field");
        quantityField.getStyleClass().add("text-field");
        reorderLevelField.getStyleClass().add("text-field");
        categoryIdField.getStyleClass().add("text-field");
        supplierIdField.getStyleClass().add("text-field");
        batchIdField.getStyleClass().add("text-field");
        
        // Set up buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Product");
        updateButton = new Button("Update Product");
        deleteButton = new Button("Delete Product");
        clearButton = new Button("Clear Fields");

        addButton.getStyleClass().add("button");
        updateButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button-del");
        clearButton.getStyleClass().add("button-del");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        // Set up TableView to display products
        productTable = new TableView<>();

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);

        TableColumn<Product, String> skuColumn = new TableColumn<>("SKU");
        skuColumn.setCellValueFactory(cellData -> cellData.getValue().skuProperty());
        skuColumn.setPrefWidth(100);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityInStockProperty().asObject());
        quantityColumn.setPrefWidth(80);

        TableColumn<Product, Integer> reorderLevelColumn = new TableColumn<>("Reorder Level");
        reorderLevelColumn.setCellValueFactory(cellData -> cellData.getValue().reorderLevelProperty().asObject());
        reorderLevelColumn.setPrefWidth(100);

        TableColumn<Product, Integer> categoryIdColumn = new TableColumn<>("Category ID");
        categoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().categoryIdProperty().asObject());
        categoryIdColumn.setPrefWidth(100);

        TableColumn<Product, Integer> supplierIdColumn = new TableColumn<>("Supplier ID");
        supplierIdColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        supplierIdColumn.setPrefWidth(100);

        TableColumn<Product, Integer> batchIdColumn = new TableColumn<>("Batch ID");
        batchIdColumn.setCellValueFactory(cellData -> cellData.getValue().batchIdProperty().asObject());
        batchIdColumn.setPrefWidth(100);

        // Add columns to TableView
        productTable.getColumns().setAll(nameColumn, skuColumn, quantityColumn, reorderLevelColumn, categoryIdColumn, supplierIdColumn, batchIdColumn);
        productTable.setItems(productList);
        productTable.setPlaceholder(new Label("No products available"));
        productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // Adjust table resizing policy to avoid extra column
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        
        //for table 
        productTable.getStyleClass().add("table-view");


        // Add event handlers
        addButton.setOnAction(e -> {
            try {
                addProduct();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to add product: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                updateProduct();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to update product: " + ex.getMessage());
            }
        });
        
      // Ensure multiple selection is enabled on the TableView
productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

deleteButton.setOnAction(e -> {
    ObservableList<Product> selectedProducts = productTable.getSelectionModel().getSelectedItems();

    // Check if multiple items are selected
    if (selectedProducts == null || selectedProducts.isEmpty()) {
        showAlert("No Selection", "Please select products to delete.");
        return;
    }

    // Show confirmation alert
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Deletion");
    confirmationAlert.setHeaderText("Are you sure you want to delete the selected products?");
    confirmationAlert.setContentText("This action cannot be undone.");

    confirmationAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                // Create a new list to store products to delete to avoid modifying the original list
                List<Product> productsToDelete = new ArrayList<>(selectedProducts);

                // Iterate over the copy of the selected items and delete each from the database and the productList
                productsToDelete.forEach(product -> {
                    try {
                        productController.deleteProduct(product.getProductId()); // Delete from database
                    } catch (SQLException ex) {
                        showAlert("Database Error", "Failed to delete product: " + ex.getMessage());
                    }
                });

                // Remove all deleted products from productList in one go
                productList.removeAll(productsToDelete);

                // Clear input fields after deletion
                clearFields();

            } catch (Exception ex) {
                showAlert("Unexpected Error", "An unexpected error occurred: " + ex.getMessage());
            }
        }
    });
});
       
        clearButton.setOnAction(e -> clearFields());
        
        
        
        

        // Layout adjustments
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, form, buttonBox, productTable);

        // Populate form fields when a row in the TableView is selected
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }
    private void addProduct() throws SQLException {
        try {
            String name = nameField.getText();
            String sku = skuField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int reorderLevel = Integer.parseInt(reorderLevelField.getText());
            int categoryId = Integer.parseInt(categoryIdField.getText()); // New field for category ID
            int supplierId = Integer.parseInt(supplierIdField.getText()); // New field for supplier ID
            int batchId = Integer.parseInt(batchIdField.getText()); // New field for batch ID

    
            // Create a new Product with all necessary parameters
            Product product = new Product(0, name, categoryId, sku, quantity, reorderLevel, supplierId, batchId);
            productController.addProduct(product); // Add product to database
            productList.add(product); // Add product to TableView
    
            clearFields(); // Clear fields after adding
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter valid numeric values for Quantity, Reorder Level, Category ID, and Supplier ID.");
        }
    }
    

    private void updateProduct() throws SQLException {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                selectedProduct.setName(nameField.getText());
                selectedProduct.setSku(skuField.getText());
                selectedProduct.setQuantityInStock(Integer.parseInt(quantityField.getText()));
                selectedProduct.setReorderLevel(Integer.parseInt(reorderLevelField.getText()));
                selectedProduct.setCategoryId(Integer.parseInt(categoryIdField.getText()));
                selectedProduct.setSupplierId(Integer.parseInt(supplierIdField.getText()));
                selectedProduct.setBatchId(Integer.parseInt(batchIdField.getText()));


                productController.updateProduct(selectedProduct);  // Update in database
                productTable.refresh();  // Refresh TableView
                clearFields();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numeric values for Quantity and Reorder Level.");
            }
        } else {
            showAlert("No Selection", "Please select a product to update.");
        }
    }

    private void deleteProduct()throws SQLException{
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productController.deleteProduct(selectedProduct.getProductId());  // Remove from database
            productList.remove(selectedProduct);  // Remove from TableView
            clearFields();
        } else {
            showAlert("No Selection", "Please select a product to delete.");
        }
    }

    private void clearFields() {
        nameField.clear();
        skuField.clear();
        quantityField.clear();
        reorderLevelField.clear();
        categoryIdField.clear();
        supplierIdField.clear();
        batchIdField.clear();

    }

    private void populateFields(Product product) {
        nameField.setText(product.getName());
        skuField.setText(product.getSku());
        quantityField.setText(String.valueOf(product.getQuantityInStock()));
        reorderLevelField.setText(String.valueOf(product.getReorderLevel()));
        categoryIdField.setText(String.valueOf(product.getCategoryId()));
        supplierIdField.setText(String.valueOf(product.getSupplierId()));
        batchIdField.setText(String.valueOf(product.getBatchId()));


    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
