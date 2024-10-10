import React from 'react';
import axios from 'axios';

const ProductList = ({ products, onSelectProduct, onDeleteProduct, onStockSelect, onVisibilityToggle }) => {
  if (!products || products.length === 0) return <div>No products found</div>;

  const handleToggleVisibility = async (product) => {
    try {
      const updatedProduct = { ...product, visible: !product.visible };
      await axios.put(`http://localhost:8080/api/admin/products/${product.productId}`, updatedProduct);
      onVisibilityToggle(updatedProduct); // Notify parent component
    } catch (error) {
      console.error("Error toggling visibility:", error);
    }
  };

  const handleDelete = async (productId) => {
    try {
      await axios.delete(`http://localhost:8080/api/admin/products/${productId}`);
      onDeleteProduct(productId); // Call the parent to update the list
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };

  return (
    <ul>
      {products.map((product) => (
        <li key={product.productId}>
          {product.name} ({product.category}) - ${product.price.toFixed(2)}  {/* Format price */}
          <button onClick={() => onSelectProduct(product)}>Edit</button>
          <button onClick={() => onStockSelect(product)}>Update Stock</button>
          <button onClick={() => handleToggleVisibility(product)}>
            {product.visible ? "Hide" : "Show"}
          </button>
          <button onClick={() => handleDelete(product.productId)}>Delete</button>
        </li>
      ))}
    </ul>
  );
};

export default ProductList;
