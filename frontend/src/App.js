import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './components/Home/HomePage';
import NewHealthProfile from './components/HealthProfile/NewHealthProfile';
import VaccinationHistory from './components/HealthProfile/VaccinationHistory';
import Header from './components/Header/Header';
import Footer from './components/Header/Footer';
import Loader from './components/Pagination/Loader';
import BlogPage from './pages/BlogPage';
import Medical from './pages/Medical';
import './App.scss';
import Report from './pages/Report';
import Calendar from './pages/Calendar';

function App() {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 3000);
    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return <Loader />;
  }

  return (
    <Router>
      <div className="app-container">
        <div className="header-container">
          <Header />
        </div>

        <div className="main-container">
          <div className="app-content">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/blog" element={<BlogPage />} />
              <Route path="/health-profile/new" element={<NewHealthProfile />} />
              <Route path="/health-profile/vaccination" element={<VaccinationHistory />} />
              <Route path="/medical" element={<Medical />} />
              <Route path="/report" element={<Report />} />
              <Route path="/calendar" element={<Calendar />} />
            </Routes>
          </div>
        </div>

        <div className="footer-container">
          <Footer />
        </div>
      </div>
    </Router>
  );
}

export default App;
