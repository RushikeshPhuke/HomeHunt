import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const validateField = (name, value) => {
    let message = '';
    switch (name) {
      case 'email':
        if (!value.trim()) message = 'Email is required';
        else if (!/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value))
          message = 'Invalid Email Format';
        break;
      case 'password':
        if (!value.trim()) message = 'Password is required';
        else if (value.length < 8) message = 'Password must be at least 8 characters';
        break;
      default:
        break;
    }
    return message;
  };

  const handleChange = (name, value) => {
    if (name === 'email') setEmail(value);
    if (name === 'password') setPassword(value);
    setFieldErrors((prev) => ({
      ...prev,
      [name]: validateField(name, value)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validate inputs
    const emailError = validateField('email', email);
    const passwordError = validateField('password', password);
    const newErrors = {};
    if (emailError) newErrors.email = emailError;
    if (passwordError) newErrors.password = passwordError;

    if (Object.keys(newErrors).length > 0) {
      setFieldErrors(newErrors);
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      if (!response.ok) {
        const text = await response.text();
        setError(text || 'Login failed');
        return;
      }

      const data = await response.json();
      console.log('Received login response:', data);

      if (!data.token) {
        setError('No token received from server.');
        return;
      }

      // Store token and role exactly as received
      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.role);
      localStorage.setItem('email', email);

      // Role-based redirects & ID storage
      if (data.role === 'OWNER') {
        localStorage.setItem('ownerId', data.userId);
        navigate('/owner/properties');
      } else if (data.role === 'BROKER') {
        localStorage.setItem('brokerId', data.userId);
        navigate('/broker/properties');
      } else if (data.role === 'USER') {
        localStorage.setItem('userId', data.userId);
        navigate('/properties');
      } else if (data.role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/');
      }

    } catch (err) {
      console.error('Login error:', err);
      setError('Something went wrong. Please try again.');
    }
  };

  return (
    <>
      <nav className="navbar navbar-light bg-light px-4 shadow-sm">
        <a className="navbar-brand fw-bold fs-4" href="/">HomeHunt</a>
      </nav>

      <div style={{
        display: 'flex', justifyContent: 'center', alignItems: 'center',
        height: 'calc(100vh - 70px)', backgroundColor: '#e4e6e6ff'
      }}>
        <form onSubmit={handleSubmit} style={{
          width: '400px', padding: '20px', backgroundColor: 'white', borderRadius: '5px'
        }}>
          <h2 style={{ textAlign: 'center', marginBottom: '30px' }}>Login</h2>

          <div style={{ marginBottom: '15px' }}>
            <label>Email</label>
            <input
              type="email"
              style={{
                width: '100%', padding: '8px',
                border: fieldErrors.email ? '1px solid red' : email ? '1px solid green' : '1px solid #ddd',
                borderRadius: '4px'
              }}
              value={email}
              onChange={(e) => handleChange('email', e.target.value)}
            />
            {fieldErrors.email && <span style={{ color: 'red', fontSize: '12px' }}>{fieldErrors.email}</span>}
          </div>

          <div style={{ marginBottom: '15px' }}>
            <label>Password</label>
            <input
              type="password"
              style={{
                width: '100%', padding: '8px',
                border: fieldErrors.password ? '1px solid red' : password ? '1px solid green' : '1px solid #ddd',
                borderRadius: '4px'
              }}
              value={password}
              onChange={(e) => handleChange('password', e.target.value)}
            />
            {fieldErrors.password && <span style={{ color: 'red', fontSize: '12px' }}>{fieldErrors.password}</span>}
          </div>

          {error && <div style={{ color: 'red', marginBottom: '15px', fontSize: '14px' }}>{error}</div>}

          <div style={{ textAlign: 'center', marginBottom: '15px' }}>
            <button
              type="submit"
              style={{
                padding: '10px 20px', backgroundColor: '#59a4cfff', color: 'white',
                border: 'none', borderRadius: '4px', cursor: 'pointer', width: '100%'
              }}
            >
              Login
            </button>
          </div>

          <div style={{ textAlign: 'center' }}>
            <p>Don't have an account? <Link to="/register" style={{ color: '#59a4cfff' }}>Register</Link></p>
          </div>
        </form>
      </div>
    </>
  );
}

export default LoginPage;
