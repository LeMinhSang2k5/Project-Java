import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { FaHeartbeat, FaUserMd, FaExclamationTriangle, FaCalendarAlt, FaPills, FaComments, FaChartBar, FaAppleAlt, FaDumbbell, FaBed, FaBrain, FaPhone } from 'react-icons/fa';
import './HomePage.scss';
import logo from '../../assets/img/logo_uth.png';

function HomePage() {
  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-background">
          <div className="hero-overlay"></div>
        </div>
        <Container className="hero-content">
          <Row className="align-items-center min-vh-100">
            <Col lg={6} className="hero-text">
              <div className="hero-badge">
               <span> Hệ thống Y tế Học đường</span>
              </div>
              <h1 className="hero-title">
                <span>Chăm sóc sức khỏe học sinh</span>
                <br />
                <span>một cách toàn diện</span>
              </h1>
              <p className="hero-description">
                Nền tảng quản lý y tế học đường hiện đại, giúp theo dõi và chăm sóc sức khỏe 
                học sinh một cách hiệu quả và chuyên nghiệp.
              </p>
              <div className="hero-buttons">
                <Button variant="primary" size="lg" className="cta-button">
                  <FaHeartbeat className="btn-icon" />
                  Khám phá ngay
                </Button>
                <Button variant="outline-light" size="lg" className="secondary-button">
                  Tìm hiểu thêm
                </Button>
              </div>
            </Col>
            <Col lg={6} className="hero-visual">
              <div className="hero-image-container">
                <img src={require("../../assets/img/home-bg.png")} alt="School Healthcare" className="hero-image" />
                <div className="floating-card card-1">
                  <FaUserMd className="card-icon" />
                  <span>Y tá chuyên nghiệp</span>
                </div>
                <div className="floating-card card-2">
                  <FaHeartbeat className="card-icon" />
                  <span>Khám sức khỏe</span>
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <Container>
          <div className="section-header">
            <h2 className="section-title">
              Tính năng <span className="gradient-text">nổi bật</span>
            </h2>
            <p className="section-subtitle">
              Hệ thống được thiết kế với những tính năng hiện đại nhất để phục vụ tốt nhất cho việc chăm sóc sức khỏe học sinh
            </p>
          </div>
          
          <Row className="features-grid">
            <Col lg={4} md={6} className="feature-item">
              <Card className="feature-card">
                <div className="feature-icon-wrapper">
                  <FaUserMd className="feature-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Hồ sơ y tế</Card.Title>
                  <Card.Text>
                    Quản lý hồ sơ sức khỏe của học sinh một cách chi tiết và chính xác, 
                    theo dõi lịch sử khám bệnh và tiêm chủng.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={4} md={6} className="feature-item">
              <Card className="feature-card">
                <div className="feature-icon-wrapper">
                  <FaHeartbeat className="feature-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Khám sức khỏe</Card.Title>
                  <Card.Text>
                    Ghi lại lịch sử kiểm tra sức khỏe định kỳ và theo dõi tình trạng 
                    sức khỏe của từng học sinh một cách liên tục.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={4} md={6} className="feature-item">
              <Card className="feature-card">
                <div className="feature-icon-wrapper">
                  <FaExclamationTriangle className="feature-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Cảnh báo y tế</Card.Title>
                  <Card.Text>
                    Hệ thống cảnh báo thông minh giúp phát hiện sớm các vấn đề sức khỏe 
                    và thông báo kịp thời cho phụ huynh và nhà trường.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Quick Actions Section */}
      <section className="quick-actions-section">
        <Container>
          <div className="section-header text-center">
            <h2 className="section-title text-white">
              Thao Tác Nhanh
            </h2>
            <p className="section-subtitle text-white">
              Truy cập nhanh các tính năng quan trọng nhất
            </p>
          </div>
          
          <Row className="quick-actions-grid">
            <Col lg={3} md={6} className="action-item">
              <Button variant="light" className="action-button" href="/medical">
                <FaPills className="action-icon" />
                <span>Gửi Thuốc</span>
              </Button>
            </Col>
            
            <Col lg={3} md={6} className="action-item">
              <Button variant="light" className="action-button" href="/calendar">
                <FaCalendarAlt className="action-icon" />
                <span>Lịch tiêm chủng</span>
              </Button>
            </Col>
            
            <Col lg={3} md={6} className="action-item">
              <Button variant="light" className="action-button">
                <FaComments className="action-icon" />
                <span>Tư vấn sức khỏe</span>
              </Button>
            </Col>
            
            <Col lg={3} md={6} className="action-item">
              <Button variant="light" className="action-button" href="/report">
                <FaChartBar className="action-icon" />
                <span>Báo cáo sự cố</span>
              </Button>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Health Tips Section */}
      <section className="health-tips-section">
        <Container>
          <div className="section-header">
            <h2 className="section-title">
              Lời khuyên <span className="gradient-text">sức khỏe</span>
            </h2>
            <p className="section-subtitle">
              Những lời khuyên hữu ích để duy trì sức khỏe tốt cho học sinh
            </p>
          </div>
          
          <Row className="tips-grid">
            <Col lg={3} md={6} className="tip-item">
              <Card className="tip-card">
                <div className="tip-icon-wrapper">
                  <FaAppleAlt className="tip-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Dinh dưỡng học đường</Card.Title>
                  <Card.Text>
                    Chế độ ăn cân bằng với đầy đủ chất dinh dưỡng cần thiết 
                    cho sự phát triển toàn diện của học sinh.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={3} md={6} className="tip-item">
              <Card className="tip-card">
                <div className="tip-icon-wrapper">
                  <FaDumbbell className="tip-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Vận động thể chất</Card.Title>
                  <Card.Text>
                    Khuyến khích các hoạt động thể dục thể thao phù hợp 
                    với lứa tuổi để tăng cường sức khỏe.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={3} md={6} className="tip-item">
              <Card className="tip-card">
                <div className="tip-icon-wrapper">
                  <FaBed className="tip-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Giấc ngủ lành mạnh</Card.Title>
                  <Card.Text>
                    Hướng dẫn thói quen ngủ tốt và đảm bảo thời gian 
                    ngủ đủ cho sự phát triển của học sinh.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={3} md={6} className="tip-item">
              <Card className="tip-card">
                <div className="tip-icon-wrapper">
                  <FaBrain className="tip-icon" />
                </div>
                <Card.Body>
                  <Card.Title>Sức khỏe tinh thần</Card.Title>
                  <Card.Text>
                    Chăm sóc sức khỏe tâm lý và tinh thần cho học sinh 
                    trong môi trường học tập hiện đại.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* News & Events Section */}
      <section className="news-section">
        <Container>
          <div className="section-header">
            <h2 className="section-title">
              Tin tức & <span className="gradient-text">Sự kiện</span>
            </h2>
            <p className="section-subtitle">
              Cập nhật những thông tin mới nhất về sức khỏe học đường
            </p>
          </div>
          
          <Row className="news-grid">
            <Col lg={4} md={6} className="news-item">
              <Card className="news-card">
                <div className="news-image-wrapper">
                  <Card.Img variant="top" src={require("../../assets/img/new1.jpg")} className="news-image" />
                  <div className="news-overlay">
                    <span className="news-category">Tiêm chủng</span>
                  </div>
                </div>
                <Card.Body>
                  <Card.Title>Chiến dịch tiêm chủng mở rộng</Card.Title>
                  <Card.Text>
                    Thông tin chi tiết về lịch tiêm chủng và các biện pháp 
                    phòng ngừa bệnh tật cho học sinh.
                  </Card.Text>
                  <Button variant="outline-primary" className="read-more-btn">
                    Đọc thêm
                  </Button>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={4} md={6} className="news-item">
              <Card className="news-card">
                <div className="news-image-wrapper">
                  <Card.Img variant="top" src={require("../../assets/img/new2.jpg")} className="news-image" />
                  <div className="news-overlay">
                    <span className="news-category">Hội thảo</span>
                  </div>
                </div>
                <Card.Body>
                  <Card.Title>Hội thảo sức khỏe học đường</Card.Title>
                  <Card.Text>
                    Chia sẻ kinh nghiệm và kiến thức về chăm sóc sức khỏe 
                    học sinh từ các chuyên gia y tế.
                  </Card.Text>
                  <Button variant="outline-primary" className="read-more-btn">
                    Đọc thêm
                  </Button>
                </Card.Body>
              </Card>
            </Col>
            
            <Col lg={4} md={6} className="news-item">
              <Card className="news-card">
                <div className="news-image-wrapper">
                  <Card.Img variant="top" src={require("../../assets/img/new3.jpg")} className="news-image" />
                  <div className="news-overlay">
                    <span className="news-category">Khám sức khỏe</span>
                  </div>
                </div>
                <Card.Body>
                  <Card.Title>Chương trình khám sức khỏe định kỳ</Card.Title>
                  <Card.Text>
                    Lịch trình khám sức khỏe toàn diện cho học sinh 
                    toàn trường trong năm học mới.
                  </Card.Text>
                  <Button variant="outline-primary" className="read-more-btn">
                    Đọc thêm
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Contact Section */}
      <section className="contact-section">
        <Container>
          <Row className="justify-content-center">
            <Col lg={8} className="text-center">
              <div className="contact-content">
                <h2 className="contact-title">
                  Liên hệ với <span className="gradient-text">chúng tôi</span>
                </h2>
                <p className="contact-description">
                  Bạn cần hỗ trợ hoặc có thắc mắc về hệ thống y tế học đường? 
                  Hãy liên hệ với chúng tôi để được tư vấn và hỗ trợ tốt nhất!
                </p>
                <div className="contact-buttons">
                  <Button variant="outline-primary" size="lg" className="contact-btn" href="/sendmail">
                    <FaComments className="btn-icon" />
                    Gửi email
                  </Button>
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </section>
    </div>
  );
}

export default HomePage;