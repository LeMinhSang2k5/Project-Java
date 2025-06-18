import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import HomePage from './components/Home/HomePage';
import NewHealthProfile from './components/HealthProfile/NewHealthProfile';
import VaccinationHistory from './components/HealthProfile/VaccinationHistory';
import BlogPage from './pages/BlogPage';
import Medical from './pages/Medical';
import Report from './pages/Report';
import Calendar from './pages/Calendar';
import AdminPage from './components/Admin/AdminPage';
import Loader from './components/Pagination/Loader';
import AdminLayout from './components/Admin/AdminLayout';
import './App.scss';
import AddUser from './components/users/AddUser';
import EditUser from './components/users/EditUser';
import ViewUser from './components/users/ViewUser';
import UserLayout from './components/users/UserLayout';
import Login from './pages/Login/Login';

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
      <Routes>
        {/* User Layout */}
        <Route element={<UserLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/blog" element={<BlogPage />} />
          <Route path="/health-profile/new" element={<NewHealthProfile />} />
          <Route path="/health-profile/vaccination" element={<VaccinationHistory />} />
          <Route path="/medical" element={<Medical />} />
          <Route path="/report" element={<Report />} />
          <Route path="/calendar" element={<Calendar />} />
        </Route>

        {/* Admin Layout */}
        <Route element={<AdminLayout />}>
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/adduser" element={<AddUser />} />
          <Route path="/edituser/:id" element={<EditUser />} />
          <Route path="/viewuser/:id" element={<ViewUser />} />
        </Route>

        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;