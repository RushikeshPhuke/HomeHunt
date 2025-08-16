import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

function Cart() {
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    const storedCart = localStorage.getItem('propertyCart');
    if (storedCart) {
      setCartItems(JSON.parse(storedCart));
    }
  }, []);

  const handleRemove = (propertyId) => {
    const updatedCart = cartItems.filter(item => item.propertyId !== propertyId);
    setCartItems(updatedCart);
    localStorage.setItem('propertyCart', JSON.stringify(updatedCart));
  };

  const handleCheckout = () => {
    // Placeholder for actual booking logic
    alert('Checkout successful! ');
    localStorage.removeItem('propertyCart');
    setCartItems([]);
  };

  return (
    <div className="container py-5">
      <h1 className="fw-bold mb-4">Your Property Cart</h1>

      {cartItems.length === 0 ? (
        <div className="alert alert-info">Your cart is empty. Go add some properties!</div>
      ) : (
        <>
          <div className="row g-4">
            {cartItems.map((property) => (
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

                    <button
                      className="btn btn-outline-danger mt-3"
                      onClick={() => handleRemove(property.propertyId)}
                    >
                      Remove
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="mt-4 text-end">
            <button className="btn btn-success" onClick={handleCheckout}>
              Proceed to Checkout ({cartItems.length} properties)
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Cart;
