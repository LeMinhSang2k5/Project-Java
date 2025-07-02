import 'bootstrap/dist/css/bootstrap.min.css';
import './Header.scss';
import React, { useState, useEffect } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { NavLink, useNavigate, useLocation } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import { FaUser, FaSignOutAlt, FaUserCircle, FaCog } from 'react-icons/fa';
import logo from '../../assets/img/logo_uth.png';

const Header = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [user, setUser] = useState(null);
    const [scrolled, setScrolled] = useState(false);

    useEffect(() => {
        // Kiểm tra thông tin user từ localStorage
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            try {
                const parsedUser = JSON.parse(storedUser);
                console.log('🔍 User data from localStorage:', parsedUser);
                console.log('🔍 fullName value:', parsedUser.fullName);
                setUser(parsedUser);
            } catch (error) {
                console.error('Error parsing user data:', error);
                localStorage.removeItem('user');
            }
        }

        // Thêm scroll listener để thay đổi style header
        const handleScroll = () => {
            const isScrolled = window.scrollY > 50;
            setScrolled(isScrolled);
        };

        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    // Function để cuộn lên đầu trang
    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    const handleNavigation = (path) => {
        if (location.pathname === path) {
            scrollToTop();
        } else {
            navigate(path);
            setTimeout(() => {
                scrollToTop();
            }, 100);
        }
    };

    const handleLogout = () => {
        // Xóa thông tin đăng nhập
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        setUser(null);
        navigate('/');
    };

    const handleLogin = () => {
        if (user.role === 'PARENT') {
            navigate('/parent');
        }
        if (user.role === 'STUDENT') {
            navigate('/student');
        }
        if (user.role === 'SCHOOL_NURSE') {
            navigate('/school-nurse');
        }
        if (user.role === 'MANAGER') {
            navigate('/manager');
        }
        if (user.role === 'ADMIN') {
            navigate('/admin');
        }
    };

    const getRoleDisplayName = (role) => {
        const roleNames = {
            'PARENT': 'Phụ huynh',
            'STUDENT': 'Học sinh',
            'SCHOOL_NURSE': 'Y tá',
            'MANAGER': 'Quản lý',
            'ADMIN': 'Quản trị viên'
        };
        return roleNames[role] || role;
    };

    return (
        <Navbar 
            expand="lg" 
            className={`modern-navbar ${scrolled ? 'scrolled' : ''}`}
            fixed="top"
        >
            <Container>
                <NavLink to="/" className="navbar-brand" onClick={scrollToTop}>
                    <div className="brand-content">
                        <img src={logo} className="brand-icon" />
                    </div>
                </NavLink>
                
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <NavLink to="/" className="nav-link">Trang chủ</NavLink>
                        <NavLink to="/about" className="nav-link">Giới thiệu</NavLink>
                        
                        <NavDropdown className="me-2" title="Tài liệu & Hướng dẫn" id="health-docs-dropdown">
                            <NavDropdown.Item as={NavLink} to="/docs/general">Sức khỏe học đường</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/docs/nutrition">Dinh dưỡng học đường</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/docs/prevention">Phòng chống dịch bệnh</NavDropdown.Item>
                        </NavDropdown>

                        <NavLink to="/blog" className="nav-link">Blog chia sẻ</NavLink>
                        

                    </Nav>
                    
                    <Nav className="auth-nav">
                        {user ? (
                            <NavDropdown 
                                title={
                                    <div className="user-profile">
                                        <div className="user-avatar">
                                            <FaUserCircle />
                                        </div>
                                        <div className="user-info">
                                            <span className="user-name">{user.fullName}</span>
                                            <span className="user-role">{getRoleDisplayName(user.role)}</span>
                                        </div>
                                    </div>
                                } 
                                id="user-dropdown" 
                                align="end"
                                className="user-dropdown"
                            >
                                <div className="dropdown-header">
                                    <div className="user-avatar-large">
                                        <FaUserCircle />
                                    </div>
                                    <div className="user-details">
                                        <strong>{user.fullName}</strong>
                                        <small>{getRoleDisplayName(user.role)}</small>
                                    </div>
                                </div>
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={handleLogin}>
                                    <FaUser className="dropdown-icon" />
                                    Dashboard
                                </NavDropdown.Item>
                                <NavDropdown.Item onClick={handleLogout}>
                                    <FaSignOutAlt className="dropdown-icon" />
                                    Đăng xuất
                                </NavDropdown.Item>
                            </NavDropdown>
                        ) : (
                            <Button 
                                variant="primary" 
                                className="login-btn" 
                                onClick={() => navigate('/login')}
                            >
                                <FaUser className="btn-icon" />
                                Đăng nhập
                            </Button>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header; 