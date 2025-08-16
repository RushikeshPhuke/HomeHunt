import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

function UserProperties() {
  const [activeTab, setActiveTab] = useState('');
  const [filters, setFilters] = useState({
    city: '',
    priceRange: '',
    bedrooms: ''
  });
  const [properties, setProperties] = useState([]);
  const [cart, setCart] = useState([]);

  // Helper to get Authorization headers with JWT token
  const getAuthHeaders = () => {
  const token = localStorage.getItem('jwtToken');
  console.log('Token from localStorage:', token);
  return token ? { Authorization: `Bearer ${token}` } : {};
};

  const formatPropertiesWithImages = (data) => {
    return data.map((property) => {
      let imageUrl = 'https://placehold.co/400x200?text=No+Image';

      // If images array exists and has data
      if (property.images && property.images.length > 0) {
        const img = property.images[0];
        if (img.imageData) {
          const imageType = img.imageType || 'image/jpeg';
          imageUrl = `data:${imageType};base64,${img.imageData}`;
        }
      }
      // If flat structure exists (from /all maybe)
      else if (property.imageData && property.imageData !== '') {
        const imageType = property.imageType || 'image/jpeg';
        imageUrl = `data:${imageType};base64,${property.imageData}`;
      }

      return {
        ...property,
        image: imageUrl
      };
    });
  };

  const fetchProperties = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/properties/all', {
        headers: getAuthHeaders()
      });

      const formatted = formatPropertiesWithImages(response.data);
      setProperties(formatted);
    } catch (error) {
      console.error("Error fetching properties:", error);
    }
  };

  const fetchFilteredProperties = async () => {
    try {
      let minPrice = null;
      let maxPrice = null;

      if (filters.priceRange) {
        const parts = filters.priceRange.split('-').map(p => parseInt(p));
        if (parts.length === 2 && !isNaN(parts[0]) && !isNaN(parts[1])) {
          minPrice = parts[0];
          maxPrice = parts[1];
        }
      }

      const response = await axios.get('http://localhost:8080/api/properties/filter', {
        params: {
          city: filters.city || null,
          minPrice: minPrice,
          maxPrice: maxPrice,
          bedrooms: filters.bedrooms ? parseInt(filters.bedrooms) : null
        },
        headers: getAuthHeaders()
      });

      const formatted = formatPropertiesWithImages(response.data);
      setProperties(formatted);
    } catch (error) {
      console.error("Error fetching filtered properties:", error);
    }
  };

  const fetchRentOrSale = async (type) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/properties/${type}`, {
        headers: getAuthHeaders()
      });
      const formatted = formatPropertiesWithImages(response.data);
      setProperties(formatted);
    } catch (error) {
      console.error(`Error fetching ${type} properties:`, error);
    }
  };

  useEffect(() => {
    fetchProperties();
  }, []);

  const handleApplyFilters = () => {
    fetchFilteredProperties();
  };

  const handleClearFilters = () => {
    setFilters({ city: '', priceRange: '', bedrooms: '' });
    setActiveTab('');
    fetchProperties();
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
    fetchRentOrSale(tab); // "rent" or "sale"
  };

  const handleAddToCart = (property) => {
    if (!cart.some((item) => item.propertyId === property.propertyId)) {
      setCart([...cart, property]);
    }
  };

  const handleGoToCart = () => {
    localStorage.setItem('propertyCart', JSON.stringify(cart));
    window.location.href = '/properties/cart';
  };

  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between mb-3">
        <button className="btn btn-outline-dark" onClick={handleGoToCart}>
          Cart ({cart.length})
        </button>

        <button
          className="btn btn-dark text-white"
          onClick={() => {
            localStorage.clear();
            sessionStorage.clear();
            window.location.href = '/';
          }}
        >
          Logout
        </button>
      </div>

      <h1 className="fw-bold">Find Your Perfect Property</h1>
      <p className="text-muted mb-4">
        Discover thousands of properties for rent and sale. Professional broker assistance available.
      </p>

      <div className="row g-3 mb-3">
        <div className="col-md-4">
          <label className="form-label">City</label>
          <select
            className="form-select"
            value={filters.city}
            onChange={(e) => setFilters({ ...filters, city: e.target.value })}
          >
            <option value="">All Cities</option>
            <option value="citycenter">City Center</option>
            <option value="greenvalley">Green Valley</option>
            <option value="midtown">Midtown</option>
            <option value="uptown">Uptown</option>
            <option value="riverside">Riverside</option>
            <option value="downtown">Downtown</option>
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label">Price Range</label>
          <select
            className="form-select"
            value={filters.priceRange}
            onChange={(e) => setFilters({ ...filters, priceRange: e.target.value })}
          >
            <option value="">All Prices</option>
            <option value="0-2000">Below $2000</option>
            <option value="2000-3000">$2000 - $3000</option>
            <option value="3000-5000">$3000 - $5000</option>
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label">Bedrooms</label>
          <select
            className="form-select"
            value={filters.bedrooms}
            onChange={(e) => setFilters({ ...filters, bedrooms: e.target.value })}
          >
            <option value="">All</option>
            <option value="1">1 BHK</option>
            <option value="2">2 BHK</option>
            <option value="3">3 BHK</option>
          </select>
        </div>
      </div>

      <div className="mb-4 d-flex gap-2">
        <button className="btn btn-dark" onClick={handleApplyFilters}>
          Apply Filters
        </button>

        <button className="btn btn-outline-secondary" onClick={handleClearFilters}>
          Clear Filters
        </button>
      </div>

      <hr className="my-3" />

      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="text-muted fs-5">
          {properties.length} {properties.length === 1 ? 'Property' : 'Properties'} Found
        </div>
        <div className="btn-group">
          <button
            type="button"
            className={`btn ${activeTab === 'sale' ? 'btn-dark' : 'btn-outline-dark'}`}
            onClick={() => handleTabClick('sale')}
          >
            Sale
          </button>
          <button
            type="button"
            className={`btn ${activeTab === 'rent' ? 'btn-dark' : 'btn-outline-dark'}`}
            onClick={() => handleTabClick('rent')}
          >
            Rent
          </button>
        </div>
      </div>

      <div className="row g-4">
        {properties.map((property) => (
          <div className="col-md-4" key={property.propertyId}>
            <div className="card h-100 shadow-sm">
              <img
                src={property.image}
                className="card-img-top"
                alt="Property"
                style={{ height: '200px', objectFit: 'cover' }}
              />
              <div className="card-body">
                <h5 className="card-title fw-bold">${property.price}</h5>
                <p className="card-text text-muted">
                  {property.address}, {property.city}
                </p>
                <p className="text-muted small mb-2">
                  {property.bedrooms} Beds • {property.bathrooms} Baths • {property.area} sqft
                </p>

                {property.ownerName || property.ownerEmail || property.ownerPhone ? (
                  <div className="mb-2 small text-muted">
                    <strong>Owner:</strong> {property.ownerName}<br />
                    <strong>Phone:</strong> {property.ownerPhone}<br />
                    <strong>Email:</strong> {property.ownerEmail}
                  </div>
                ) : (
                  <div className="mb-2 small text-muted">No owner information</div>
                )}

                <button
                  className="btn btn-primary mt-2"
                  onClick={() => handleAddToCart(property)}
                  disabled={cart.some(item => item.propertyId === property.propertyId)}
                >
                  {cart.some(item => item.propertyId === property.propertyId)
                    ? 'Added to Cart'
                    : 'Add to Cart'}
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default UserProperties;
