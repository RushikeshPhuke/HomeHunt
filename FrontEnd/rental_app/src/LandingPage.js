import React from 'react';
import { Link } from 'react-router-dom';

function LandingPage() {
  return (
    <div style={{ fontFamily: 'Arial, sans-serif', margin: 0, padding: 0, color: '#333' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', padding: '20px', alignItems: 'center', maxWidth: '1200px', margin: '0 auto' }}>
        <div style={{ fontSize: '28px', fontWeight: 'bold' }}>HomeHunt</div>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <Link to="/register" style={{ marginLeft: '20px', textDecoration: 'none', color: '#333', fontSize: '20px' }}>Buy</Link>
          <Link to="/register" style={{ marginLeft: '20px', textDecoration: 'none', color: '#333', fontSize: '20px' }}>Rent</Link>

          {/* Simplified Login Button */}
          <Link
            to="/login"
            style={{
              marginLeft: '20px',
              background: 'rgb(35 133 185)',
              color: 'white',
              padding: '8px 16px',
              borderRadius: '4px',
              fontSize: '20px',
              textDecoration: 'none',
              cursor: 'pointer',
            }}
          >
            Login
          </Link>
        </div>
      </div>

      {/* tagline Section */}
      <div style={{ display: 'flex', maxWidth: '1200px', margin: '40px auto', padding: '0 20px', minHeight: '500px', position: 'relative' }}>
        <div style={{ width: '50%', paddingRight: '40px' }}>
          <h1 style={{ fontSize: '70px', fontWeight: 'bold', marginBottom: '20px' }}>Find your next home.</h1>
          <p style={{ fontSize: '18px' }}>Discover the perfect rental property with our selection of homes and apartments.</p>
        </div>
        
        <div style={{ width: '600px', height: '600px', borderRadius: '50%', overflow: 'hidden', position: 'absolute', right: '20px', top: '50px' }}>
          <img 
            src="https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=800&q=80" 
            alt="Apartment building"
            style={{ width: '135%', height: '158%', objectFit: 'cover' }}
          />
        </div>
      </div>

      {/* Features Section */}
      <div style={{ background: '#e7e4e4', padding: '95px 20px', marginTop: '100px' }}>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '60px', maxWidth: '1000px', margin: '0 auto' }}>
          {/* Feature 1 - Search */}
          <div style={{ background: 'white', padding: '44px', borderRadius: '8px', width: '100%', textAlign: 'center', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
            <div style={{ width: '35px', height: '20px', margin: '0 auto 20px' }}>
              <img src="https://cdn-icons-png.flaticon.com/512/149/149852.png" alt="Search" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
            </div>
            <h3 style={{ marginTop: 0 }}>Search</h3>
            <p>Browse thousands of verified properties</p>
          </div>
          
          {/* Feature 2 - Secure */}
          <div style={{ background: 'white', padding: '44px', borderRadius: '8px', width: '100%', textAlign: 'center', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
            <div style={{ width: '35px', height: '20px', margin: '0 auto 20px' }}>
              <img src="https://cdn-icons-png.flaticon.com/512/2889/2889676.png" alt="Secure" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
            </div>
            <h3 style={{ marginTop: 0 }}>Secure</h3>
            <p>Safe and reliable rental process</p>
          </div>
          
          {/* Feature 3 - Settle */}
          <div style={{ background: 'white', padding: '44px', borderRadius: '8px', width: '100%', textAlign: 'center', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
            <div style={{ width: '35px', height: '20px', margin: '0 auto 20px' }}>
              <img src="https://cdn-icons-png.flaticon.com/512/263/263115.png" alt="Settle" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
            </div>
            <h3 style={{ marginTop: 0 }}>Settle</h3>
            <p>Move into your perfect new home</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
