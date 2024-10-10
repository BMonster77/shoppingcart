import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ProductForm = ({ selectedProduct, onSave }) => {
  const [product, setProduct] = useState(selectedProduct);

  useEffect(() => {
    setProduct(selectedProduct);
  }, [selectedProduct]);

  const handleSave = async () => {
    try {
      await axios.put(`http://localhost:8080/api/admin/products/${product.productId}`, product);
      alert("Product updated successfully");
      onSave();
    } catch (error) {
      console.error("Error updating product:", error);
    }
  };

  if (!product) return null;

  return (
    <div>
      <h3>Edit Product</h3>
      <input
        type="text"
        value={product.name}
        onChange={(e) => setProduct({ ...product, name: e.target.value })}
        placeholder="Product Name"
      />
      <input
        type="text"
        value={product.category}
        onChange={(e) => setProduct({ ...product, category: e.target.value })}
        placeholder="Category"
      />
      <input
        type="number"
        value={parseFloat(product.price).toFixed(2)}  // Ensure price shows as two decimal places
        onChange={(e) => setProduct({ ...product, price: parseFloat(e.target.value).toFixed(2) })}
        placeholder="Price"
      />
      <input
        type="number"
        value={product.storeQuantity}
        onChange={(e) => setProduct({ ...product, storeQuantity: e.target.value })}
        placeholder="Stock Quantity"
      />
      <label>
        Visible:
        <input
          type="checkbox"
          checked={product.visible}
          onChange={(e) => setProduct({ ...product, visible: e.target.checked })}
        />
      </label>
      <button onClick={handleSave}>Save</button>
    </div>
  );
};

export default ProductForm;
