import 'bootstrap/dist/css/bootstrap.min.css';
import NotificationBell from './NotificationBell';

function Broker() {
  const brokerId = 1;

  return (
    <>
      {/* Navbar */}
      <nav className="navbar navbar-light bg-light px-4 shadow-sm">
        <a className="navbar-brand fw-bold fs-4" href="/">HomeHunt</a>
        <div className="ms-auto d-flex align-items-center gap-3">
          <NotificationBell brokerId={brokerId} />
          <button className="btn btn-dark">Logout</button>
        </div>
      </nav>

      <div className="container my-5">
        {/* Top bar: heading + filters */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div className="text-muted fs-5">Properties Found</div>
          <div>
            <div className="btn-group" role="group">
              <button type="button" className="btn btn-outline-dark">Sale</button>
              <button type="button" className="btn btn-outline-dark">Rent</button>
            </div>
          </div>
        </div>

        {/* Properties Grid */}
        <div className="row g-4">
          {[...Array(6)].map((_, index) => (
            <div className="col-md-4" key={index}>
              <div className="card h-100 shadow-sm">
                <div className="position-relative">
                  <button type="button" className="btn btn-dark position-absolute top-0 start-0 m-2">
                    Broker
                  </button>
                  <img
                    src="https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=600&q=80"
                    className="card-img-top"
                    alt="Modern Apartment"
                    style={{ height: '160px', objectFit: 'cover' }}
                  />
                </div>
                <div className="card-body">
                  <h5 className="card-title fw-bold">$2,400/mo</h5>
                  <p className="card-text text-muted">123 Downtown St, City Center</p>
                  <p className="text-muted small mb-0">2 Beds • 1 Bath • 850 sqft</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default Broker;
