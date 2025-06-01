import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom'; // Nếu có các link nội bộ
import { FaBrain, FaCalendarAlt, FaBullhorn, FaHeartbeat, FaUsers, FaRocket } from 'react-icons/fa'; // Chọn icon phù hợp

import './AboutPage.scss'; // File SCSS riêng cho trang About

// Một component nhỏ cho mỗi mục dịch vụ (có thể tách ra file riêng nếu muốn)
const ServiceHighlight = ({ icon, title, description, linkTo, linkText }) => (
    <Col md={6} lg={3} className="mb-4 d-flex align-items-stretch">
        <Card className="text-center h-100 shadow-sm service-card">
            <Card.Body className="d-flex flex-column">
                <div className="service-icon mb-3">{icon}</div>
                <Card.Title as="h5" className="mb-3">{title}</Card.Title>
                <Card.Text className="flex-grow-1 small">{description}</Card.Text>
                {linkTo && linkText && (
                    <Button as={Link} to={linkTo} variant="outline-primary" size="sm" className="mt-auto">
                        {linkText} <FaRocket className="ms-1" />
                    </Button>
                )}
            </Card.Body>
        </Card>
    </Col>
);

const AboutPage = () => {
    const schoolName = "Tên Trường Đại Học Của Bạn"; // Thay bằng tên trường thực tế
    const websiteName = "Y Tế Học Đường"; // Hoặc tên website cụ thể

    return (
        <Container fluid className="about-page-container py-5">
            {/* --- Phần Hero --- */}
            <Row className="justify-content-center text-center mb-5 hero-section">
                <Col md={10} lg={8}>
                    <h1 className="display-4 fw-bold mb-3">
                        👋 Chào Bạn, Người Bạn Đồng Hành Sức Khỏe Của Sinh Viên {schoolName}!
                    </h1>
                    <p className="lead text-muted mb-4">
                        Cuộc sống sinh viên thật tuyệt vời! Giữa giảng đường, deadline, và muôn vàn hoạt động,
                        đôi khi chúng mình lỡ quên đi "người bạn" quan trọng: **SỨC KHỎE**.
                        Và đó là lý do **{websiteName}** ra đời – một người bạn, một góc nhỏ bình yên cho bạn.
                    </p>
                    {/* Có thể thêm hình ảnh ở đây nếu muốn */}
                    {/* <img src="/path/to/your/hero-image.jpg" alt="Sinh viên năng động" className="img-fluid rounded shadow mb-4" /> */}
                </Col>
            </Row>

            {/* --- Phần Chúng Mình Là Ai? --- */}
            <Row className="justify-content-center mb-5 section-intro">
                <Col md={10} lg={8} className="text-center">
                    <h2 className="section-title mb-3">Chúng Mình Là Ai? 🤔</h2>
                    <p className="mb-0">
                        Chúng mình là đội ngũ [Phòng Y Tế/Trung Tâm Tư Vấn Sức Khỏe Sinh Viên] của Đại học {schoolName}.
                        Sứ mệnh của chúng mình: mong muốn mỗi sinh viên đều có một hành trình đại học thật **TRỌN VẸN** –
                        khỏe mạnh về thể chất, vững vàng về tinh thần để tự tin theo đuổi đam mê.
                    </p>
                </Col>
            </Row>

            {/* --- Phần Dịch Vụ Nổi Bật --- */}
            <Row className="justify-content-center mb-5 services-section">
                <Col md={10} lg={10} className="text-center mb-4">
                    <h2 className="section-title mb-3">"Ngôi Nhà Chung" Này Có Gì Hay Ho Cho Bạn? ✨</h2>
                </Col>
                <ServiceHighlight
                    icon={<FaBrain size={40} className="text-primary" />}
                    title='"Não Cá Vàng"? Có "Bách Khoa Sức Khỏe"!'
                    description="Thông tin từ A-Z về dinh dưỡng, bí kíp mùa thi, phòng bệnh... dễ hiểu, gần gũi, không khô khan!"
                    linkTo="/docs/general" // Thay bằng link thực tế
                    linkText="Khám phá ngay"
                />
                <ServiceHighlight
                    icon={<FaHeartbeat size={40} className="text-danger" />}
                    title='"Trái Tim Cần Lời Khuyên"? Ghé "Góc Tư Vấn Tâm Lý"!'
                    description="Các chuyên gia tâm lý sẵn sàng lắng nghe, chia sẻ và đồng hành cùng bạn. Mọi chia sẻ đều bảo mật!"
                    linkTo="/counselling" // Thay bằng link thực tế
                    linkText="Tìm hiểu thêm"
                />
                <ServiceHighlight
                    icon={<FaCalendarAlt size={40} className="text-success" />}
                    title='Cần Gặp Bác Sĩ? "Đặt Lịch Thông Minh"!'
                    description="Chủ động đặt lịch khám tại phòng y tế trường, chọn khung giờ phù hợp, tiện lợi và nhanh chóng."
                    linkTo="/appointment/new" // Thay bằng link thực tế
                    linkText="Đặt lịch"
                />
                <ServiceHighlight
                    icon={<FaBullhorn size={40} className="text-warning" />}
                    title='"Tin Nóng" Dịch Bệnh? "Thông Báo Khẩn" Có Liền!'
                    description="Cập nhật nhanh nhất thông tin dịch bệnh và hướng dẫn phòng ngừa để bạn luôn chủ động bảo vệ sức khỏe."
                    linkTo="/notifications" // Thay bằng link thực tế
                    linkText="Xem tin tức"
                />
            </Row>

            {/* --- Phần Triết Lý --- */}
            <Row className="justify-content-center mb-5 philosophy-section bg-light py-5">
                <Col md={10} lg={8} className="text-center">
                    <FaRocket size={50} className="text-primary mb-3" />
                    <h2 className="section-title mb-3">Triết Lý Của Chúng Mình: "Khỏe Để Bay Cao!" 🚀</h2>
                    <p className="lead">
                        Tại {websiteName}, sức khỏe không chỉ là "không bệnh tật".
                        Đó là sự **cân bằng**, là **niềm vui sống**, là **năng lượng tích cực** để bạn khám phá thế giới và chính mình.
                        Chúng mình ở đây để **lắng nghe, chia sẻ và cùng bạn xây dựng những thói quen tốt**.
                    </p>
                </Col>
            </Row>

            {/* --- Phần Lời Kêu Gọi Hành Động --- */}
            <Row className="justify-content-center text-center cta-section">
                <Col md={10} lg={8}>
                    <h2 className="section-title mb-3">Hãy Cùng Nhau Tạo Nên Một Cộng Đồng Sinh Viên {schoolName} Khỏe Mạnh!</h2>
                    <p className="mb-4">
                        Đừng ngần ngại khám phá "ngôi nhà chung" này! Hãy lướt xem các bài viết, tìm kiếm thông tin bạn cần,
                        hoặc ghé qua để biết rằng bạn không đơn độc trên hành trình chăm sóc sức khỏe này.
                    </p>
                    <Button as={Link} to="/contact" variant="primary" size="lg">
                        <FaUsers className="me-2" /> Liên Hệ Với Chúng Mình
                    </Button>
                </Col>
            </Row>
        </Container>
    );
};

export default AboutPage;