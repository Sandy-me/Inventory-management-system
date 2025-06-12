package controller;

import model.Batch;
import java.sql.SQLException;
import java.util.List;

public class BatchController {
// Add a new batch
    public void addBatch(Batch batch) throws SQLException {
        batch.save();
    }

    // Update an existing batch
    public void updateBatch(Batch batch) throws SQLException {
        batch.update();
    }

   public void deleteBatch(int batch_id) throws SQLException {
        Batch batch = new Batch(batch_id, 0, null, 0); // Temporary Batch object with ID only
        batch.delete();
    }


    // Fetch all batches for a specific product
    public List<Batch> fetchBatchesByProduct(int product_id) throws SQLException {
        return Batch.fetchByProductId(product_id);
    }
     // Fetch all batches for a specific product
     public List<Batch> fetchBatches(int product_id) throws SQLException {
        return Batch.fetchAll();
    }


    
}
