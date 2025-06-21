import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Footer.scss'; // File SCSS riêng cho Footer

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Nav from 'react-bootstrap/Nav';
import { NavLink } from 'react-router-dom';
import { FaFacebookF, FaTwitter, FaInstagram, FaLinkedinIn, FaEnvelope, FaPhone, FaMapMarkerAlt, FaGithub } from 'react-icons/fa'; // Thêm các icon bạn muốn

const Footer = () => {
    const currentYear = new Date().getFullYear();

    return (
        <footer className="site-footer bg-dark text-light py-4 mt-auto">
            <Container>
                <Row className="gy-4"> {/* gy-4 for gutter spacing on y-axis */}
                    {/* --- Cột thông tin chung --- */}
                    <Col md={6} lg={4}>
                        <h5 className="footer-heading">
                            <NavLink to="/" className="text-light text-decoration-none">
                                🏥 Y Tế Học Đường
                            </NavLink>
                        </h5>
                        <p className="small text-muted">
                            Nền tảng cung cấp thông tin, tài liệu và công cụ quản lý sức khỏe toàn diện cho học sinh, sinh viên.
                        </p>
                        <p className="small text-muted">
                            <FaMapMarkerAlt className="me-2" /> Đại học ABC, Quận XYZ, Thành phố HCM
                        </p>
                    </Col>

                    {/* --- Cột liên kết nhanh --- */}
                    <Col md={6} lg={2}>
                        <h5 className="footer-heading">Liên kết</h5>
                        <Nav className="flex-column footer-links">
                            <NavLink to="/" className="nav-link px-0">Trang chủ</NavLink>
                            <NavLink to="/about" className="nav-link px-0">Giới thiệu</NavLink>
                            <NavLink to="/docs/general" className="nav-link px-0">Tài liệu</NavLink>
                            <NavLink to="/blog" className="nav-link px-0">Blog</NavLink>
                            <NavLink to="/contact" className="nav-link px-0">Liên hệ</NavLink> {/* Thêm trang liên hệ nếu có */}
                        </Nav>
                    </Col>

                    {/* --- Cột liên kết tài liệu (ví dụ) --- */}
                    <Col md={6} lg={3}>
                        <h5 className="footer-heading">Tài liệu nổi bật</h5>
                        <Nav className="flex-column footer-links">
                            <NavLink to="/docs/nutrition" className="nav-link px-0">Dinh dưỡng học đường</NavLink>
                            <NavLink to="/docs/prevention" className="nav-link px-0">Phòng chống dịch bệnh</NavLink>
                            <NavLink to="/health-profile/new" className="nav-link px-0">Khai báo hồ sơ</NavLink>
                        </Nav>
                    </Col>
                    
                    {/* --- Cột liên hệ & Mạng xã hội --- */}
                    <Col md={6} lg={3}>
                        <h5 className="footer-heading">Theo dõi chúng tôi</h5>
                        <div className="social-icons mb-3">
                            <a href="https://github.com/LeMinhSang2k5/Project-Java" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                                <FaGithub size={20}/>
                            </a>
                            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                                <FaFacebookF size={20} />
                            </a>
                            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                                <FaTwitter size={20} />
                            </a>
                            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                                <FaInstagram size={20} />
                            </a>
                            <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" className="text-light">
                                <FaLinkedinIn size={20} />
                            </a>
                        </div>
                        <p className="small">
                            <FaEnvelope className="me-2" /> info@ytehocduong.vn
                        </p>
                        <p className="small">
                            <FaPhone className="me-2" /> (028) 1234 5678
                        </p>
                    </Col>
                </Row>

                <hr className="my-4" style={{ borderColor: 'rgba(255,255,255,0.2)' }} />

                <Row>
                    <Col className="text-center">
                        <p className="small mb-0">
                            © {currentYear} Y Tế Học Đường. Phát triển bởi [Tên Nhóm/Bạn]. Bảo lưu mọi quyền.
                        </p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
}

export default Footer;