import React, { useState, useEffect } from 'react'; 
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Container, Form, Button, Row, Col, Card, Alert, Image } from 'react-bootstrap';

function RegisterProperty() {
  const navigate = useNavigate();

  // Redirect owner to login if not logged in (check ownerId & token)
  useEffect(() => {
    const ownerId = localStorage.getItem("ownerId");
    const token = localStorage.getItem("token");

    if (!ownerId || !token) {
      navigate('/login');
    }
  }, [navigate]);

  const [assignBroker, setAssignBroker] = useState('no');
  const [brokerId, setBrokerId] = useState('');
  const [brokers, setBrokers] = useState([]);

  useEffect(() => {
    if (assignBroker === 'yes') {
      fetch('http://localhost:8080/getAllBrokers')
        .then(res => res.json())
        .then(data => setBrokers(data))
        .catch(err => console.error('Error fetching brokers:', err));
    }
  }, [assignBroker]);

  const [propertyDetails, setPropertyDetails] = useState({
    title: '',
    description: '',
    price: '',
    propertyType: 'apartment',
    status: 'available',
    address: '',
    city: '',
    state: '',
    pincode: '',
    country: 'India',
    bedrooms: '',
    bathrooms: '',
    area: '',
    furnished: 'no',
    saleOrRent: 'Sale',
    photos: []
  });

  const [errorMessage, setErrorMessage] = useState('');
  const [imagePreviews, setImagePreviews] = useState([]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPropertyDetails(prev => ({ ...prev, [name]: value }));
  };

  const handlePhotoUpload = (e) => {
    const selectedFiles = Array.from(e.target.files);
    if (selectedFiles.length > 0) {
      setPropertyDetails(prev => ({
        ...prev,
        photos: [...prev.photos, ...selectedFiles]
      }));

      const newPreviews = selectedFiles.map(file => URL.createObjectURL(file));
      setImagePreviews(prev => [...prev, ...newPreviews]);
    }
  };

  const removePhoto = (index) => {
    const updatedPhotos = [...propertyDetails.photos];
    const updatedPreviews = [...imagePreviews];

    updatedPhotos.splice(index, 1);
    updatedPreviews.splice(index, 1);

    setPropertyDetails(prev => ({ ...prev, photos: updatedPhotos }));
    setImagePreviews(updatedPreviews);
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');

    if (propertyDetails.title.length < 5) {
      setErrorMessage('Please provide a more descriptive title (at least 5 characters)');
      return;
    }

    if (!propertyDetails.price || isNaN(propertyDetails.price)) {
      setErrorMessage('Please enter a valid price for the property');
      return;
    }

    // Check ownerId and token existence before submission
    const ownerId = localStorage.getItem("ownerId");
    const token = localStorage.getItem("token");

    if (!ownerId || !token) {
      setErrorMessage("Owner not logged in.");
      return;
    }

    try {
      const propertyPayload = {
        title: propertyDetails.title,
        description: propertyDetails.description,
        price: parseFloat(propertyDetails.price),
        propertyType: propertyDetails.propertyType,
        status: propertyDetails.status,
        address: propertyDetails.address,
        city: propertyDetails.city,
        location: `${propertyDetails.state}, ${propertyDetails.pincode}, ${propertyDetails.country}`,
        bedrooms: parseInt(propertyDetails.bedrooms),
        bathrooms: parseInt(propertyDetails.bathrooms),
        area: parseFloat(propertyDetails.area),
        furnished: propertyDetails.furnished !== 'no',
        saleOrRent: propertyDetails.saleOrRent,
        broker: assignBroker === 'yes' && brokerId ? { brokerId: parseInt(brokerId) } : null
      };

      const propertyRes = await fetch('http://localhost:8080/api/properties/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(propertyPayload)
      });

      if (!propertyRes.ok) {
        const errorText = await propertyRes.text();
        console.error("Register property failed:", errorText);
        throw new Error('Failed to register property');
      }

      const savedProperty = await propertyRes.json();
      const propertyId = savedProperty.propertyId;

      if (propertyDetails.photos.length > 0) {
        const formData = new FormData();
        propertyDetails.photos.forEach(photo => {
          formData.append('images', photo);
        });

        const imageRes = await axios.post(
          `http://localhost:8080/api/properties/${propertyId}/upload-images`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": `Bearer ${token}`
            }
          }
        );

        // axios does not have an "ok" property, check status instead
        if (imageRes.status !== 200 && imageRes.status !== 201) {
          throw new Error('Images upload failed');
        }
      }

      navigate('/owner/properties');
    } catch (error) {
      console.error(error);
      setErrorMessage(error.message || 'Something went wrong');
    }
  };

  return (
    <>
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
      <Container className="py-5">
        <Row className="justify-content-center">
          <Col md={8} lg={6}>
            <Card className="shadow-sm">
              <Card.Body>
                <h2 className="text-center mb-4">List Your Property</h2>

                {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}

                <Form onSubmit={handleFormSubmit}>
                  <h5 className="mb-3">Basic Information</h5>
                  <Form.Group className="mb-3">
                    <Form.Label>Property Title*</Form.Label>
                    <Form.Control
                      type="text"
                      name="title"
                      value={propertyDetails.title}
                      onChange={handleInputChange}
                      required
                    />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Description</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={4}
                      name="description"
                      value={propertyDetails.description}
                      onChange={handleInputChange}
                    />
                  </Form.Group>

                  <Row className="mb-3">
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Asking Price (₹)*</Form.Label>
                        <Form.Control
                          type="number"
                          name="price"
                          value={propertyDetails.price}
                          onChange={handleInputChange}
                          required
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Property Type</Form.Label>
                        <Form.Select
                          name="propertyType"
                          value={propertyDetails.propertyType}
                          onChange={handleInputChange}
                        >
                          <option value="apartment">Apartment</option>
                          <option value="house">House</option>
                          <option value="villa">Villa</option>
                          <option value="land">Land</option>
                          <option value="commercial">Commercial Space</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                  </Row>

                  <h5 className="mb-3 mt-4">Location Details</h5>
                  <Form.Group className="mb-3">
                    <Form.Label>Full Address</Form.Label>
                    <Form.Control
                      type="text"
                      name="address"
                      value={propertyDetails.address}
                      onChange={handleInputChange}
                    />
                  </Form.Group>

                  <Row className="mb-3">
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>City</Form.Label>
                        <Form.Control
                          type="text"
                          name="city"
                          value={propertyDetails.city}
                          onChange={handleInputChange}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>State</Form.Label>
                        <Form.Control
                          type="text"
                          name="state"
                          value={propertyDetails.state}
                          onChange={handleInputChange}
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row className="mb-3">
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>PIN Code</Form.Label>
                        <Form.Control
                          type="text"
                          name="pincode"
                          value={propertyDetails.pincode}
                          onChange={handleInputChange}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Country</Form.Label>
                        <Form.Control
                          type="text"
                          name="country"
                          value={propertyDetails.country}
                          onChange={handleInputChange}
                          readOnly
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <h5 className="mb-3 mt-4">Property Specifications</h5>
                  <Row className="mb-3">
                    <Col md={4}>
                      <Form.Group>
                        <Form.Label>Bedrooms</Form.Label>
                        <Form.Control
                          type="number"
                          name="bedrooms"
                          value={propertyDetails.bedrooms}
                          onChange={handleInputChange}
                          min="0"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group>
                        <Form.Label>Bathrooms</Form.Label>
                        <Form.Control
                          type="number"
                          name="bathrooms"
                          value={propertyDetails.bathrooms}
                          onChange={handleInputChange}
                          min="0"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group>
                        <Form.Label>Area (sq ft)</Form.Label>
                        <Form.Control
                          type="number"
                          name="area"
                          value={propertyDetails.area}
                          onChange={handleInputChange}
                          min="0"
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-4">
                    <Form.Label>Furnishing</Form.Label>
                    <Form.Select
                      name="furnished"
                      value={propertyDetails.furnished}
                      onChange={handleInputChange}
                    >
                      <option value="no">Unfurnished</option>
                      <option value="semi">Semi-Furnished</option>
                      <option value="full">Fully Furnished</option>
                    </Form.Select>
                  </Form.Group>

                  <Form.Group className="mb-3 mt-4">
                    <Form.Label>Do you want to assign a broker?</Form.Label>
                    <Form.Select
                      value={assignBroker}
                      onChange={(e) => setAssignBroker(e.target.value)}
                    >
                      <option value="no">No</option>
                      <option value="yes">Yes</option>
                    </Form.Select>
                  </Form.Group>

                  {assignBroker === 'yes' && (
                    <Form.Group className="mb-3">
                      <Form.Label>Select Broker</Form.Label>
                      <Form.Select
                        value={brokerId}
                        onChange={(e) => setBrokerId(e.target.value)}
                        required
                      >
                        <option value="">-- Choose a Broker --</option>
                        {brokers.map(broker => (
                          <option key={broker.brokerId} value={broker.brokerId}>
                            {broker.name} - {broker.email}
                          </option>
                        ))}
                      </Form.Select>
                    </Form.Group>
                  )}

                  <Form.Group className="mb-3">
                    <Form.Label>Sale or Rent</Form.Label>
                    <Form.Select
                      name="saleOrRent"
                      value={propertyDetails.saleOrRent}
                      onChange={handleInputChange}
                    >
                      <option value="Sale">Sale</option>
                      <option value="Rent">Rent</option>
                    </Form.Select>
                  </Form.Group>

                  <h5 className="mb-3 mt-4">Property Photos</h5>
                  <Form.Group className="mb-4">
                    <Card className="border-dashed p-4 text-center bg-light">
                      <input
                        type="file"
                        id="property-photos"
                        multiple
                        onChange={handlePhotoUpload}
                        style={{ display: 'none' }}
                        accept="image/*"
                      />
                      <Button
                        as="label"
                        htmlFor="property-photos"
                        variant="outline-primary"
                        className="mb-2"
                      >
                        <i className="bi bi-camera me-2"></i>Upload Photos
                      </Button>
                      <p className="text-muted mb-0">
                        Upload photos (max 10, JPEG/PNG)
                      </p>
                    </Card>

                    {imagePreviews.length > 0 && (
                      <div className="mt-3">
                        <h6 className="mb-3">Selected Photos ({imagePreviews.length})</h6>
                        <div className="d-flex flex-wrap gap-3">
                          {imagePreviews.map((preview, index) => (
                            <div key={index} className="position-relative" style={{ width: '120px' }}>
                              <Image
                                src={preview}
                                alt={`Property photo ${index + 1}`}
                                thumbnail
                                className="img-thumbnail"
                                style={{ width: '100%', height: '120px', objectFit: 'cover' }}
                              />
                              <Button
                                variant="danger"
                                size="sm"
                                className="position-absolute top-0 end-0 rounded-circle p-0"
                                style={{ width: '25px', height: '25px' }}
                                onClick={() => removePhoto(index)}
                                aria-label="Remove photo"
                              >
                                ×
                              </Button>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}
                  </Form.Group>

                  <div className="d-grid gap-3 mt-4">
                    <Button variant="primary" type="submit" size="lg">
                      <i className="bi bi-house-check me-2"></i>Register Property
                    </Button>
                    <Button 
                      variant="outline-secondary" 
                      size="lg"
                      onClick={() => navigate('/properties')}
                    >
                      <i className="bi bi-x-circle me-2"></i>Cancel
                    </Button>
                  </div>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
}

export default RegisterProperty;
