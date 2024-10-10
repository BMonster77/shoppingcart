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
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <a className="navbar-brand" href="/">Admin Panel</a>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav mr-auto">
            <li className="nav-item active">
              <a className="nav-link" href="/">Home</a>
            </li>
          </ul>
        </div>
      </nav>

      <div className="container">
        <div className="row">
          <div className="col-md-8">
            <ProductSearch setProducts={setProducts} />
            <ProductList
              products={products}
              onSelectProduct={handleProductSelect}
              onDeleteProduct={handleDeleteProduct}
              onStockSelect={handleStockSelect}
              onVisibilityToggle={handleVisibilityToggle}
            />
          </div>
          <div className="col-md-4">
            {selectedProduct && <ProductForm selectedProduct={selectedProduct} onSave={handleSave} />}
            {stockProduct && <StockForm product={stockProduct} onStockUpdate={handleStockUpdate} />}
            <AddProductForm onProductAdded={handleProductAdded} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default App;
