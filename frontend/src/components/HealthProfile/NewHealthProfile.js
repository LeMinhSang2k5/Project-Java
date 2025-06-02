import React, { useState } from 'react';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import './HealthProfile.scss';

const NewHealthProfile = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        // Thông tin học sinh
        studentName: '',        // Họ và tên học sinh
        dateOfBirth: '',       // Ngày sinh
        gender: '',            // Giới tính
        grade: '',             // Khối học
        className: '',         // Lớp học
        change: '',            // Những đính chính

        // Thông tin gia đình
        fatherName: '',        // Họ và tên bố
        fatherAge: '',         // Tuổi bố
        fatherOccupation: '',  // Nghề nghiệp bố
        motherName: '',        // Họ và tên mẹ
        motherAge: '',         // Tuổi mẹ
        motherOccupation: '',  // Nghề nghiệp mẹ
        phoneNumber: '',       // Số điện thoại liên hệ
        address: '',           // Địa chỉ
        city: '',             // Tỉnh/Thành phố
        district: '',         // Quận/Huyện

        // Thông tin sức khỏe
        allergies: '',         // Dị ứng
        chronicDiseases: '',   // Bệnh mãn tính
        treatmentHistory: '',  // Tiền sử điều trị
        vision: '',           // Thị lực
        hearing: '',          // Thính lực
        vaccinations: []      // Danh sách tiêm chủng
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO: Gửi dữ liệu lên server
        console.log('Form submitted:', formData);
        // Sau khi lưu thành công, chuyển hướng về trang chủ
        navigate('/');
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
                                            <Form.Label>Tỉnh, thành phố</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="city"
                                                value={formData.city}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>

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
                                            <Form.Label>Quận, Huyện</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="district"
                                                value={formData.district}
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
                                </Row>

                                <Form.Group className="mb-3">
                                    <Form.Label>Những đính chính (do thay đổi)</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="change"
                                        value={formData.change}
                                        onChange={handleChange}
                                        required
                                    >
                                    </Form.Control>
                                </Form.Group>
                            </div>

                            <div className="health-profile__section">
                                <h3>Thông tin lý lịch gia đình</h3>
                                <Row>
                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Họ và tên bố</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="fatherName"
                                                value={formData.fatherName}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Tuồi</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="fatherAge"
                                                value={formData.fatherAge}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Nghề nghiệp</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="fatherOccupation"
                                                value={formData.fatherOccupation}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Họ và tên mẹ</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="motherName"
                                                value={formData.motherName}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Tuồi</Form.Label>
                                            <Form.Control
                                                type="number"
                                                name="motherAge"
                                                value={formData.motherAge}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={4}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Nghề nghiệp</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="motherOccupation"
                                                value={formData.motherOccupation}
                                                onChange={handleChange}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Địa chỉ sống hiện tại</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="address"
                                            value={formData.address}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Địa chỉ sống hiện tại</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="address"
                                            value={formData.address}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Số điện thoại liên hệ</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="phoneNumber"
                                            value={formData.phoneNumber}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>
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
                                        name="treatmentHistory"
                                        value={formData.treatmentHistory}
                                        onChange={handleChange}
                                        placeholder="Nhập tiền sử điều trị (nếu có)"
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
                                                name="vision"
                                                value={formData.vision}
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
                                                name="hearing"
                                                value={formData.hearing}
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