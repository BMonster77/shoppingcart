import React, { useState } from 'react';
import axios from 'axios';

const AddProductForm = ({ onProductAdded }) => {
  const [newProduct, setNewProduct] = useState({
    name: '',
    category: '',
    description: '',
    price: '0.00',
    discount: '0.00',
    storeQuantity: 0,
    imageUrl: '',
    tags: '',
    viewCount: 0,
    visible: true
  });
  const [imagePreview, setImagePreview] = useState('');  // Preview state

  const handleAddProduct = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/admin/products', newProduct);
      alert('Product added successfully');
      onProductAdded(response.data);  // Notify parent component
    } catch (error) {
      console.error("Error adding product:", error);
    }
  };

  const handleImageChange = (e) => {
    const url = e.target.value;
    setNewProduct({ ...newProduct, imageUrl: url });
    setImagePreview(url);  // Update the preview
  };

  const handleBlur = (field, value) => {
    const formattedValue = parseFloat(value).toFixed(2);
    setNewProduct({ ...newProduct, [field]: formattedValue });
  };

  return (
    <div className="card p-3 mb-3">
      <h3 className="mb-3">Add New Product</h3>
      <div className="form-group">
        <label>Product Name</label>
        <input
          type="text"
          className="form-control"
          value={newProduct.name}
          onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
          placeholder="Product Name"
        />
      </div>
      <div className="form-group">
        <label>Category</label>
        <input
          type="text"
          className="form-control"
          value={newProduct.category}
          onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
          placeholder="Category"
        />
      </div>
      <div className="form-group">
        <label>Description</label>
        <textarea
          className="form-control"
          value={newProduct.description}
          onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
          placeholder="Description"
        />
      </div>
      <div className="form-group">
        <label>Price</label>
        <input
          type="number"
          className="form-control"
          value={newProduct.price}
          onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
          onBlur={(e) => handleBlur('price', e.target.value)}
          placeholder="Price"
        />
      </div>
      <div className="form-group">
        <label>Discount</label>
        <input
          type="number"
          className="form-control"
          value={newProduct.discount}
          onChange={(e) => setNewProduct({ ...newProduct, discount: e.target.value })}
          onBlur={(e) => handleBlur('discount', e.target.value)}
          placeholder="Discount"
        />
      </div>
      <div className="form-group">
        <label>Stock Quantity</label>
        <input
          type="number"
          className="form-control"
          value={newProduct.storeQuantity}
          onChange={(e) => setNewProduct({ ...newProduct, storeQuantity: e.target.value })}
          placeholder="Stock Quantity"
        />
      </div>
      <div className="form-group">
        <label>Image URL</label>
        <input
          type="text"
          className="form-control"
          value={newProduct.imageUrl || ''}
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
          value={newProduct.tags || ''}
          onChange={(e) => setNewProduct({ ...newProduct, tags: e.target.value })}
          placeholder="Tags"
        />
      </div>
      <div className="form-group">
        <label>View Count</label>
        <input
          type="number"
          className="form-control"
          value={newProduct.viewCount || 0}
          onChange={(e) => setNewProduct({ ...newProduct, viewCount: e.target.value })}
          placeholder="View Count"
        />
      </div>
      <div className="form-check mb-3">
        <input
          type="checkbox"
          className="form-check-input"
          checked={newProduct.visible}
          onChange={(e) => setNewProduct({ ...newProduct, visible: e.target.checked })}
        />
        <label className="form-check-label">Visible</label>
      </div>
      <button className="btn btn-primary" onClick={handleAddProduct}>Add Product</button>
    </div>
  );
};

export default AddProductForm;
