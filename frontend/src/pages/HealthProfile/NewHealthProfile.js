import React, { useState, useEffect } from 'react';
import { Form, Button, Card, Container, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
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
    const { studentId: urlStudentId } = useParams();
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
    const [alertMsg, setAlertMsg] = useState(null);
    const [studentId, setStudentId] = useState(null);

useEffect(() => {
    if (urlStudentId) {
        setStudentId(urlStudentId);
        
        // Lấy thông tin student trước
        api.get(`/students/${urlStudentId}`)
            .then(studentRes => {
                const student = studentRes.data;
                // Điền thông tin cơ bản từ student
                setFormData(prev => ({
                    ...prev,
                    studentName: student.fullName || '',
                    dateOfBirth: student.dateOfBirth || '',
                    gender: student.gender || '',
                    grade: student.grade || '',
                    className: student.studentClass || '',
                    city: student.city || '',
                    district: student.district || ''
                }));
                
                // Sau đó kiểm tra xem có health profile không
                return api.get(`/health-profiles/student/${urlStudentId}`);
            })
            .then(healthRes => {
                if (healthRes && healthRes.data) {
                    // Nếu có health profile, cập nhật với dữ liệu từ health profile
                    setFormData(prev => ({
                        ...prev,
                        studentName: healthRes.data.studentName || prev.studentName,
                        dateOfBirth: healthRes.data.dateOfBirth || prev.dateOfBirth,
                        gender: healthRes.data.gender || prev.gender,
                        grade: healthRes.data.grade || prev.grade,
                        className: healthRes.data.className || prev.className,
                        city: healthRes.data.city || prev.city,
                        district: healthRes.data.district || prev.district,
                        allergies: healthRes.data.allergies || '',
                        chronicDiseases: healthRes.data.chronicDiseases || '',
                        medicalHistory: healthRes.data.medicalHistory || '',
                        visionDetails: healthRes.data.visionDetails || '',
                        hearingDetails: healthRes.data.hearingDetails || '',
                        vaccinationHistory: healthRes.data.vaccinationHistory || ''
                    }));
                    setAlertMsg("Hồ sơ sức khỏe đã tồn tại. Bạn có thể cập nhật thông tin.");
                } else {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                }
            })
            .catch(err => {
                console.error('Error loading data:', err);
                if (err.response && err.response.status === 404) {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                } else {
                    setAlertMsg("Có lỗi khi tải thông tin học sinh.");
                }
            });
        return;
    }

    const studentIdFromStorage = localStorage.getItem('studentId');
    if (studentIdFromStorage) {
        setStudentId(studentIdFromStorage);
        
        // Lấy thông tin student trước
        api.get(`/students/${studentIdFromStorage}`)
            .then(studentRes => {
                const student = studentRes.data;
                // Điền thông tin cơ bản từ student
                setFormData(prev => ({
                    ...prev,
                    studentName: student.fullName || '',
                    dateOfBirth: student.dateOfBirth || '',
                    gender: student.gender || '',
                    grade: student.grade || '',
                    className: student.studentClass || '',
                    city: student.city || '',
                    district: student.district || ''
                }));
                
                // Sau đó kiểm tra xem có health profile không
                return api.get(`/health-profiles/student/${studentIdFromStorage}`);
            })
            .then(healthRes => {
                if (healthRes && healthRes.data) {
                    // Nếu có health profile, cập nhật với dữ liệu từ health profile
                    setFormData(prev => ({
                        ...prev,
                        studentName: healthRes.data.studentName || prev.studentName,
                        dateOfBirth: healthRes.data.dateOfBirth || prev.dateOfBirth,
                        gender: healthRes.data.gender || prev.gender,
                        grade: healthRes.data.grade || prev.grade,
                        className: healthRes.data.className || prev.className,
                        city: healthRes.data.city || prev.city,
                        district: healthRes.data.district || prev.district,
                        allergies: healthRes.data.allergies || '',
                        chronicDiseases: healthRes.data.chronicDiseases || '',
                        medicalHistory: healthRes.data.medicalHistory || '',
                        visionDetails: healthRes.data.visionDetails || '',
                        hearingDetails: healthRes.data.hearingDetails || '',
                        vaccinationHistory: healthRes.data.vaccinationHistory || ''
                    }));
                    setAlertMsg(null);
                } else {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                }
            })
            .catch((err) => {
                if (err.response && err.response.status === 404) {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                } else {
                    setAlertMsg("Có lỗi khi tải hồ sơ sức khỏe.");
                }
            });
        return;
    }

    const parentId = localStorage.getItem('parentId');
    if (parentId) {
        api.get(`/health-profiles/by-parent/${parentId}/student`)
            .then(res => {
                setStudentId(res.data);
                
                // Lấy thông tin student trước
                return api.get(`/students/${res.data}`);
            })
            .then(studentRes => {
                const student = studentRes.data;
                // Điền thông tin cơ bản từ student
                setFormData(prev => ({
                    ...prev,
                    studentName: student.fullName || '',
                    dateOfBirth: student.dateOfBirth || '',
                    gender: student.gender || '',
                    grade: student.grade || '',
                    className: student.studentClass || '',
                    city: student.city || '',
                    district: student.district || ''
                }));
                
                // Sau đó kiểm tra xem có health profile không
                return api.get(`/health-profiles/student/${student.id}`);
            })
            .then(healthRes => {
                if (healthRes && healthRes.data) {
                    // Nếu có health profile, cập nhật với dữ liệu từ health profile
                    setFormData(prev => ({
                        ...prev,
                        studentName: healthRes.data.studentName || prev.studentName,
                        dateOfBirth: healthRes.data.dateOfBirth || prev.dateOfBirth,
                        gender: healthRes.data.gender || prev.gender,
                        grade: healthRes.data.grade || prev.grade,
                        className: healthRes.data.className || prev.className,
                        city: healthRes.data.city || prev.city,
                        district: healthRes.data.district || prev.district,
                        allergies: healthRes.data.allergies || '',
                        chronicDiseases: healthRes.data.chronicDiseases || '',
                        medicalHistory: healthRes.data.medicalHistory || '',
                        visionDetails: healthRes.data.visionDetails || '',
                        hearingDetails: healthRes.data.hearingDetails || '',
                        vaccinationHistory: healthRes.data.vaccinationHistory || ''
                    }));
                    setAlertMsg(null);
                } else {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                }
            })
            .catch((err) => {
                if (err.response && err.response.status === 404) {
                    setAlertMsg("Chưa có hồ sơ sức khỏe cho học sinh này. Vui lòng tạo mới!");
                } else {
                    setAlertMsg("Có lỗi khi tải hồ sơ sức khỏe.");
                }
            });
    }
}, [urlStudentId]);

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

            if (!formData.studentName || !formData.dateOfBirth || !formData.gender ||
                !formData.grade || !formData.className || !formData.city || !formData.district) {
                window.alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
                return;
            }
            if (!studentId) {
    window.alert('Không tìm thấy học sinh cho phụ huynh này!');
    return;
}

            const dataToSend = {
                ...formData,
                student: {
                    id: Number(studentId)
                }
            };

            // Kiểm tra xem hồ sơ đã tồn tại chưa để quyết định tạo mới hay cập nhật
            try {
                const existingProfile = await api.get(`/health-profiles/student/${studentId}`);
                console.log('Existing profile found:', existingProfile.data);
                
                if (existingProfile.data) {
                    // Cập nhật hồ sơ hiện có
                    const updateData = {
                        ...formData,
                        id: existingProfile.data.id, // Thêm ID của health profile
                        student: {
                            id: Number(studentId)
                        }
                    };

                    const updateResponse = await api.put(`/health-profiles/${existingProfile.data.id}`, updateData);
                    console.log('Update response:', updateResponse.data);
                    window.alert('Hồ sơ sức khỏe đã được cập nhật thành công!');
                    
                    // Refresh dữ liệu sau khi cập nhật thành công
                    await refreshHealthProfileData();
                } else {
                    // Tạo mới hồ sơ
                    const createResponse = await api.post('/health-profiles', dataToSend);
                    console.log('Create response:', createResponse.data);
                    window.alert('Hồ sơ sức khỏe đã được tạo thành công!');
                }
            } catch (error) {
                console.error('Error in health profile operation:', error);
                if (error.response && error.response.status === 404) {
                    // Không tìm thấy hồ sơ, tạo mới
                    const createResponse = await api.post('/health-profiles', dataToSend);
                    console.log('Create response:', createResponse.data);
                    window.alert('Hồ sơ sức khỏe đã được tạo thành công!');
                } else {
                    throw error;
                }
            }
            
            // Chuyển về trang parent sau khi hoàn thành
            navigate('/parent');
        } catch (error) {
            if (error.response) {
                window.alert(`Lỗi: ${error.response.data.message || 'Có lỗi xảy ra khi tạo hồ sơ sức khỏe'}`);
            } else if (error.request) {
                window.alert('Không thể kết nối đến server. Vui lòng kiểm tra lại kết nối!');
            } else {
                window.alert('Có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại!');
            }
        }
    };

    // Function để refresh dữ liệu health profile
    const refreshHealthProfileData = async () => {
        if (!studentId) return;
        
        try {
            // Lấy thông tin student trước
            const studentResponse = await api.get(`/students/${studentId}`);
            const student = studentResponse.data;
            
            // Điền thông tin cơ bản từ student
            const baseData = {
                studentName: student.fullName || '',
                dateOfBirth: student.dateOfBirth || '',
                gender: student.gender || '',
                grade: student.grade || '',
                className: student.studentClass || '',
                city: student.city || '',
                district: student.district || ''
            };
            
            // Sau đó lấy thông tin từ health profile nếu có
            try {
                const healthResponse = await api.get(`/health-profiles/student/${studentId}`);
                if (healthResponse.data) {
                    setFormData({
                        ...baseData,
                        studentName: healthResponse.data.studentName || baseData.studentName,
                        dateOfBirth: healthResponse.data.dateOfBirth || baseData.dateOfBirth,
                        gender: healthResponse.data.gender || baseData.gender,
                        grade: healthResponse.data.grade || baseData.grade,
                        className: healthResponse.data.className || baseData.className,
                        city: healthResponse.data.city || baseData.city,
                        district: healthResponse.data.district || baseData.district,
                        allergies: healthResponse.data.allergies || '',
                        chronicDiseases: healthResponse.data.chronicDiseases || '',
                        medicalHistory: healthResponse.data.medicalHistory || '',
                        visionDetails: healthResponse.data.visionDetails || '',
                        hearingDetails: healthResponse.data.hearingDetails || '',
                        vaccinationHistory: healthResponse.data.vaccinationHistory || ''
                    });
                } else {
                    setFormData({
                        ...baseData,
                        allergies: '',
                        chronicDiseases: '',
                        medicalHistory: '',
                        visionDetails: '',
                        hearingDetails: '',
                        vaccinationHistory: ''
                    });
                }
                console.log('Health profile data refreshed:', healthResponse.data || 'No health profile found');
            } catch (healthError) {
                if (healthError.response?.status === 404) {
                    // Không có health profile, chỉ sử dụng thông tin từ student
                    setFormData({
                        ...baseData,
                        allergies: '',
                        chronicDiseases: '',
                        medicalHistory: '',
                        visionDetails: '',
                        hearingDetails: '',
                        vaccinationHistory: ''
                    });
                } else {
                    throw healthError;
                }
            }
        } catch (error) {
            console.error('Error refreshing health profile data:', error);
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
                {alertMsg && <Alert variant="info">{alertMsg}</Alert>}
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
                                                <option value="MALE">Nam</option>
                                                <option value="FEMALE">Nữ</option>
                                                <option value="OTHER">Khác</option>
                                            </Form.Select>
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