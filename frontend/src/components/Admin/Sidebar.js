import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  FaUsers, 
  FaBlog, 
  FaMedkit, 
  FaChartBar, 
  FaCalendarCheck,
  FaSignOutAlt
} from 'react-icons/fa';
import './Admin.scss';

const Sidebar = ({ role = 'ADMIN' }) => {
    const location = useLocation();
    const isManager = role === 'MANAGER';

    const allMenuItems = [
        { path: '/admin', icon: FaChartBar, label: 'Dashboard', exact: true, roles: ['ADMIN'] },
        { path: '/manage-user', icon: FaUsers, label: 'Quản lý người dùng', roles: ['ADMIN', 'MANAGER'] },
        { path: '/manage-blog', icon: FaBlog, label: 'Quản lý Blog', roles: ['ADMIN'] },
        { path: '/manage-medical-supply', icon: FaMedkit, label: 'Quản lý vật tư y tế', roles: ['ADMIN'] },
        { path: '/manage-health-profile', icon: FaCalendarCheck, label: 'Quản lý hồ sơ sức khỏe', roles: ['ADMIN'] }
    ];

    const menuItems = allMenuItems.filter((item) => item.roles.includes(role));

    const isActive = (path, exact = false) => {
        if (exact) {
            return location.pathname === path;
        }
        return location.pathname.startsWith(path);
    };

    return (
        <div className="admin-sidebar">
            <div className="sidebar-header">
                <div className="logo-section">
                    <div className="logo-icon">
                        <FaChartBar />
                    </div>
                    <div className="logo-text">
                        <h3>{isManager ? 'Manager Panel' : 'Admin Panel'}</h3>
                        <span>Hệ thống y tế</span>
                    </div>
                </div>
            </div>

            <div className="sidebar-menu">
                {menuItems.map((item, index) => (
                    <Link 
                        key={index}
                        to={item.path} 
                        className={`menu-item ${isActive(item.path, item.exact) ? 'active' : ''}`}
                        title={item.label}
                    >
                        <item.icon className="menu-icon" />
                        <span>{item.label}</span>
                    </Link>
                ))}
            </div>

            <div className="sidebar-footer">
                <Link to="/login" className="menu-item logout-btn" title="Đăng xuất">
                    <FaSignOutAlt className="menu-icon"/>
                    <span>Đăng xuất</span>
                </Link>
            </div>
        </div>
    );
};

export default Sidebar;
