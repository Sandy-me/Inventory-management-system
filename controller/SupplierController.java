package controller;

import model.Supplier;
import java.sql.SQLException;
import java.util.List;

public class SupplierController {
    // Add new supplier
    public void addSupplier(Supplier supplier) throws SQLException {
        supplier.save();
    }

    // Update supplier
    public void updateSupplier(Supplier supplier) throws SQLException {
        supplier.update();
    }

    // Delete supplier
    public void deleteSupplier(Supplier supplier) throws SQLException {
        supplier.delete();
    }

    // Fetch all suppliers
    public List<Supplier> fetchAllSuppliers() throws SQLException {
        return Supplier.fetchAll();
    }

}
