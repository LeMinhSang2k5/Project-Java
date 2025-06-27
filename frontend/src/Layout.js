import {
    Routes,
    Route,
    BrowserRouter
} from 'react-router-dom';

import HomePage from "./components/Home/HomePage";
import NewHealthProfile from './pages/HealthProfile/NewHealthProfile';
import BlogPage from './pages/BlogPage';
import Medical from './pages/Medical';
import Report from './pages/Report';
import Calendar from './pages/Calendar';
import MedicalSupply from './pages/MedicationManagement/MedicalSupply';
import CreateBlog from './pages/CreateBlog';
import UserLayout from './components/User/UserLayout';
import AdminLayout from './components/Admin/AdminLayout';
import ManageUser from './components/Admin/User/ManageUser';
import ManageBlog from './components/Admin/Blog/ManageBlog';
import Login from './pages/Login/Login';
import VaccinationHistory from './pages/HealthProfile/VaccinationHistory';
import AdminDashboard from './components/Admin/AdminDashboard';
import ManageHealthProfile from './components/Admin/HealthProfile/ManageHealthProfile';
import ParentPage from './pages/Parent/ParentPage';

const Layout = (props) => {
    return (
        <BrowserRouter>
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
                    <Route path="/medicalsupply" element={<MedicalSupply />} />
                    <Route path="/createblog" element={<CreateBlog />} />
                    <Route path="/parent" element={<ParentPage />} />
                </Route>

                {/* Admin Layout */}
                <Route element={<AdminLayout />}>
                    <Route path="/admin" element={<AdminDashboard />} />
                    <Route path="/manage-user" element={<ManageUser />} />
                    <Route path="/manage-blog" element={<ManageBlog />} />
                    <Route path="/manage-health-profile" element={<ManageHealthProfile />} />
                </Route>

                {/* Nurse Layout */}
                <Route element={<NurseLayout />}>
                    <Route path="/nurse" element={<NurseDashboard />} />
                </Route>

                <Route path="/login" element={<Login />} />
            </Routes>
        </BrowserRouter>
    )
}

export default Layout;