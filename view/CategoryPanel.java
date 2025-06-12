package view;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Category;
import controller.CategoryController;

public class CategoryPanel extends VBox {

    private TableView<Category> categoryTable;
    private TextField idField, nameField;
    private Button addButton, updateButton, deleteButton, clearButton;
    private CategoryController categoryController;
    private ObservableList<Category> categoryList;

    public CategoryPanel() throws SQLException {
        this.getStyleClass().add("panel");

        // Initialize controller and data
        categoryController = new CategoryController();
        categoryList = FXCollections.observableArrayList(categoryController.fetchAllCategories());

        // Header label
        Label headerLabel = new Label("Category Management");
        headerLabel.getStyleClass().add("header-label");

        // Set up form layout with labels and fields
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        // ID Field
        Label idLabel = new Label("Category ID:");
        idLabel.getStyleClass().add("form-label");
        idField = new TextField();
        idField.setPromptText("ID"); // Placeholder text
        idField.setDisable(true); // Disable editing if it's auto-generated in the database
        idField.getStyleClass().add("text-field");

        // Name Field
        Label nameLabel = new Label("Category Name:");
        nameLabel.getStyleClass().add("form-label");
        nameField = new TextField();
        nameField.setPromptText("Enter Category Name");
        nameField.getStyleClass().add("text-field");

        // Adding labels and fields to the form grid with spacing
        form.add(idLabel, 0, 0);
        form.add(idField, 1, 0);
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);

        // Set up buttons
        HBox buttonBox = new HBox(10);
        addButton = new Button("Add Category");
        updateButton = new Button("Update Category");
        deleteButton = new Button("Delete Category");
        clearButton = new Button("Clear Fields");

        addButton.getStyleClass().add("button");
        updateButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button-del");
        clearButton.getStyleClass().add("button-del");

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Set up TableView to display categories
        categoryTable = new TableView<>();

        TableColumn<Category, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().categoryIdProperty().asObject());
        idColumn.setPrefWidth(100);

        TableColumn<Category, String> nameColumn = new TableColumn<>("Category Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        nameColumn.setPrefWidth(150);
        
        // Center-align the ID column
        idColumn.setCellFactory(column -> {
            return new TableCell<Category, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                        setStyle("-fx-alignment: CENTER;"); // Center alignment
                    }
                }
            };
        });

        // Center-align the Category Name column
        nameColumn.setCellFactory(column -> {
            return new TableCell<Category, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        setStyle("-fx-alignment: CENTER;"); // Center alignment
                    }
                }
            };
        });

        // Add columns to TableView
        categoryTable.getColumns().setAll(idColumn, nameColumn);
        categoryTable.setItems(categoryList);
        categoryTable.setPlaceholder(new Label("No categories available"));

        // Adjust table resizing policy to avoid extra column
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        categoryTable.getStyleClass().add("table-view");

        // Add event handlers
        addButton.setOnAction(e -> {
            try {
                addCategory();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to add category: " + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> {
            try {
                updateCategory();
            } catch (SQLException ex) {
                showAlert("Database Error", "Failed to update category: " + ex.getMessage());
            }
        });

        categoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        deleteButton.setOnAction(e -> {
            ObservableList<Category> selectedCategories = categoryTable.getSelectionModel().getSelectedItems();
            if (selectedCategories == null || selectedCategories.isEmpty()) {
                showAlert("No Selection", "Please select categories to delete.");
                return;
            }

            // Show confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected categories?");
            confirmationAlert.setContentText("This action cannot be undone.");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        for (Category category : selectedCategories) {
                            categoryController.deleteCategory(category);
                        }
                        categoryList.removeAll(selectedCategories);
                        clearFields();
                    } catch (SQLException ex) {
                        showAlert("Database Error", "Failed to delete categories: " + ex.getMessage());
                    }
                }
            });
        });

        clearButton.setOnAction(e -> clearFields());

        // Layout adjustments
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(headerLabel, form, buttonBox, categoryTable);

        // Populate form fields when a row in the TableView is selected
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

   
private void addCategory() throws SQLException {
    String name = nameField.getText();

    // Create a Category object with a temporary ID (0)
    Category category = new Category(0, name);

    // Save the category to the database, retrieving and setting the generated ID
    categoryController.addCategory(category);

    // Now the `category` object should have its ID updated from the database
    categoryList.add(category); // Add category to the TableView with correct ID
    clearFields();
}



    private void updateCategory() throws SQLException {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            selectedCategory.setCategoryName(nameField.getText());
            categoryController.updateCategory(selectedCategory);
            categoryTable.refresh();
            clearFields();
        } else {
            showAlert("No Selection", "Please select a category to update.");
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
    }

    private void populateFields(Category category) {
        idField.setText(String.valueOf(category.getCategoryId()));
        nameField.setText(category.getCategoryName());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

