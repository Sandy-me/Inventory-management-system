package view;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Batch;
import controller.BatchController;

public class BatchPanel extends VBox {

    private TableView<Batch> batchTable;
    private TextField idField, productIdField, expiryDateField, quantityField;
    private Button addButton, updateButton, deleteButton, clearButton;
    private BatchController batchController;
    private ObservableList<Batch> batchList;

    public BatchPanel() throws SQLException {
        this.getStyleClass().add("panel");

        // Initialize controller and data
        batchController = new BatchController();
        batchList = FXCollections.observableArrayList(batchController.fetchBatchesByProduct(0)); // Sample data

        // Header label
        Label headerLabel = new Label("Batch Management");
        headerLabel.getStyleClass().add("header-label");

        // Set up form layout with labels and fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        // ID Field
        Label idLabel = new Label("Batch ID:");
        idLabel.getStyleClass().add("form-label");
        idField = new TextField();
        idField.setPromptText("ID");
        idField.setDisable(true);
        idField.getStyleClass().add("text-field");

        // Product ID Field
        Label productIdLabel = new Label("Product ID:");
        productIdLabel.getStyleClass().add("form-label");
        productIdField = new TextField();
        productIdField.setPromptText("Enter Product ID");
        productIdField.getStyleClass().add("text-field");

        // Expiry Date Field
        Label expiryDateLabel = new Label("Expiry Date:");
        expiryDateLabel.getStyleClass().add("form-label");
        expiryDateField = new TextField();
        expiryDateField.setPromptText("Enter Expiry Date");
        expiryDateField.getStyleClass().add("text-field");

        // Quantity Field
        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("form-label");
        quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");
        quantityField.getStyleClass().add("text-field");

        // Adding labels and fields to the form grid
        form.add(idLabel, 0, 0);
        form.add(idField, 1, 0);
        form.add(productIdLabel, 0, 1);
        form.add(productIdField, 1, 1);
        form.add(expiryDateLabel, 0, 2);
        form.add(expiryDateField, 1, 2);
        form.add(quantityLabel, 0, 3);
        form.add(quantityField, 1, 3);

        // Set up buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Batch");
        updateButton = new Button("Update Batch");
        deleteButton = new Button("Delete Batch");
        clearButton = new Button("Clear Fields");

        addButton.getStyleClass().add("button");
        updateButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button-del");
        clearButton.getStyleClass().add("button-del");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Set up TableView to display batches
        batchTable = new TableView<>();

        TableColumn<Batch, Integer> idColumn = new TableColumn<>("Batch ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().batchIdProperty().asObject());
        idColumn.setCellFactory(column -> createCenterAlignedCellForInteger());
        idColumn.setPrefWidth(100);

        TableColumn<Batch, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        productIdColumn.setCellFactory(column -> createCenterAlignedCellForInteger());
        productIdColumn.setPrefWidth(150);

        TableColumn<Batch, String> expiryDateColumn = new TableColumn<>("Expiry Date");
        expiryDateColumn.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());
        expiryDateColumn.setCellFactory(column -> createCenterAlignedCellForString());
        expiryDateColumn.setPrefWidth(150);

        TableColumn<Batch, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityInBatchProperty().asObject());
        quantityColumn.setCellFactory(column -> createCenterAlignedCellForInteger());
        quantityColumn.setPrefWidth(100);

        // Add columns to TableView
        batchTable.getColumns().setAll(idColumn, productIdColumn, expiryDateColumn, quantityColumn);
        batchTable.setItems(batchList);
        batchTable.setPlaceholder(new Label("No batches available"));

        // Adjust table resizing policy
        batchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        batchTable.getStyleClass().add("table-view");

        // Add event handlers
        addButton.setOnAction(e -> {
            try {
                addBatch();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to add batch: " + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> {
            try {
                updateBatch();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to update batch: " + ex.getMessage());
            }
        });

        deleteButton.setOnAction(e -> deleteBatch());

        clearButton.setOnAction(e -> clearFields());

        // Layout adjustments
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, form, buttonBox, batchTable);

        // Populate form fields when a row in the TableView is selected
        batchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private TableCell<Batch, Integer> createCenterAlignedCellForInteger() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toString());
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private TableCell<Batch, String> createCenterAlignedCellForString() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private void addBatch() throws SQLException {
        int productId = Integer.parseInt(productIdField.getText());
        String expiryDate = expiryDateField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        Batch batch = new Batch(0, productId, expiryDate, quantity);
        batchController.addBatch(batch);
        batchList.add(batch);
        clearFields();
    }

    private void updateBatch() throws SQLException {
        Batch selectedBatch = batchTable.getSelectionModel().getSelectedItem();
        if (selectedBatch != null) {
            selectedBatch.setProductId(Integer.parseInt(productIdField.getText()));
            selectedBatch.setExpiryDate(expiryDateField.getText());
            selectedBatch.setQuantityInBatch(Integer.parseInt(quantityField.getText()));
            batchController.updateBatch(selectedBatch);
            batchTable.refresh();
            clearFields();
        } else {
            showAlert("No Selection", "Please select a batch to update.");
        }
    }

    private void deleteBatch() {
        ObservableList<Batch> selectedBatches = batchTable.getSelectionModel().getSelectedItems();
        if (selectedBatches.isEmpty()) {
            showAlert("No Selection", "Please select batches to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete the selected batches?");
        confirmationAlert.setContentText("This action cannot be undone.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    for (Batch batch : selectedBatches) {
                        batchController.deleteBatch(batch.getBatchId());
                    }
                    batchList.removeAll(selectedBatches);
                    clearFields();
                } catch (SQLException ex) {
                    showAlert("Database Error", "Failed to delete batches: " + ex.getMessage());
                }
            }
        });
    }

    private void clearFields() {
        idField.clear();
        productIdField.clear();
        expiryDateField.clear();
        quantityField.clear();
    }

    private void populateFields(Batch batch) {
        idField.setText(String.valueOf(batch.getBatchId()));
        productIdField.setText(String.valueOf(batch.getProductId()));
        expiryDateField.setText(batch.getExpiryDate());
        quantityField.setText(String.valueOf(batch.getQuantityInBatch()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



