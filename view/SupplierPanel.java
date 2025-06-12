package view;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Supplier;
import controller.SupplierController;

public class SupplierPanel extends VBox {

    private TableView<Supplier> supplierTable;
    private TextField idField, nameField, contactField, addressField;
    private Button addButton, updateButton, deleteButton, clearButton;
    private SupplierController supplierController;
    private ObservableList<Supplier> supplierList;

    public SupplierPanel() throws SQLException {
        this.getStyleClass().add("panel");

        // Initialize controller and data
        supplierController = new SupplierController();
        supplierList = FXCollections.observableArrayList(supplierController.fetchAllSuppliers());

        // Header label
        Label headerLabel = new Label("Supplier Management");
        headerLabel.getStyleClass().add("header-label");

        // Set up form layout with labels and fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        // ID Field
        Label idLabel = new Label("Supplier ID:");
        idLabel.getStyleClass().add("form-label");
        idField = new TextField();
        idField.setPromptText("ID"); // Placeholder text
        idField.setDisable(true); // Disable editing if it's auto-generated in the database
        idField.getStyleClass().add("text-field");

        // Name Field
        Label nameLabel = new Label("Supplier Name:");
        nameLabel.getStyleClass().add("form-label");
        nameField = new TextField();
        nameField.setPromptText("Enter Supplier Name");
        nameField.getStyleClass().add("text-field");

        // Contact Field
        Label contactLabel = new Label("Contact:");
        contactLabel.getStyleClass().add("form-label");
        contactField = new TextField();
        contactField.setPromptText("Enter Contact Information");
        contactField.getStyleClass().add("text-field");

        // Address Field
        Label addressLabel = new Label("Address:");
        addressLabel.getStyleClass().add("form-label");
        addressField = new TextField();
        addressField.setPromptText("Enter Address");
        addressField.getStyleClass().add("text-field");

        // Adding labels and fields to the form grid with spacing
        form.add(idLabel, 0, 0);
        form.add(idField, 1, 0);
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);
        form.add(contactLabel, 0, 2);
        form.add(contactField, 1, 2);
        form.add(addressLabel, 0, 3);
        form.add(addressField, 1, 3);

        // Set up buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Supplier");
        updateButton = new Button("Update Supplier");
        deleteButton = new Button("Delete Supplier");
        clearButton = new Button("Clear Fields");

        addButton.getStyleClass().add("button");
        updateButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button-del");
        clearButton.getStyleClass().add("button-del");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Set up TableView to display suppliers
        supplierTable = new TableView<>();

        TableColumn<Supplier, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        idColumn.setPrefWidth(100);

        TableColumn<Supplier, String> nameColumn = new TableColumn<>("Supplier Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);

        TableColumn<Supplier, String> contactColumn = new TableColumn<>("Contact");
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactInfoProperty());
        contactColumn.setPrefWidth(150);

        TableColumn<Supplier, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        addressColumn.setPrefWidth(150);

        // Center-align the columns with the correct type
        idColumn.setCellFactory(column -> createCenterAlignedIntegerCell());
        nameColumn.setCellFactory(column -> createCenterAlignedStringCell());
        contactColumn.setCellFactory(column -> createCenterAlignedStringCell());
        addressColumn.setCellFactory(column -> createCenterAlignedStringCell());

        // Add columns to TableView
        supplierTable.getColumns().setAll(idColumn, nameColumn, contactColumn, addressColumn);
        supplierTable.setItems(supplierList);
        supplierTable.setPlaceholder(new Label("No suppliers available"));

        // Adjust table resizing policy to avoid extra column
        supplierTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        supplierTable.getStyleClass().add("table-view");

        // Add event handlers
        addButton.setOnAction(e -> {
            try {
                addSupplier();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to add supplier: " + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> {
            try {
                updateSupplier();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to update supplier: " + ex.getMessage());
            }
        });

        supplierTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        deleteButton.setOnAction(e -> {
            ObservableList<Supplier> selectedSuppliers = supplierTable.getSelectionModel().getSelectedItems();
            if (selectedSuppliers == null || selectedSuppliers.isEmpty()) {
                showAlert("No Selection", "Please select suppliers to delete.");
                return;
            }

            // Show confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected suppliers?");
            confirmationAlert.setContentText("This action cannot be undone.");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        for (Supplier supplier : selectedSuppliers) {
                            supplierController.deleteSupplier(supplier);
                        }
                        supplierList.removeAll(selectedSuppliers);
                        clearFields();
                    } catch (SQLException ex) {
                        showAlert("Database Error", "Failed to delete suppliers: " + ex.getMessage());
                    }
                }
            });
        });

        clearButton.setOnAction(e -> clearFields());

        // Layout adjustments
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, form, buttonBox, supplierTable);

        // Populate form fields when a row in the TableView is selected
        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    // Create center-aligned cell for String data
    private TableCell<Supplier, String> createCenterAlignedStringCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    // Create center-aligned cell for Integer data
    private TableCell<Supplier, Integer> createCenterAlignedIntegerCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toString());
                setStyle("-fx-alignment: CENTER;");
            }
        };
    }

    private void addSupplier() throws SQLException {
        String name = nameField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();

        Supplier supplier = new Supplier(0, name, contact, address);
        supplierController.addSupplier(supplier);
        supplierList.add(supplier);
        clearFields();
    }

    private void updateSupplier() throws SQLException {
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier != null) {
            selectedSupplier.setName(nameField.getText());
            selectedSupplier.setContactInfo(contactField.getText());
            selectedSupplier.setAddress(addressField.getText());
            supplierController.updateSupplier(selectedSupplier);
            supplierTable.refresh();
            clearFields();
        } else {
            showAlert("No Selection", "Please select a supplier to update.");
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        contactField.clear();
        addressField.clear();
    }

    private void populateFields(Supplier supplier) {
        idField.setText(String.valueOf(supplier.getSupplierId()));
        nameField.setText(supplier.getName());
        contactField.setText(supplier.getContactInfo());
        addressField.setText(supplier.getAddress());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



