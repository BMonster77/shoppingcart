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
    <ul className="list-group mb-3">
      {products.map((product) => (
        <li key={product.productId} className="list-group-item d-flex justify-content-between align-items-center">
          <div>
            <strong>{product.name}</strong> ({product.category}) - ${product.price.toFixed(2)}
          </div>
          <div>
            <button className="btn btn-outline-info btn-sm mr-2" onClick={() => onSelectProduct(product)}>Edit</button>
            <button className="btn btn-outline-success btn-sm mr-2" onClick={() => onStockSelect(product)}>Stock</button>
            <button className="btn btn-outline-warning btn-sm mr-2" onClick={() => handleToggleVisibility(product)}>
              {product.visible ? "Hide" : "Show"}
            </button>
            <button className="btn btn-outline-danger btn-sm" onClick={() => handleDelete(product.productId)}>Delete</button>
          </div>
        </li>
      ))}
    </ul>
  );
};

export default ProductList;
