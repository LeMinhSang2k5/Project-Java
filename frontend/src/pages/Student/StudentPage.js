import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Badge, Alert, Spinner, ListGroup, Table } from 'react-bootstrap';
import api from '../../config/api';
import './StudentPage.scss';

const StudentPage = () => {
    const [healthProfile, setHealthProfile] = useState(null);
    const [student, setStudent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [key, setKey] = useState('personal');
    const [medicalRequests, setMedicalRequests] = useState([]);
    const [loadingMedical, setLoadingMedical] = useState(false);

    useEffect(() => {
        fetchStudentProfile();
    }, []);

    useEffect(() => {
        if (key === 'medication') {
            fetchMedicalRequests();
        }
    }, [key]);

    const fetchStudentProfile = async () => {
        try {
            setLoading(true);
            // Lấy student ID từ localStorage khi user đăng nhập
            const user = JSON.parse(localStorage.getItem('user') || '{}');
            const studentId = localStorage.getItem('studentId') || user.id;
            
            if (!studentId) {
                setError('Không tìm thấy thông tin học sinh. Vui lòng đăng nhập lại.');
                return;
            }
            
            // Lấy thông tin student
            const studentRes = await api.get(`/students/${studentId}`);
            setStudent(studentRes.data);

            // Lấy health profile từ parent
            try {
                const healthRes = await api.get(`/health-profiles/student/${studentId}`);
                setHealthProfile(healthRes.data);
            } catch (healthError) {
                if (healthError.response?.status === 404) {
                    // Nếu chưa có health profile, sử dụng thông tin từ student
                    setHealthProfile({
                        studentName: studentRes.data.fullName,
                        dateOfBirth: studentRes.data.dateOfBirth,
                        gender: studentRes.data.gender,
                        grade: studentRes.data.grade,
                        className: studentRes.data.studentClass,
                        city: studentRes.data.city,
                        district: studentRes.data.district
                    });
                } else {
                    throw healthError;
                }
            }
            
        } catch (err) {
            console.error('Error fetching student profile:', err);
            if (err.response?.status === 404) {
                setError('Không tìm thấy thông tin học sinh. Vui lòng liên hệ admin.');
            } else {
                setError('Có lỗi khi tải thông tin hồ sơ sức khỏe.');
            }
        } finally {
            setLoading(false);
        }
    };

    const fetchMedicalRequests = async () => {
        try {
            setLoadingMedical(true);
            const studentId = localStorage.getItem('studentId');
            if (studentId) {
                const response = await api.get(`/medical/student/${studentId}`);
                setMedicalRequests(response.data);
            }
        } catch (error) {
            console.error('Error fetching medical requests:', error);
        } finally {
            setLoadingMedical(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'Chưa cập nhật';
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('vi-VN');
        } catch (error) {
            return dateString;
        }
    };

    const getStatusBadge = (status) => {
        const statusConfig = {
            'PENDING': { variant: 'warning', text: 'Chờ duyệt' },
            'APPROVED': { variant: 'success', text: 'Đã duyệt' },
            'REJECTED': { variant: 'danger', text: 'Từ chối' },
            'COMPLETED': { variant: 'info', text: 'Đã hoàn thành' }
        };
        
        const config = statusConfig[status] || { variant: 'secondary', text: status };
        return <Badge bg={config.variant}>{config.text}</Badge>;
    };

    const renderPersonalInfo = () => (
        <Card className="mb-4">
            <Card.Header>
                <h5 className="mb-0">Thông tin cá nhân</h5>
            </Card.Header>
            <Card.Body>
                <Row>
                    <Col md={6}>
                        <div className="info-row">
                            <strong className="info-label">Họ và tên:</strong>
                            <span className="info-value">
                                {healthProfile?.studentName || student?.fullName}
                            </span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Mã học sinh:</strong>
                            <span className="info-value">{student?.code}</span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Lớp:</strong>
                            <span className="info-value">
                                {healthProfile?.className || student?.studentClass}
                            </span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Khối:</strong>
                            <span className="info-value">
                                {healthProfile?.grade || student?.grade}
                            </span>
                        </div>
                    </Col>
                    <Col md={6}>
                        <div className="info-row">
                            <strong className="info-label">Giới tính:</strong>
                            <span className="info-value">
                                {healthProfile?.gender === 'MALE' ? 'Nam' : 
                                 healthProfile?.gender === 'FEMALE' ? 'Nữ' : 
                                 student?.gender === 'MALE' ? 'Nam' :
                                 student?.gender === 'FEMALE' ? 'Nữ' : 'Khác'}
                            </span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Ngày sinh:</strong>
                            <span className="info-value">
                                {formatDate(healthProfile?.dateOfBirth || student?.dateOfBirth)}
                            </span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Địa chỉ:</strong>
                            <span className="info-value">
                                {healthProfile?.district || student?.district}, {healthProfile?.city || student?.city}
                            </span>
                        </div>
                        <div className="info-row">
                            <strong className="info-label">Email:</strong>
                            <span className="info-value">{student?.email}</span>
                        </div>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    );

    const renderHealthProfile = () => (
        <>
            <Row>
                <Col md={6}>
                    <Card className="mb-4">
                        <Card.Header className="allergies-header">
                            <h5 className="mb-0">Dị ứng & Bệnh mãn tính</h5>
                        </Card.Header>
                        <Card.Body>
                            <div className="mb-3">
                                <strong>Dị ứng:</strong>
                                <div className="mt-2">
                                    {healthProfile?.allergies && healthProfile.allergies !== 'Chưa có thông tin từ phụ huynh' ? (
                                        <div className="data-box">
                                            {healthProfile.allergies}
                                        </div>
                                    ) : (
                                        <span className="no-data">
                                            {healthProfile?.allergies || 'Không có dị ứng'}
                                        </span>
                                    )}
                                </div>
                            </div>
                            <div>
                                <strong>Bệnh mãn tính:</strong>
                                <div className="mt-2">
                                    {healthProfile?.chronicDiseases && healthProfile.chronicDiseases !== 'Chưa có thông tin từ phụ huynh' ? (
                                        <div className="data-box">
                                            {healthProfile.chronicDiseases}
                                        </div>
                                    ) : (
                                        <span className="no-data">
                                            {healthProfile?.chronicDiseases || 'Không có bệnh mãn tính'}
                                        </span>
                                    )}
                                </div>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={6}>
                    <Card className="mb-4">
                        <Card.Header className="medical-history-header">
                            <h5 className="mb-0">Lịch sử y tế</h5>
                        </Card.Header>
                        <Card.Body>
                            <div className="mb-3">
                                <strong>Tiền sử bệnh:</strong>
                                <div className="mt-2">
                                    {healthProfile?.medicalHistory && healthProfile.medicalHistory !== 'Chưa có thông tin từ phụ huynh' ? (
                                        <div className="data-box">
                                            {healthProfile.medicalHistory}
                                        </div>
                                    ) : (
                                        <span className="no-data">
                                            {healthProfile?.medicalHistory || 'Không có tiền sử bệnh'}
                                        </span>
                                    )}
                                </div>
                            </div>
                            <div>
                                <strong>Lịch sử tiêm chủng:</strong>
                                <div className="mt-2">
                                    {healthProfile?.vaccinationHistory && healthProfile.vaccinationHistory !== 'Chưa có thông tin từ phụ huynh' ? (
                                        <div className="data-box">
                                            {healthProfile.vaccinationHistory}
                                        </div>
                                    ) : (
                                        <span className="no-data">
                                            {healthProfile?.vaccinationHistory || 'Chưa có thông tin tiêm chủng'}
                                        </span>
                                    )}
                                </div>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row>
                <Col md={6}>
                    <Card className="mb-4">
                        <Card.Header className="vision-header">
                            <h5 className="mb-0">Thị giác</h5>
                        </Card.Header>
                        <Card.Body>
                            {healthProfile?.visionDetails && healthProfile.visionDetails !== 'Chưa có thông tin từ phụ huynh' ? (
                                <div className="data-box">
                                    {healthProfile.visionDetails}
                                </div>
                            ) : (
                                <span className="no-data">
                                    {healthProfile?.visionDetails || 'Chưa có thông tin thị giác'}
                                </span>
                            )}
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={6}>
                    <Card className="mb-4">
                        <Card.Header className="hearing-header">
                            <h5 className="mb-0">Thính giác</h5>
                        </Card.Header>
                        <Card.Body>
                            {healthProfile?.hearingDetails && healthProfile.hearingDetails !== 'Chưa có thông tin từ phụ huynh' ? (
                                <div className="data-box">
                                    {healthProfile.hearingDetails}
                                </div>
                            ) : (
                                <span className="no-data">
                                    {healthProfile?.hearingDetails || 'Chưa có thông tin thính giác'}
                                </span>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </>
    );

    const renderMedicationRequests = () => (
        <Card className="mb-4">
            <Card.Header>
                <h5 className="mb-0">Yêu cầu thuốc từ phụ huynh</h5>
            </Card.Header>
            <Card.Body>
                {loadingMedical ? (
                    <div className="text-center py-4">
                        <Spinner animation="border" />
                        <p className="mt-2">Đang tải thông tin...</p>
                    </div>
                ) : medicalRequests.length === 0 ? (
                    <Alert variant="info">
                        <Alert.Heading>Thông báo</Alert.Heading>
                        <p>Hiện tại chưa có yêu cầu thuốc nào từ phụ huynh.</p>
                        <hr />
                        <p className="mb-0">
                            Khi phụ huynh gửi yêu cầu thuốc, thông tin sẽ được hiển thị tại đây.
                        </p>
                    </Alert>
                ) : (
                    <Table striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>Tên thuốc</th>
                                <th>Liều lượng</th>
                                <th>Ghi chú</th>
                                <th>Ngày gửi</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            {medicalRequests.map((request) => (
                                <tr key={request.id}>
                                    <td>{request.medicalName}</td>
                                    <td>{request.dosage}</td>
                                    <td>{request.note || 'Không có'}</td>
                                    <td>{formatDate(request.requestDate)}</td>
                                    <td>{getStatusBadge(request.status)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                )}
            </Card.Body>
        </Card>
    );

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center min-height-400">
                <div className="text-center">
                    <Spinner animation="border" variant="primary" />
                    <p className="mt-3">Đang tải thông tin hồ sơ sức khỏe...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mt-4">
                <Alert variant="warning">
                    <Alert.Heading>Thông báo</Alert.Heading>
                    <p>{error}</p>
                    <hr />
                    <p className="mb-0">
                        Nếu bạn cần hỗ trợ, vui lòng liên hệ với nhân viên y tế trường học.
                    </p>
                </Alert>
            </div>
        );
    }

    return (
        <div className="student-page">
            <div className="container-fluid mt-4">
                <Row>
                    {/* Sidebar trái */}
                    <Col md={3} className="parent-sidebar">
                        <Card>
                            <Card.Body>
                                {student ? (
                                    <div className="mb-3">
                                        <div><strong>Họ tên:</strong> {student.fullName}</div>
                                        <div><strong>Mã học sinh:</strong> {student.code}</div>
                                        <div><strong>Lớp:</strong> {student.studentClass}</div>
                                        <div><strong>Email:</strong> {student.email}</div>
                                    </div>
                                ) : <div>Không có thông tin học sinh</div>}
                                <ListGroup variant="flush">
                                    <ListGroup.Item action active={key === 'personal'} onClick={() => setKey('personal')}>
                                        Thông tin cá nhân
                                    </ListGroup.Item>
                                    <ListGroup.Item action active={key === 'health'} onClick={() => setKey('health')}>
                                        Hồ sơ sức khỏe
                                    </ListGroup.Item>
                                    <ListGroup.Item action active={key === 'medication'} onClick={() => setKey('medication')}>
                                        Phụ huynh gửi thuốc
                                    </ListGroup.Item>
                                </ListGroup>
                            </Card.Body>
                        </Card>
                    </Col>

                    {/* Nội dung chính */}
                    <Col md={9}>
                        <div className="parent-content">
                            {key === 'personal' && renderPersonalInfo()}
                            {key === 'health' && renderHealthProfile()}
                            {key === 'medication' && renderMedicationRequests()}
                        </div>
                    </Col>
                </Row>
            </div>
        </div>
    );
};

export default StudentPage;