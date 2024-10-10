import React, { useState } from 'react';
import axios from 'axios';

const ProductSearch = ({ setProducts }) => {
  const [searchParams, setSearchParams] = useState({
    productId: '',
    name: '',
    category: ''
  });

  const handleSearch = async () => {
    let query = '';
    if (searchParams.productId) {
      query = `/id/${searchParams.productId}`;
    } else if (searchParams.name) {
      query = `/name/${searchParams.name}`;
    } else if (searchParams.category) {
      query = `/category/${searchParams.category}`;
    }

    try {
      const response = await axios.get(`http://localhost:8080/api/admin/products${query}`);
      setProducts(response.data);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };

  return (
    <div className="card mb-3 p-3">
      <h4>Search Products</h4>
      <div className="form-row mb-3">
        <input
          type="text"
          className="form-control col-md-3 mr-2"
          placeholder="Search by ID"
          value={searchParams.productId}
          onChange={(e) => setSearchParams({ ...searchParams, productId: e.target.value })}
        />
        <input
          type="text"
          className="form-control col-md-3 mr-2"
          placeholder="Search by Name"
          value={searchParams.name}
          onChange={(e) => setSearchParams({ ...searchParams, name: e.target.value })}
        />
        <input
          type="text"
          className="form-control col-md-3 mr-2"
          placeholder="Search by Category"
          value={searchParams.category}
          onChange={(e) => setSearchParams({ ...searchParams, category: e.target.value })}
        />
        <button onClick={handleSearch} className="btn btn-primary col-md-2">Search</button>
      </div>
    </div>
  );
};

export default ProductSearch;
