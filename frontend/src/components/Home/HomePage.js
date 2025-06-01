import React from 'react';
import { Container, Row, Col, Card, Button, Carousel } from 'react-bootstrap';
import './HomePage.scss';

function HomePage() {
  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero bg-primary text-white text-center">
        <img src={require("../../assets/img/home-bg.png")} alt="School Healthcare" className="img-fluid mb-4" />
        <div className="hero-content">
          <h1 className="display-4 mb-3">Hệ Thống Y Tế Học Đường</h1>
          <p className="lead mb-4">
            🩺 Chào mừng đến với Cổng Thông Tin Y Tế Trường Học!
            <br />
            Chúng tôi luôn đồng hành cùng sức khỏe học sinh – sinh viên mỗi ngày. ❤️
          </p>
          <Button variant="light" size="lg" className="explore-btn">
            Khám phá ngay 🚀
          </Button>
        </div>
      </section>

      {/* Features Section */}
      <Container className="features-section my-5">
        <h2 className="text-center mb-5">Tính năng nổi bật 🌟</h2>
        <Row>
          <Col md={4}>
            <Card className="feature-card text-center h-100">
              <Card.Body>
                <div className="feature-icon">📋</div>
                <Card.Title>Hồ sơ y tế</Card.Title>
                <Card.Text>Quản lý hồ sơ sức khỏe của học sinh nhanh chóng và chính xác.</Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="feature-card text-center h-100">
              <Card.Body>
                <div className="feature-icon">🩺</div>
                <Card.Title>Khám sức khỏe</Card.Title>
                <Card.Text>Ghi lại lịch sử kiểm tra sức khỏe định kỳ và theo dõi tình trạng.</Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="feature-card text-center h-100">
              <Card.Body>
                <div className="feature-icon">🚨</div>
                <Card.Title>Cảnh báo y tế</Card.Title>
                <Card.Text>Thông báo tình trạng khẩn cấp và cảnh báo sức khỏe kịp thời.</Card.Text>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>

      {/* Quick Links Section */}
            <section className="quick-links bg-primary text-white py-5">
        <Container>
          <h2 className="text-center mb-4">Liên kết nhanh 🔗</h2>
          <Row className="justify-content-center">
            <Col md={3} className="text-center mb-3">
              <Button variant="light" className="w-100 mb-2">Gửi Thuốc</Button>
            </Col>
            <Col md={3} className="text-center mb-3">
              <Button variant="light" className="w-100 mb-2">Xem lịch tiêm chủng</Button>
            </Col>
            <Col md={3} className="text-center mb-3">
              <Button variant="light" className="w-100 mb-2">Tư vấn sức khỏe</Button>
            </Col>
            <Col md={3} className="text-center mb-3">
              <Button variant="light" className="w-100 mb-2">Báo cáo sự cố</Button>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Health Tips Section */}
      <section className="health-tips bg-light py-5">
        <Container>
          <h2 className="text-center mb-5">Lời khuyên sức khỏe 💡</h2>
          <Row>
            <Col md={6} lg={3}>
              <Card className="tip-card mb-4">
                <Card.Body>
                  <h5>🍎 Dinh dưỡng học đường</h5>
                  <p>Chế độ ăn cân bằng cho học sinh phát triển toàn diện</p>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6} lg={3}>
              <Card className="tip-card mb-4">
                <Card.Body>
                  <h5>💪 Vận động thể chất</h5>
                  <p>Hoạt động thể dục thể thao phù hợp với lứa tuổi</p>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6} lg={3}>
              <Card className="tip-card mb-4">
                <Card.Body>
                  <h5>😴 Giấc ngủ lành mạnh</h5>
                  <p>Hướng dẫn thói quen ngủ tốt cho học sinh</p>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6} lg={3}>
              <Card className="tip-card mb-4">
                <Card.Body>
                  <h5>🧠 Sức khỏe tinh thần</h5>
                  <p>Chăm sóc sức khỏe tâm lý cho học sinh</p>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* News & Events Section */}
      <Container className="news-section my-5">
        <h2 className="text-center mb-5">Tin tức & Sự kiện 📰</h2>
        <Row>
          <Col md={4}>
            <Card className="news-card mb-4">
              <Card.Img variant="top" src={require("../../assets/img/new1.jpg")} />
              <Card.Body>
                <Card.Title>Chiến dịch tiêm chủng mở rộng</Card.Title>
                <Card.Text>Thông tin về lịch tiêm chủng và các biện pháp phòng ngừa</Card.Text>
                <Button variant="outline-primary">Đọc thêm</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="news-card mb-4">
              <Card.Img variant="top" src={require("../../assets/img/new2.jpg")} />
              <Card.Body>
                <Card.Title>Hội thảo sức khỏe học đường</Card.Title>
                <Card.Text>Chia sẻ kinh nghiệm về chăm sóc sức khỏe học sinh</Card.Text>
                <Button variant="outline-primary">Đọc thêm</Button>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card className="news-card mb-4">
              <Card.Img variant="top" src={require("../../assets/img/new3.jpg")} />
              <Card.Body>
                <Card.Title>Chương trình khám sức khỏe định kỳ</Card.Title>
                <Card.Text>Lịch khám sức khỏe cho học sinh toàn trường</Card.Text>
                <Button variant="outline-primary">Đọc thêm</Button>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>



      {/* Contact Section */}
      <Container className="contact-section my-5">
        <Row className="justify-content-center">
          <Col md={8} className="text-center">
            <h2 className="mb-4">Liên hệ với chúng tôi 📞</h2>
            <p className="lead mb-4">
              Bạn cần hỗ trợ hoặc có thắc mắc? Hãy liên hệ với chúng tôi!
            </p>
            <Button variant="primary" size="lg" className="me-3">
              Gọi ngay
            </Button>
            <Button variant="outline-primary" size="lg">
              Gửi email
            </Button>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default HomePage;