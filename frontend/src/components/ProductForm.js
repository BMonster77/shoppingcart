import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ProductForm = ({ selectedProduct, onSave }) => {
  const [product, setProduct] = useState(selectedProduct);
  const [imagePreview, setImagePreview] = useState(selectedProduct.imageUrl || '');  // Preview state

  useEffect(() => {
    setProduct(selectedProduct);
    setImagePreview(selectedProduct.imageUrl || '');  // Set preview when a product is selected
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

  const handleImageChange = (e) => {
    const url = e.target.value;
    setProduct({ ...product, imageUrl: url });
    setImagePreview(url);  // Update the preview
  };

  const handleBlur = (field, value) => {
    const formattedValue = parseFloat(value).toFixed(2);
    setProduct({ ...product, [field]: formattedValue });
  };

  if (!product) return null;

  return (
    <div className="card p-3 mb-3">
      <h3 className="mb-3">Edit Product</h3>
      <div className="form-group">
        <label>Product Name</label>
        <input
          type="text"
          className="form-control"
          value={product.name}
          onChange={(e) => setProduct({ ...product, name: e.target.value })}
          placeholder="Product Name"
        />
      </div>
      <div className="form-group">
        <label>Category</label>
        <input
          type="text"
          className="form-control"
          value={product.category}
          onChange={(e) => setProduct({ ...product, category: e.target.value })}
          placeholder="Category"
        />
      </div>
      <div className="form-group">
        <label>Description</label>
        <textarea
          className="form-control"
          value={product.description || ''}
          onChange={(e) => setProduct({ ...product, description: e.target.value })}
          placeholder="Description"
        />
      </div>
      <div className="form-group">
        <label>Price</label>
        <input
          type="number"
          className="form-control"
          value={product.price}
          onChange={(e) => setProduct({ ...product, price: e.target.value })}
          onBlur={(e) => handleBlur('price', e.target.value)}
          placeholder="Price"
        />
      </div>
      <div className="form-group">
        <label>Discount</label>
        <input
          type="number"
          className="form-control"
          value={product.discount}
          onChange={(e) => setProduct({ ...product, discount: e.target.value })}
          onBlur={(e) => handleBlur('discount', e.target.value)}
          placeholder="Discount"
        />
      </div>
      <div className="form-group">
        <label>Stock Quantity</label>
        <input
          type="number"
          className="form-control"
          value={product.storeQuantity}
          onChange={(e) => setProduct({ ...product, storeQuantity: e.target.value })}
          placeholder="Stock Quantity"
        />
      </div>
      <div className="form-group">
        <label>Image URL</label>
        <input
          type="text"
          className="form-control"
          value={product.imageUrl || ''}
          onChange={handleImageChange}
          placeholder="Image URL"
        />
      </div>
      {imagePreview && (
        <div className="form-group">
          <label>Image Preview</label>
          <img src={imagePreview} alt="Preview" className="img-fluid mb-3" style={{ maxWidth: '200px' }} />
        </div>
      )}
      <div className="form-group">
        <label>Tags</label>
        <input
          type="text"
          className="form-control"
          value={product.tags || ''}
          onChange={(e) => setProduct({ ...product, tags: e.target.value })}
          placeholder="Tags"
        />
      </div>
      <div className="form-group">
        <label>View Count</label>
        <input
          type="number"
          className="form-control"
          value={product.viewCount || 0}
          onChange={(e) => setProduct({ ...product, viewCount: e.target.value })}
          placeholder="View Count"
        />
      </div>
      <div className="form-check mb-3">
        <input
          type="checkbox"
          className="form-check-input"
          checked={product.isVisible}
          onChange={(e) => setProduct({ ...product, isVisible: e.target.checked })}
        />
        <label className="form-check-label">Visible</label>
      </div>
      <button className="btn btn-primary" onClick={handleSave}>Save</button>
    </div>
  );
};

export default ProductForm;
