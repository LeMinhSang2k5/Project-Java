import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Footer.scss';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Nav from 'react-bootstrap/Nav';
import { NavLink } from 'react-router-dom';
import logo from '../../assets/img/logo_uth.png';
import { FaFacebookF, FaTwitter, FaInstagram, FaLinkedinIn, FaEnvelope, FaPhone, FaMapMarkerAlt, FaGithub, FaHeart, FaArrowUp } from 'react-icons/fa';

const Footer = () => {
    const currentYear = new Date().getFullYear();

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    return (
        <footer className="modern-footer">
            {/* Main Footer Content */}
            <div className="footer-main">
                <Container>
                    <Row className="footer-content">
                        {/* Brand Section */}
                        <Col lg={4} md={6} className="footer-brand">
                            <div className="brand-section">
                                <div className="brand-logo">
                                    <img src={logo} className="brand-icon" />
                                    <div className="brand-text">
                                        <h3>Y Tế Học Đường</h3>
                                        <p>Chăm sóc sức khỏe toàn diện</p>
                                    </div>
                                </div>
                                <p className="brand-description">
                                    Nền tảng quản lý y tế học đường hiện đại, cung cấp giải pháp toàn diện 
                                    cho việc chăm sóc sức khỏe học sinh với công nghệ tiên tiến.
                                </p>
                                <div className="brand-stats">
                                    <div className="stat-item">
                                        <span className="stat-number">1000+</span>
                                        <span className="stat-label">Học sinh</span>
                                    </div>
                                    <div className="stat-item">
                                        <span className="stat-number">50+</span>
                                        <span className="stat-label">Y tá</span>
                                    </div>
                                    <div className="stat-item">
                                        <span className="stat-number">99%</span>
                                        <span className="stat-label">Hài lòng</span>
                                    </div>
                                </div>
                            </div>
                        </Col>

                        {/* Quick Links */}
                        <Col lg={2} md={6} className="footer-links">
                            <h4 className="footer-heading">Liên kết nhanh</h4>
                            <Nav className="footer-nav">
                                <NavLink to="/" className="footer-link">
                                    Trang chủ
                                </NavLink>
                                <NavLink to="/about" className="footer-link">
                                    Giới thiệu
                                </NavLink>
                                <NavLink to="/docs/general" className="footer-link">
                                    Tài liệu
                                </NavLink>
                                <NavLink to="/blog" className="footer-link">
                                    Blog
                                </NavLink>
                                <NavLink to="/contact" className="footer-link">
                                    Liên hệ
                                </NavLink>
                            </Nav>
                        </Col>

                        {/* Services */}
                        <Col lg={3} md={6} className="footer-services">
                            <h4 className="footer-heading">Dịch vụ chính</h4>
                            <Nav className="footer-nav">
                                <NavLink to="/docs/nutrition" className="footer-link">
                                    Dinh dưỡng học đường
                                </NavLink>
                                <NavLink to="/docs/prevention" className="footer-link">
                                    Phòng chống dịch bệnh
                                </NavLink>
                                <NavLink to="/health-profile/new" className="footer-link">
                                    Khai báo hồ sơ
                                </NavLink>
                                <NavLink to="/calendar" className="footer-link">
                                    Lịch tiêm chủng
                                </NavLink>
                                <NavLink to="/medical" className="footer-link">
                                    Quản lý thuốc
                                </NavLink>
                            </Nav>
                        </Col>
                        
                        {/* Contact & Social */}
                        <Col lg={3} md={6} className="footer-contact">
                            <h4 className="footer-heading">Liên hệ & Mạng xã hội</h4>
                            
                            <div className="contact-info">
                                <div className="contact-item">
                                    <FaMapMarkerAlt className="contact-icon" />
                                    <div className="contact-details">
                                        <span className="contact-label">Địa chỉ</span>
                                        <span className="contact-value">Đại học ABC, Quận XYZ, TP.HCM</span>
                                    </div>
                                </div>
                                
                                <div className="contact-item">
                                    <FaPhone className="contact-icon" />
                                    <div className="contact-details">
                                        <span className="contact-label">Điện thoại</span>
                                        <span className="contact-value">(028) 1234 5678</span>
                                    </div>
                                </div>
                                
                                <div className="contact-item">
                                    <FaEnvelope className="contact-icon" />
                                    <div className="contact-details">
                                        <span className="contact-label">Email</span>
                                        <span className="contact-value">info@ytehocduong.vn</span>
                                    </div>
                                </div>
                            </div>

                            <div className="social-section">
                                <h5 className="social-title">Theo dõi chúng tôi</h5>
                                <div className="social-icons">
                                    <a href="https://github.com/LeMinhSang2k5/Project-Java" 
                                       target="_blank" 
                                       rel="noopener noreferrer" 
                                       className="social-link github">
                                        <FaGithub />
                                    </a>
                                    <a href="https://facebook.com" 
                                       target="_blank" 
                                       rel="noopener noreferrer" 
                                       className="social-link facebook">
                                        <FaFacebookF />
                                    </a>
                                    <a href="https://twitter.com" 
                                       target="_blank" 
                                       rel="noopener noreferrer" 
                                       className="social-link twitter">
                                        <FaTwitter />
                                    </a>
                                    <a href="https://instagram.com" 
                                       target="_blank" 
                                       rel="noopener noreferrer" 
                                       className="social-link instagram">
                                        <FaInstagram />
                                    </a>
                                    <a href="https://linkedin.com" 
                                       target="_blank" 
                                       rel="noopener noreferrer" 
                                       className="social-link linkedin">
                                        <FaLinkedinIn />
                                    </a>
                                </div>
                            </div>
                        </Col>
                    </Row>
                </Container>
            </div>

            {/* Footer Bottom */}
            <div className="footer-bottom">
                <Container>
                    <Row className="align-items-center">
                        <Col md={6} className="copyright">
                            <p>
                                © {currentYear} Y Tế Học Đường. Phát triển <FaHeart className="heart-icon" /> bởi Team 2K5 Tại Đại học GTVT TP.HCM.
                            </p>
                        </Col>
                        <Col md={6} className="footer-actions">
                            <button onClick={scrollToTop} className="scroll-top-btn">
                                <FaArrowUp />
                                <span>Lên đầu trang</span>
                            </button>
                        </Col>
                    </Row>
                </Container>
            </div>
        </footer>
    );
}

export default Footer;