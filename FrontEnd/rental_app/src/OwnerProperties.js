import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

function OwnerProperties() {
  const navigate = useNavigate();
  const [ownerProperties, setOwnerProperties] = useState([]);

  useEffect(() => {
  const ownerId = localStorage.getItem("ownerId");
  const token = localStorage.getItem("token");

  if (!ownerId || !token) {
    // If either ownerId or JWT token is missing → send to login
    navigate('/login');
    return;
  }

  axios.get(`http://localhost:8080/api/properties/owner/${ownerId}`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
    .then(response => {
      const propertiesWithImages = response.data.map(property => {
        let imageUrl = 'https://via.placeholder.com/400x200?text=No+Image';

        if (property.images && property.images.length > 0) {
          const firstImage = property.images[0];
          if (firstImage.imageData && firstImage.imageType) {
            imageUrl = `data:${firstImage.imageType};base64,${firstImage.imageData}`;
          }
        }

        return {
          ...property,
          image: imageUrl
        };
      });

      setOwnerProperties(propertiesWithImages);
    })
    .catch(error => {
      console.error("Error fetching owner properties:", error);
    });
}, [navigate]);


  return (
    <div>
      <nav className="navbar navbar-light bg-light px-4 shadow-sm">
        <a className="navbar-brand fw-bold fs-4" href="/">HomeHunt</a>
        <div className="ms-auto d-flex align-items-center gap-3">
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
      </nav>

      <div className="container py-5">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h1 className="fw-bold">My Properties</h1>
          <button
            className="btn btn-primary"
            onClick={() => navigate('/registerproperty')}
          >
            + Register New Property
          </button>
        </div>

        <p className="text-muted mb-4">
          Manage your properties listed for rent and sale.
        </p>

        <hr className="my-3" />

        <div className="d-flex justify-content-between align-items-center mb-4">
          <div className="text-muted fs-5">
            {ownerProperties.length} {ownerProperties.length === 1 ? 'Property' : 'Properties'} Found
          </div>
        </div>

        <div className="row g-4">
          {ownerProperties.length > 0 ? (
            ownerProperties.map((property) => (
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
                    <p className="text-muted small mb-0">
                      {property.bedrooms} Beds • {property.bathrooms} Baths • {property.area} sqft
                    </p>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="col-12 text-center py-5">
              <h4>No properties found</h4>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default OwnerProperties;
