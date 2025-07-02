import 'bootstrap/dist/css/bootstrap.min.css';
import './Header.scss';
import React, { useState, useEffect } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { NavLink, useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';

const Header = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

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
    }, []);

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
    

    return (
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container>
                <NavLink to="/" className="navbar-brand">
                    🏥 Y Tế Học Đường
                </NavLink>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <NavLink to="/" className="nav-link">Trang chủ</NavLink>
                        <NavLink to="/about" className="nav-link">Giới thiệu</NavLink>
                        <NavLink to="/docs/general" className="nav-link">Tài liệu & Hướng dẫn</NavLink>
                        <NavLink to="/blog" className="nav-link">Blog chia sẻ</NavLink>
                        

                    </Nav>
                    <Nav>
                        {user ? (
                            // Đã đăng nhập: Hiển thị tên user và dropdown
                            <NavDropdown title={`Xin chào, ${ user.fullName}`} id="user-dropdown" align="end">
                                <NavDropdown.Item disabled>
                                    <strong>{user.fullName}</strong><br />
                                    <small className="text-muted">Vai trò: {user.role}</small>
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={handleLogin}>
                                    Thông tin cá nhân
                                </NavDropdown.Item>
                                <NavDropdown.Item onClick={handleLogout}>
                                    Đăng xuất
                                </NavDropdown.Item>
                            </NavDropdown>
                        ) : (
                            // Chưa đăng nhập: Hiển thị nút đăng nhập
                            <Button 
                                variant="outline-primary" 
                                className="nav-link" 
                                style={{marginLeft: 8, border: '1px solid #007bff'}} 
                                onClick={() => navigate('/login')}
                            >
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