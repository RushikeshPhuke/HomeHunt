import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './LandingPage';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';
import RegisterProperty from './RegisterProperty';
import UserProperties from './UserProperties';
import OwnerProperties from './OwnerProperties';
import Broker from './Broker';
import Cart from './Cart';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
         <Route path="/registerproperty" element={<RegisterProperty />} />
         <Route path="/properties" element={<UserProperties />} />
         <Route path="/owner/properties" element={<OwnerProperties />} />
         <Route path="/broker/properties" element={<Broker />} />
         <Route path="/properties/cart" element={<Cart />} />
      </Routes>
    </Router>
  );
}

export default App;