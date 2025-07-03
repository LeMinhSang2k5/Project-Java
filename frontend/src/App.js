import React, { useState, useEffect } from 'react';
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import Loader from './pages/Pagination/Loader';
import './App.scss';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import UserLayout from './components/User/UserLayout';
import AdminLayout from './components/Admin/AdminLayout';
import HomePage from './components/Home/HomePage';
import BlogPage from './pages/BlogPage';
import NewHealthProfile from './pages/HealthProfile/NewHealthProfile';
import VaccinationHistory from './pages/HealthProfile/VaccinationHistory';
import Medical from './pages/Medical';
import Report from './pages/Report';
import Calendar from './pages/Calendar';
import MedicalSupply from './pages/MedicationManagement/MedicalSupply';
import CreateBlog from './pages/CreateBlog';
import AdminDashboard from './components/Admin/AdminDashboard';
import ManageUser from './components/Admin/User/ManageUser';
import ManageBlog from './components/Admin/Blog/ManageBlog';
import Login from './pages/Login/Login';
import About from './pages/About';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import BlogDetail from './pages/BlogDetail';
import ModalUpdateBlog from './components/Admin/Blog/ModalUpdateBlog';
import ManageHealthProfile from './components/Admin/HealthProfile/ManageHealthProfile';
import ParentPage from './pages/Parent/ParentPage';
import NurseLayout from './components/Nurse/NurseLayout';
import NurseDashboard from './components/Nurse/NurseDashboard';
import ManageMedicalSupply from './components/Admin/MedicalSupply/ManageMedicalSupply';
import StudentPage from './pages/Student/StudentPage';
import ManageVaccination from './components/Nurse/Vaccination/ManageVaccination';
import MedicalCheckup from './components/Nurse/MedicalCheckup/MedicalCheckup';

function App() {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 1000);
    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return <Loader />;
  }

  return (
    <>
      <BrowserRouter>
        <Routes>
            {/* User Layout */}
            <Route element={<UserLayout />}>
                <Route path="/" element={<HomePage />} />
                <Route path="/about" element={<About />} />
                <Route path="/blog" element={<BlogPage />} />
                <Route path="/health-profile/new" element={<NewHealthProfile />} />
                <Route path="/new-health-profile/:studentId" element={<NewHealthProfile />} />
                <Route path="/health-profile/:studentId" element={<NewHealthProfile />} />
                <Route path="/health-profile/vaccination" element={<VaccinationHistory />} />
                <Route path="/medical" element={<Medical />} />
                <Route path="/report" element={<Report />} />
                <Route path="/calendar" element={<Calendar />} />
                <Route path="/medicalsupply" element={<MedicalSupply />} />
                <Route path="/createblog" element={<CreateBlog />} />
                <Route path="/blog/:id" element={<BlogDetail />} />
                <Route path="/parent" element={<ParentPage />} />
                <Route path="/student" element={<StudentPage />} />
            </Route>

            {/* Admin Layout */}
            <Route element={<AdminLayout />}>
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/manage-user" element={<ManageUser />} />
                <Route path="/manage-blog" element={<ManageBlog />} />
                <Route path="/manage-health-profile" element={<ManageHealthProfile />} />
                <Route path="/update-blog/:id" element={<ModalUpdateBlog />} />
                <Route path="/manage-medical-supply" element={<ManageMedicalSupply />} />
            </Route>

            {/* Nurse Layout */}
            <Route element={<NurseLayout />}>
                <Route path="/nurse" element={<NurseDashboard />} />
                <Route path="/nurse/medical-supplies" element={<ManageMedicalSupply />} />
                <Route path="/nurse/vaccination" element={<VaccinationHistory />} />
                <Route path="/nurse/manage-vaccination" element={<ManageVaccination />} />
                <Route path="/nurse/health-profiles" element={<ManageHealthProfile />} />
                <Route path="/nurse/health-check" element={<MedicalCheckup />} />
            </Route>

            <Route path="/login" element={<Login />} />
        </Routes>
      </BrowserRouter>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
    </>
  );
}

export default App;