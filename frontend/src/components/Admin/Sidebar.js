import React, { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { 
  FaUsers, 
  FaBlog, 
  FaMedkit, 
  FaChartBar, 
  FaCalendarCheck,
  FaUserGraduate,
  FaUserTie,
  FaUserNurse,
  FaCog,
  FaSignOutAlt,
  FaBars,
  FaTimes
} from 'react-icons/fa';
import './Admin.scss';

const Sidebar = () => {
    const [isCollapsed, setIsCollapsed] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();

    const menuItems = [
        { path: '/admin', icon: FaChartBar, label: 'Dashboard', exact: true },
        { path: '/manage-user', icon: FaUsers, label: 'Quản lý người dùng' },
        { path: '/manage-blog', icon: FaBlog, label: 'Quản lý Blog' },
        { path: '/manage-medical-supply', icon: FaMedkit, label: 'Quản lý vật tư y tế' },
        { path: '/manage-health-profile', icon: FaCalendarCheck, label: 'Quản lý hồ sơ sức khỏe' }
    ];

    const isActive = (path, exact = false) => {
        if (exact) {
            return location.pathname === path;
        }
        return location.pathname.startsWith(path);
    };


    return (
        <div className={`admin-sidebar ${isCollapsed ? 'collapsed' : ''}`}>
            <div className="sidebar-header">
                <div className="logo-section">
                    <div className="logo-icon">
                        <FaChartBar />
                    </div>
                    {!isCollapsed && (
                        <div className="logo-text">
                            <h3>Admin Panel</h3>
                            <span>Hệ thống y tế</span>
                        </div>
                    )}
                </div>
                <button 
                    className="collapse-btn"
                    onClick={() => setIsCollapsed(!isCollapsed)}
                >
                    {isCollapsed ? <FaBars /> : <FaTimes />}
                </button>
            </div>

            <div className="sidebar-menu">
                {menuItems.map((item, index) => (
                    <Link 
                        key={index}
                        to={item.path} 
                        className={`menu-item ${isActive(item.path, item.exact) ? 'active' : ''}`}
                        title={isCollapsed ? item.label : ''}
                    >
                        <item.icon className="menu-icon" />
                        <span className={isCollapsed ? 'collapsed-text' : ''}>{item.label}</span>
                    </Link>
                ))}
            </div>

            <div className="sidebar-footer">
                <Link to="/admin/settings" className="menu-item" title={isCollapsed ? 'Cài đặt' : ''}>
                    <FaCog className="menu-icon" />
                    <span className={isCollapsed ? 'collapsed-text' : ''}>Cài đặt</span>
                </Link>
                <Link to="/login" className="menu-item logout-btn" title={isCollapsed ? 'Đăng xuất' : ''}>
                    <FaSignOutAlt className="menu-icon"/>
                    <span className={isCollapsed ? 'collapsed-text' : ''}>Đăng xuất</span>
                </Link>
            </div>
        </div>
    );
};

export default Sidebar; 