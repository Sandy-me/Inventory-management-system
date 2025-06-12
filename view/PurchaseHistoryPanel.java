package view;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PurchaseHistory;
import controller.PurchaseHistoryController;

public class PurchaseHistoryPanel extends VBox {

    private TableView<PurchaseHistory> purchaseTable;
    private TextField productIdField, supplierIdField, purchaseDateField, quantityField, costField;
    private Button addButton, refreshButton;
    private PurchaseHistoryController purchaseHistoryController;
    private ObservableList<PurchaseHistory> purchaseList;

    public PurchaseHistoryPanel() throws SQLException {
        this.getStyleClass().add("panel");

        // Initialize controller and data
        purchaseHistoryController = new PurchaseHistoryController();
        purchaseList = FXCollections.observableArrayList(purchaseHistoryController.fetchAllPurchases());

        // Header label
        Label headerLabel = new Label("Purchase History");
        headerLabel.getStyleClass().add("header-label");

        // Set up form layout with labels and fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        // Product ID Field
        Label productIdLabel = new Label("Product ID:");
        productIdLabel.getStyleClass().add("form-label");
        productIdField = new TextField();
        productIdField.setPromptText("Enter Product ID");
        productIdField.getStyleClass().add("text-field");

        // Supplier ID Field
        Label supplierIdLabel = new Label("Supplier ID:");
        supplierIdLabel.getStyleClass().add("form-label");
        supplierIdField = new TextField();
        supplierIdField.setPromptText("Enter Supplier ID");
        supplierIdField.getStyleClass().add("text-field");

        // Purchase Date Field
        Label purchaseDateLabel = new Label("Purchase Date:");
        purchaseDateLabel.getStyleClass().add("form-label");
        purchaseDateField = new TextField();
        purchaseDateField.setPromptText("YYYY-MM-DD");
        purchaseDateField.getStyleClass().add("text-field");

        // Quantity Field
        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("form-label");
        quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");
        quantityField.getStyleClass().add("text-field");

        // Cost Field
        Label costLabel = new Label("Cost:");
        costLabel.getStyleClass().add("form-label");
        costField = new TextField();
        costField.setPromptText("Enter Cost");
        costField.getStyleClass().add("text-field");

        // Adding labels and fields to the form grid with spacing
        form.add(productIdLabel, 0, 0);
        form.add(productIdField, 1, 0);
        form.add(supplierIdLabel, 0, 1);
        form.add(supplierIdField, 1, 1);
        form.add(purchaseDateLabel, 0, 2);
        form.add(purchaseDateField, 1, 2);
        form.add(quantityLabel, 0, 3);
        form.add(quantityField, 1, 3);
        form.add(costLabel, 0, 4);
        form.add(costField, 1, 4);

        // Set up buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Purchase");
        refreshButton = new Button("Refresh");

        addButton.getStyleClass().add("button");
        refreshButton.getStyleClass().add("button");

        buttonBox.getChildren().addAll(addButton, refreshButton);

        // Set up TableView to display purchase history
        purchaseTable = new TableView<>();

        TableColumn<PurchaseHistory, Integer> purchaseIdColumn = new TableColumn<>("Purchase ID");
        purchaseIdColumn.setCellValueFactory(cellData -> cellData.getValue().purchaseIdProperty().asObject());
        purchaseIdColumn.setPrefWidth(100);

        TableColumn<PurchaseHistory, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        productIdColumn.setPrefWidth(100);

        TableColumn<PurchaseHistory, Integer> supplierIdColumn = new TableColumn<>("Supplier ID");
        supplierIdColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        supplierIdColumn.setPrefWidth(100);

        TableColumn<PurchaseHistory, String> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().purchaseDateProperty());
        purchaseDateColumn.setPrefWidth(120);

        TableColumn<PurchaseHistory, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setPrefWidth(80);

        TableColumn<PurchaseHistory, Double> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        costColumn.setPrefWidth(100);

        // Center-align the columns
        purchaseIdColumn.setCellFactory(column -> createCenterAlignedIntegerCell());
        productIdColumn.setCellFactory(column -> createCenterAlignedIntegerCell());
        supplierIdColumn.setCellFactory(column -> createCenterAlignedIntegerCell());
        purchaseDateColumn.setCellFactory(column -> createCenterAlignedStringCell());
        quantityColumn.setCellFactory(column -> createCenterAlignedIntegerCell());
        costColumn.setCellFactory(column -> createCenterAlignedDoubleCell());

        // Add columns to TableView
        purchaseTable.getColumns().setAll(purchaseIdColumn, productIdColumn, supplierIdColumn, purchaseDateColumn, quantityColumn, costColumn);
        purchaseTable.setItems(purchaseList);
        purchaseTable.setPlaceholder(new Label("No purchase history available"));

        // Adjust table resizing policy to avoid extra column
        purchaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        purchaseTable.getStyleClass().add("table-view");

        // Add event handlers
        addButton.setOnAction(e -> {
            try {
                addPurchase();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to add purchase: " + ex.getMessage());
            }
        });

        refreshButton.setOnAction(e -> refreshPurchaseList());

        // Layout adjustments
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, form, buttonBox, purchaseTable);
    }

    private TableCell<PurchaseHistory, String> createCenterAlignedStringCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private TableCell<PurchaseHistory, Integer> createCenterAlignedIntegerCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toString());
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private TableCell<PurchaseHistory, Double> createCenterAlignedDoubleCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%.2f", item));
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private void addPurchase() throws SQLException {
        int productId = Integer.parseInt(productIdField.getText());
        int supplierId = Integer.parseInt(supplierIdField.getText());
        String purchaseDate = purchaseDateField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double cost = Double.parseDouble(costField.getText());

        PurchaseHistory purchase = new PurchaseHistory(0, productId, supplierId, purchaseDate, quantity, cost);
        purchaseHistoryController.addPurchase(purchase);
        purchaseList.add(purchase);
        clearFields();
    }

    private void refreshPurchaseList() {
        try {
            purchaseList.setAll(purchaseHistoryController.fetchAllPurchases());
        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to refresh purchase history: " + ex.getMessage());
        }
    }

    private void clearFields() {
        productIdField.clear();
        supplierIdField.clear();
        purchaseDateField.clear();
        quantityField.clear();
        costField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



