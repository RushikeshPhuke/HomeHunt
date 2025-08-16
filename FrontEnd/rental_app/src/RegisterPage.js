import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    phone: '',
    userType: 'user',
    brokerId: '',
    experienceYears: ''
  });

  const [brokers, setBrokers] = useState([]);
  const [hasBroker, setHasBroker] = useState(false);
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/getAllBrokers')
      .then((res) => res.json())
      .then((data) => setBrokers(data))
      .catch((err) => console.error('Failed to fetch brokers:', err));
  }, []);

  // Validation rules
  const validateField = (name, value) => {
    let message = '';

    switch (name) {
      case 'name':
        if (!value.trim()) message = 'Full Name is required';
        else if (!/^[a-zA-Z\s]+$/.test(value))
          message = 'Name can only contain letters and spaces';
        break;

      case 'email':
        if (!value.trim()) message = 'Email is required';
        else if (!/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value))
          message = 'Invalid email format';
        break;

      case 'password':
        if (!value.trim()) message = 'Password is required';
        else if (value.length < 8)
          message = 'Password must be at least 6 characters';
        else if (!/[A-Z]/.test(value))
          message = 'Password must contain at least 1 uppercase letter';
        else if (!/[0-9]/.test(value))
          message = 'Password must contain at least 1 number';
        break;

      case 'phone':
        if (!value.trim()) message = 'Phone number is required';
        else if (!/^\d{10}$/.test(value))
          message = 'Phone number must be 10 digits';
        break;

      case 'experienceYears':
        if (formData.userType === 'broker') {
          if (value === '' || value < 0)
            message = 'Experience must be a positive number';
        }
        break;

      case 'brokerId':
        if (hasBroker && !value)
          message = 'Please select a broker';
        break;

      default:
        break;
    }

    return message;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({ ...prev, [name]: value }));

    setFieldErrors((prev) => ({
      ...prev,
      [name]: validateField(name, value)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const newErrors = {};
    Object.keys(formData).forEach((key) => {
      const message = validateField(key, formData[key]);
      if (message) newErrors[key] = message;
    });

    if (Object.keys(newErrors).length > 0) {
      setFieldErrors(newErrors);
      return;
    }

    setIsSubmitting(true);

    try {
      let endpoint = '';
      let payload = {};
      const today = new Date().toISOString().split('T')[0];
      const parsedBrokerId = formData.brokerId
        ? parseInt(formData.brokerId, 10)
        : null;

      switch (formData.userType) {
        case 'user':
          endpoint = 'http://localhost:8080/addUser';
          payload = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
            phone: formData.phone,
            broker: parsedBrokerId ? { brokerId: parsedBrokerId } : null
          };
          break;

        case 'owner':
          endpoint = 'http://localhost:8080/addOwner';
          payload = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
            phone: formData.phone,
            registrationDate: today,
            brokerId: parsedBrokerId
          };
          break;

        case 'broker':
          endpoint = 'http://localhost:8080/addBroker';
          payload = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
            phone: formData.phone,
            experienceYears: parseInt(formData.experienceYears),
            registeredDate: today
          };
          break;

        default:
          setError('Invalid user type');
          setIsSubmitting(false);
          return;
      }

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Registration failed');
      }

      navigate('/login');
    } catch (err) {
      console.error('Registration error:', err);
      setError(err.message || 'Failed to register. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const selectedBroker = brokers.find(
    (b) => b.brokerId === parseInt(formData.brokerId)
  );

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Register</h2>

      {error && <div style={styles.error}>{error}</div>}

      <form onSubmit={handleSubmit} style={styles.form}>
        {/* Full Name */}
        <div style={styles.formGroup}>
          <label>Full Name:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            style={{
              ...styles.input,
              borderColor: fieldErrors.name ? 'red' : '#ddd'
            }}
          />
          {fieldErrors.name && <span style={styles.fieldError}>{fieldErrors.name}</span>}
        </div>

        {/* Email */}
        <div style={styles.formGroup}>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            style={{
              ...styles.input,
              borderColor: fieldErrors.email ? 'red' : '#ddd'
            }}
          />
          {fieldErrors.email && <span style={styles.fieldError}>{fieldErrors.email}</span>}
        </div>

        {/* Password */}
        <div style={styles.formGroup}>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            style={{
              ...styles.input,
              borderColor: fieldErrors.password ? 'red' : '#ddd'
            }}
          />
          {fieldErrors.password && <span style={styles.fieldError}>{fieldErrors.password}</span>}
        </div>

        {/* Phone */}
        <div style={styles.formGroup}>
          <label>Phone:</label>
          <input
            type="tel"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            style={{
              ...styles.input,
              borderColor: fieldErrors.phone ? 'red' : '#ddd'
            }}
          />
          {fieldErrors.phone && <span style={styles.fieldError}>{fieldErrors.phone}</span>}
        </div>

        {/* User Type */}
        <div style={styles.formGroup}>
          <label>Register as:</label>
          <select
            name="userType"
            value={formData.userType}
            onChange={(e) => {
              handleChange(e);
              setHasBroker(false);
              setFormData((prev) => ({ ...prev, brokerId: '' }));
            }}
            style={styles.input}
          >
            <option value="user">User</option>
            <option value="owner">Property Owner</option>
            <option value="broker">Broker</option>
          </select>
        </div>

        {/* Experience Years */}
        {formData.userType === 'broker' && (
          <div style={styles.formGroup}>
            <label>Experience (in years):</label>
            <input
              type="number"
              name="experienceYears"
              value={formData.experienceYears}
              onChange={handleChange}
              style={{
                ...styles.input,
                borderColor: fieldErrors.experienceYears ? 'red' : '#ddd'
              }}
            />
            {fieldErrors.experienceYears && <span style={styles.fieldError}>{fieldErrors.experienceYears}</span>}
          </div>
        )}

        {/* Broker selection for user/owner */}
        {(formData.userType === 'user' || formData.userType === 'owner') && (
          <>
            <div style={styles.formGroup}>
              <label>Do you have a broker?</label>
              <select
                value={hasBroker ? 'yes' : 'no'}
                onChange={(e) => {
                  const has = e.target.value === 'yes';
                  setHasBroker(has);
                  if (!has) {
                    setFormData((prev) => ({ ...prev, brokerId: '' }));
                  }
                }}
                style={styles.input}
              >
                <option value="no">No</option>
                <option value="yes">Yes</option>
              </select>
            </div>

            {hasBroker && (
              <div style={styles.formGroup}>
                <label>Select a Broker:</label>
                <select
                  name="brokerId"
                  value={formData.brokerId}
                  onChange={handleChange}
                  style={{
                    ...styles.input,
                    borderColor: fieldErrors.brokerId ? 'red' : '#ddd'
                  }}
                >
                  <option value="">-- Select Broker --</option>
                  {brokers.map((broker) => (
                    <option key={broker.brokerId} value={broker.brokerId}>
                      {broker.name} (ID: {broker.brokerId})
                    </option>
                  ))}
                </select>
                {fieldErrors.brokerId && <span style={styles.fieldError}>{fieldErrors.brokerId}</span>}
              </div>
            )}

            {selectedBroker && (
              <div style={styles.brokerDetails}>
                <strong>Broker Details:</strong>
                <p>ID: {selectedBroker.brokerId}</p>
                <p>Name: {selectedBroker.name}</p>
                <p>Email: {selectedBroker.email}</p>
                <p>Phone: {selectedBroker.phone}</p>
                <p>Experience: {selectedBroker.experienceYears} years</p>
              </div>
            )}
          </>
        )}

        <button
          type="submit"
          disabled={isSubmitting}
          style={isSubmitting ? styles.buttonDisabled : styles.button}
        >
          {isSubmitting ? 'Registering...' : 'Register'}
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: { maxWidth: '500px', margin: '50px auto', padding: '20px', border: '1px solid #ddd', borderRadius: '5px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' },
  title: { textAlign: 'center', marginBottom: '20px' },
  error: { color: 'red', padding: '10px', marginBottom: '15px', border: '1px solid red', borderRadius: '4px', backgroundColor: '#ffeeee' },
  form: { display: 'flex', flexDirection: 'column', gap: '15px' },
  formGroup: { display: 'flex', flexDirection: 'column', gap: '5px' },
  input: { padding: '8px', borderRadius: '4px', border: '1px solid #ddd', outline: 'none' },
  button: { padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
  buttonDisabled: { padding: '10px', backgroundColor: '#cccccc', color: 'white', border: 'none', borderRadius: '4px', cursor: 'not-allowed' },
  brokerDetails: { backgroundColor: '#f5f5f5', padding: '10px', border: '1px solid #ccc', borderRadius: '5px' },
  fieldError: { color: 'red', fontSize: '12px' }
};

export default RegisterPage;
