import React, { useState } from 'react';
import ProductSearch from './components/ProductSearch';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import StockForm from './components/StockForm';
import AddProductForm from './components/AddProductForm';

const App = () => {
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [stockProduct, setStockProduct] = useState(null);

  const handleProductSelect = (product) => {
    setSelectedProduct(product);
    setStockProduct(null); // Hide stock form
  };

  const handleStockSelect = (product) => {
    setStockProduct(product);
    setSelectedProduct(null); // Hide edit form
  };

  const handleSave = () => {
    setSelectedProduct(null); // Reset after saving
  };

  const handleStockUpdate = (updatedProduct) => {
    setProducts(
      products.map((product) =>
        product.productId === updatedProduct.productId ? updatedProduct : product
      )
    );
    setStockProduct(null); // Reset after updating stock
  };

  const handleDeleteProduct = (productId) => {
    setProducts(products.filter((product) => product.productId !== productId));
  };

  const handleVisibilityToggle = (updatedProduct) => {
    setProducts(
      products.map((product) =>
        product.productId === updatedProduct.productId ? updatedProduct : product
      )
    );
  };

  const handleProductAdded = (newProduct) => {
    setProducts([...products, newProduct]);  // Add new product to the list
  };

  return (
    <div>
      <h1>Admin Panel</h1>
      <AddProductForm onProductAdded={handleProductAdded} />  {/* Add Product Form */}
      <ProductSearch setProducts={setProducts} />
      <ProductList
        products={products}
        onSelectProduct={handleProductSelect}
        onDeleteProduct={handleDeleteProduct}
        onStockSelect={handleStockSelect}
        onVisibilityToggle={handleVisibilityToggle}
      />
      {selectedProduct && <ProductForm selectedProduct={selectedProduct} onSave={handleSave} />}
      {stockProduct && <StockForm product={stockProduct} onStockUpdate={handleStockUpdate} />}
    </div>
  );
};

export default App;
