package controller;

import model.PurchaseHistory;
import java.sql.SQLException;
import java.util.List;

public class PurchaseHistoryController {
    // Add new purchase record
    public void addPurchase(PurchaseHistory purchase) throws SQLException {
        purchase.save();
    }

    // Fetch purchase history
    public List<PurchaseHistory> fetchAllPurchases() throws SQLException {
        return PurchaseHistory.fetchAll();
    }
}


