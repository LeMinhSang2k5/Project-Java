import React, { useState } from 'react';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../../config/api';
import './HealthProfile.scss';

const VIETNAM_PROVINCES = [
    "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định",
    "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk",
    "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội",
    "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
    "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An",
    "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh",
    "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế",
    "Tiền Giang", "TP Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
];

const NewHealthProfile = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        studentName: '',
        dateOfBirth: '',
        gender: '',
        grade: '',
        className: '',
        city: '',
        district: '',
        allergies: '',
        chronicDiseases: '',
        medicalHistory: '',
        visionDetails: '',
        hearingDetails: '',
        vaccinationHistory: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Validate required fields
            if (!formData.studentName || !formData.dateOfBirth || !formData.gender || 
                !formData.grade || !formData.className || !formData.city || !formData.district) {
                alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
                return;
            }

            const response = await api.post('/health-profiles', formData);
            console.log('Health profile created:', response.data);
            alert('Hồ sơ sức khỏe đã được tạo thành công!');
            navigate('/');
        } catch (error) {
            console.error('Error creating health profile:', error);
            if (error.response) {
                console.error('Server error:', error.response.data);
                alert(`Lỗi: ${error.response.data.message || 'Có lỗi xảy ra khi tạo hồ sơ sức khỏe'}`);
            } else if (error.request) {
                console.error('No response from server:', error.request);
                alert('Không thể kết nối đến server. Vui lòng kiểm tra lại kết nối!');
            } else {
                console.error('Request error:', error.message);
                alert('Có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại!');
            }
        }
    };

    return (
        <div className="health-profile">
            <div className="health-profile__header">
                <Container>
                    <h2>Khai báo hồ sơ sức khỏe học sinh</h2>
                    <p>Vui lòng điền đầy đủ thông tin để tạo hồ sơ sức khỏe cho học sinh</p>
                </Container>
            </div>

            <Container>
                <Card className="health-profile__card">
                    <Card.Body>
                        <Form onSubmit={handleSubmit} className="health-profile__form">
                            <div className="health-profile__section">
                                <h3>Thông tin học sinh</h3>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Họ và tên học sinh</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="studentName"
                                                value={formData.studentName}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Sinh ngày</Form.Label>
                                            <Form.Control
                                                type="date"
                                                name="dateOfBirth"
                                                value={formData.dateOfBirth}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Giới tính</Form.Label>
                                            <Form.Select
                                                name="gender"
                                                value={formData.gender}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">Chọn giới tính</option>
                                                <option value="male">Nam</option>
                                                <option value="female">Nữ</option>
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Khối học</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="grade"
                                                value={formData.grade}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Lớp học</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="className"
                                                value={formData.className}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Tỉnh/Thành phố</Form.Label>
                                            <Form.Select
                                                name="city"
                                                value={formData.city}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">Chọn tỉnh/thành phố</option>
                                                {VIETNAM_PROVINCES.map((province, index) => (
                                                    <option key={index} value={province}>
                                                        {province}
                                                    </option>
                                                ))}
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Quận/Huyện</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="district"
                                                value={formData.district}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </div>

                            <div className="health-profile__section">
                                <h3>Thông tin sức khỏe</h3>
                                <Form.Group className="mb-3">
                                    <Form.Label>Dị ứng</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        name="allergies"
                                        value={formData.allergies}
                                        onChange={handleChange}
                                        placeholder="Nhập các dị ứng (nếu có)"
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Bệnh mãn tính</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        name="chronicDiseases"
                                        value={formData.chronicDiseases}
                                        onChange={handleChange}
                                        placeholder="Nhập các bệnh mãn tính (nếu có)"
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Tiền sử điều trị</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        name="medicalHistory"
                                        value={formData.medicalHistory}
                                        onChange={handleChange}
                                        placeholder="Nhập tiền sử điều trị (nếu có)"
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Lịch sử tiêm chủng</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        name="vaccinationHistory"
                                        value={formData.vaccinationHistory}
                                        onChange={handleChange}
                                        placeholder="Nhập lịch sử tiêm chủng (nếu có)"
                                    />
                                </Form.Group>
                            </div>

                            <div className="health-profile__section">
                                <h3>Khám sức khỏe</h3>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Thị lực</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="visionDetails"
                                                value={formData.visionDetails}
                                                onChange={handleChange}
                                                placeholder="Ví dụ: 10/10"
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Thính lực</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="hearingDetails"
                                                value={formData.hearingDetails}
                                                onChange={handleChange}
                                                placeholder="Ví dụ: Bình thường"
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </div>

                            <div className="text-center mt-4">
                                <Button variant="primary" type="submit" size="lg" className="health-profile__button">
                                    Lưu hồ sơ
                                </Button>
                            </div>
                        </Form>
                    </Card.Body>
                </Card>
            </Container>
        </div>
    );
};

export default NewHealthProfile; 