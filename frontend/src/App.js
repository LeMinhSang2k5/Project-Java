import './App.scss';
import Header from './components/Header/Header';
import Footer from './components/Header/Footer';
import { Outlet } from 'react-router-dom';

function App() {
  return (
    <div className="app-container">
      <div className="header-container">
        <Header />
      </div>

      <div className="main-container">
        <div className="app-content">
        <Outlet/>
        </div>
      </div>
      <div className="footer-container">
        <Footer />
      </div>
    </div>
  );
}

export default App;
