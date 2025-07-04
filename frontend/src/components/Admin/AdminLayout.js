import React, { useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import { FaBell, FaUser, FaSearch, FaBars } from 'react-icons/fa';
import Sidebar from './Sidebar';
import ErrorBoundary from './ErrorBoundary';
import './Admin.scss';

const AdminLayout = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);
    const location = useLocation();

    const getUserRole = () => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.role) {
            return user.role;
        }
    };
    const role = getUserRole();

    if (role !== 'ADMIN') {
        return (
            <div className="unauthorized">
                <h1>Unauthorized Access</h1>
                <p>You do not have permission to access this page.</p>
            </div>
        );
    }

    const getPageTitle = () => {
        const path = location.pathname;
        if (path === '/admin') return 'Dashboard';
        if (path.includes('manage-user')) return 'Quản lý người dùng';
        if (path.includes('manage-blog')) return 'Quản lý Blog';
        if (path.includes('manage-medical-supply')) return 'Quản lý vật tư y tế';
        if (path.includes('manage-health-profile')) return 'Quản lý hồ sơ sức khỏe';
        return 'Admin Panel';
    };



    return (
        <div className="admin-layout">
            <Sidebar  />
            <div className="admin-main">
                <header className="admin-header">
                    <div className="header-left" >
                        <button 
                            className="sidebar-toggle"
                            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                        >
                            <FaBars />
                        </button>
                        <div className="page-info">
                            <h1 style={{ display: 'flex' }}>{getPageTitle()}</h1>
                        </div>
                    </div>
                    
                    <div className="header-right">
                        <div className="search-box">
                            <FaSearch className="search-icon" />
                            <input 
                                type="text" 
                                placeholder="Tìm kiếm..." 
                                className="search-input"
                            />
                        </div>
                        
                        <div className="header-actions">
                            
                            <div className="user-menu">
                                <button className="user-btn">
                                    <div className="user-avatar">
                                        <FaUser />
                                    </div>
                                    <span className="user-name">Admin</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </header>

                <main className="admin-content">
                    {/* <nav className="breadcrumb">
                        <span>Admin</span>
                        <span>/</span>
                        <span>{getPageTitle()}</span>
                    </nav> */}
                    <ErrorBoundary>
                        <Outlet />
                    </ErrorBoundary>
                </main>
            </div>
        </div>
    );
};

export default AdminLayout;
