import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useEffect, useState } from 'react';
import axios from 'axios';

function Broker() {
  const [brokerId, setBrokerId] = useState(null);
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [showNotifications, setShowNotifications] = useState(false);

  // Get brokerId from localStorage
  useEffect(() => {
    const id = localStorage.getItem('brokerId');
    if (id) setBrokerId(parseInt(id, 10));
    else setError("Broker ID not found. Please log in.");
  }, []);

  // Fetch properties
  useEffect(() => {
    if (!brokerId) return;

    const fetchProperties = async () => {
      setLoading(true);
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(`http://localhost:8080/api/properties/broker/${brokerId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        const propsWithImages = res.data.map((p) => {
          let imageUrl = "https://via.placeholder.com/400x200?text=No+Image";
          if (p.images && p.images.length > 0) {
            const img = p.images[0];
            if (img.imageData && img.imageType) {
              imageUrl = `data:${img.imageType};base64,${img.imageData}`;
            }
          }
          return { ...p, image: imageUrl };
        });

        setProperties(propsWithImages);
      } catch (err) {
        console.error(err);
        setError("Failed to load properties.");
      } finally {
        setLoading(false);
      }
    };

    fetchProperties();
  }, [brokerId]);

  // Fetch notifications every 5 sec
  useEffect(() => {
    if (!brokerId) return;

    const fetchNotifications = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(`http://localhost:8080/notifications/${brokerId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        console.log("Notifications:", res.data); // Debug
        setNotifications(res.data);
        setUnreadCount(res.data.filter(n => !n.readStatus).length);
      } catch (err) {
        console.error("Failed to fetch notifications", err);
      }
    };

    fetchNotifications();
    const interval = setInterval(fetchNotifications, 5000);
    return () => clearInterval(interval);
  }, [brokerId]);

  const markAllAsRead = async () => {
    try {
      const token = localStorage.getItem('token');
      await Promise.all(
        notifications.filter(n => !n.readStatus).map(n =>
          axios.put(`http://localhost:8080/notifications/${n.id}/read`, {}, {
            headers: { Authorization: `Bearer ${token}` },
          })
        )
      );

      setUnreadCount(0);
      const res = await axios.get(`http://localhost:8080/notifications/${brokerId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setNotifications(res.data);
    } catch (err) {
      console.error("Failed to mark notifications as read", err);
    }
  };

  return (
    <>
      {/* Navbar */}
      <nav className="navbar navbar-light bg-light px-4 shadow-sm">
        <a className="navbar-brand fw-bold fs-4" href="/">HomeHunt</a>
        <div className="ms-auto d-flex align-items-center gap-3" style={{ position: "relative" }}>
          
          {/* Notification Bell */}
          <div
            style={{ position: "relative", cursor: "pointer" }}
            onClick={() => {
              setShowNotifications(!showNotifications);
              markAllAsRead();
            }}
          >
            <i className="bi bi-bell" style={{ fontSize: "1.5rem" }}></i>
            {unreadCount > 0 && (
              <span style={{
                position: "absolute",
                top: "-5px",
                right: "-5px",
                background: "red",
                color: "white",
                borderRadius: "50%",
                padding: "2px 6px",
                fontSize: "0.8rem"
              }}>
                {unreadCount}
              </span>
            )}
          </div>

          {/* Notification Dropdown */}
          {showNotifications && (
            <div style={{
              position: "absolute",
              top: "40px",
              right: "0px",
              width: "320px",
              maxHeight: "350px",
              overflowY: "auto",
              background: "#fff",
              border: "1px solid #ddd",
              borderRadius: "8px",
              zIndex: 9999,
              padding: "10px"
            }}>
              <h6 className="fw-bold mb-2">Notifications</h6>
              {notifications.length > 0 ? notifications.map((n, idx) => (
                <div key={idx} className="border-bottom py-2">
                  <strong>{n.userName}</strong>  
                  <br />
                  <small>{n.phone}</small>
                  <br />
                  <small>{n.message}</small>
                </div>
              )) : (
                <p className="text-muted mb-0">No notifications</p>
              )}
            </div>
          )}

          {/* Logout */}
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

      {/* Properties */}
      <div className="container my-5">
        <h1 className="fw-bold">My Properties</h1>
        {loading && <p>Loading properties...</p>}
        {error && <p className="text-danger">{error}</p>}

        <div className="row g-4">
          {properties.length > 0 ? properties.map((property, index) => (
            <div className="col-md-4" key={index}>
              <div className="card h-100 shadow-sm">
                <img
                  src={property.image}
                  className="card-img-top"
                  alt="Property"
                  style={{ height: '200px', objectFit: 'cover' }}
                />
                <div className="card-body">
                  <h5 className="fw-bold mb-2">
                    {property.price ? `$${property.price}` : "Price not available"}
                  </h5>
                  <p className="text-muted">{property.address || "No address provided"}</p>
                </div>
              </div>
            </div>
          )) : (!loading && <p>No properties found.</p>)}
        </div>
      </div>
    </>
  );
}

export default Broker;
