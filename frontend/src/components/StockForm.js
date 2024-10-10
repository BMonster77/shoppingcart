import React, { useState } from 'react';
import axios from 'axios';

const StockForm = ({ product, onStockUpdate }) => {
  const [stock, setStock] = useState(product.storeQuantity);

  const handleStockUpdate = async () => {
    try {
      const updatedProduct = { ...product, storeQuantity: stock };
      await axios.put(`http://localhost:8080/api/admin/products/${product.productId}`, updatedProduct);
      onStockUpdate(updatedProduct); // Update parent component
      alert("Stock updated successfully");
    } catch (error) {
      console.error("Error updating stock:", error);
    }
  };

  return (
    <div>
      <h3>Update Stock for {product.name}</h3>
      <input
        type="number"
        value={stock}
        onChange={(e) => setStock(e.target.value)}
        placeholder="Update Stock Quantity"
      />
      <button onClick={handleStockUpdate}>Update Stock</button>
    </div>
  );
};

export default StockForm;
