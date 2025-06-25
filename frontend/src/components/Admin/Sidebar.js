import React from 'react';
import { Link } from 'react-router-dom';
import { FaUsers, FaBlog, FaMedkit, FaChartBar, FaCalendarCheck } from 'react-icons/fa';
import './Admin.scss';

const Sidebar = () => {
    return (
        <div className="admin-sidebar">
            <div className="sidebar-header">
                <h3>Admin Dashboard</h3>
            </div>
            <div className="sidebar-menu">
                <Link to="/admin" className="menu-item">
                    <FaChartBar className="menu-icon" />
                    <span>Dashboard</span>
                </Link>
                <Link to="/manage-user" className="menu-item">
                    <FaUsers className="menu-icon" />
                    <span>Quản lý người dùng</span>
                </Link>
                <Link to="/manage-blog" className="menu-item">
                    <FaBlog className="menu-icon" />
                    <span>Quản lý Blog</span>
                </Link>
                <Link to="/manage-medical-supply" className="menu-item">
                    <FaMedkit className="menu-icon" />
                    <span>Quản lý vật tư y tế</span>
                </Link>
                <Link to="/manage-health-profile" className="menu-item">
                    <FaCalendarCheck className="menu-icon" />
                    <span>Quản lý hồ sơ sức khỏe</span>
                </Link>
            </div>
        </div>
    );
};

export default Sidebar; 