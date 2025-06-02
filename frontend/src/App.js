import './App.scss';
import Header from './components/Header/Header';
import Footer from './components/Header/Footer';
import { Outlet, useNavigation } from 'react-router-dom';
import Loader from './components/Pagination/Loader';
import { useState, useEffect } from 'react';

function App() {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 3000);
    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return <Loader/>;
  }
  return (
    <>
      <div className="app-container">
        <div className="header-container">
          <Header />
        </div>

        <div className="main-container">
          <div className="app-content">
            <Outlet />
          </div>
        </div>

        <div className="footer-container">
          <Footer />
        </div>
      </div>
    </>
  );
}

export default App;
