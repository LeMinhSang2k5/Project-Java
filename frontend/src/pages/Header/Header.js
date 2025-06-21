import 'bootstrap/dist/css/bootstrap.min.css';
import './Header.scss';

import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { NavLink, useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';

const Header = () => {
    const navigate = useNavigate();
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
                        
                        <NavDropdown title="Tài liệu & Hướng dẫn" id="health-docs-dropdown">
                            <NavDropdown.Item as={NavLink} to="/docs/general">Sức khỏe học đường</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/docs/nutrition">Dinh dưỡng học đường</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/docs/prevention">Phòng chống dịch bệnh</NavDropdown.Item>
                        </NavDropdown>

                        <NavLink to="/blog" className="nav-link">Blog chia sẻ</NavLink>
                        
                        <NavDropdown title="Hồ sơ sức khỏe" id="health-profile-dropdown">
                            <NavDropdown.Item as={NavLink} to="/health-profile/new">Khai báo hồ sơ mới</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/health-profile/allergies">Dị ứng</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/health-profile/chronic">Bệnh mãn tính</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/health-profile/treatment">Tiền sử điều trị</NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item as={NavLink} to="/health-profile/vision">Thị lực</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/health-profile/hearing">Thính lực</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="/health-profile/vaccination">Tiêm chủng</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav>
                        <Button variant="outline-primary" className="nav-link" style={{marginLeft: 8, border: '1px solid #007bff'}} onClick={() => navigate('/login')}>
                            Đăng nhập
                        </Button>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header; 