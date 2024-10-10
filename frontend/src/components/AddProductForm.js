import React, { useState } from 'react';
import axios from 'axios';

const AddProductForm = ({ onProductAdded }) => {
  const [newProduct, setNewProduct] = useState({
    name: '',
    category: '',
    price: '0.00',
    storeQuantity: 0,
    visible: true
  });

  const handleAddProduct = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/admin/products', newProduct);
      alert('Product added successfully');
      onProductAdded(response.data);  // Notify parent component
    } catch (error) {
      console.error("Error adding product:", error);
    }
  };

  return (
    <div>
      <h3>Add New Product</h3>
      <input
        type="text"
        value={newProduct.name}
        onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
        placeholder="Product Name"
      />
      <input
        type="text"
        value={newProduct.category}
        onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
        placeholder="Category"
      />
      <input
        type="number"
        value={parseFloat(newProduct.price).toFixed(2)}  // Format price
        onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value).toFixed(2) })}
        placeholder="Price"
      />
      <input
        type="number"
        value={newProduct.storeQuantity}
        onChange={(e) => setNewProduct({ ...newProduct, storeQuantity: e.target.value })}
        placeholder="Stock Quantity"
      />
      <label>
        Visible:
        <input
          type="checkbox"
          checked={newProduct.visible}
          onChange={(e) => setNewProduct({ ...newProduct, visible: e.target.checked })}
        />
      </label>
      <button onClick={handleAddProduct}>Add Product</button>
    </div>
  );
};

export default AddProductForm;
