import React, { useState, useEffect } from 'react';
import { Card, Button, Container, Table, Alert, Spinner, Row, Col, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../../config/api';
import './StudentListView.scss';

const StudentListView = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    setLoading(true);
    try {
      const parentId = localStorage.getItem('parentId');
      if (!parentId) {
        setAlert('Không tìm thấy thông tin phụ huynh!');
        return;
      }

      const response = await api.get(`/students/by-parent/${parentId}`);
      setStudents(response.data);
      setAlert(null);
    } catch (error) {
      console.error('Error loading students:', error);
      setAlert('Không thể tải danh sách học sinh!');
    } finally {
      setLoading(false);
    }
  };


  const handleViewHealthProfile = (studentId) => {
    navigate(`/health-profile/${studentId}`);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('vi-VN');
  };

  const getGenderText = (gender) => {
    switch (gender) {
      case 'MALE': return 'Nam';
      case 'FEMALE': return 'Nữ';
      case 'OTHER': return 'Khác';
      default: return 'N/A';
    }
  };

  return (
    <Container fluid className="student-list-page mt-4">
      <Row>
        <Col>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>Danh sách học sinh</h2>
          </div>

          {alert && <Alert variant="info" onClose={() => setAlert(null)} dismissible>{alert}</Alert>}

          <Card>
            <Card.Body>
              {loading ? (
                <div className="text-center py-4">
                  <Spinner animation="border" />
                  <p className="mt-2">Đang tải danh sách học sinh...</p>
                </div>
              ) : students.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-users fa-3x text-muted mb-3"></i>
                  <h5>Chưa có học sinh nào</h5>
                </div>
              ) : (
                <Table striped bordered hover responsive>
                  <thead>
                    <tr>
                      <th>Mã học sinh</th>
                      <th>Họ và tên</th>
                      <th>Lớp</th>
                      <th>Ngày sinh</th>
                      <th>Giới tính</th>
                      <th>Email</th>
                      <th>Hồ sơ sức khỏe</th>
                      <th>Hành động</th>
                    </tr>
                  </thead>
                  <tbody>
                    {students.map((student) => (
                      <tr key={student.id}>
                        <td>
                          <Badge bg="secondary">{student.code}</Badge>
                        </td>
                        <td>
                          <strong>{student.fullName}</strong>
                        </td>
                        <td>{student.studentClass}</td>
                        <td>{formatDate(student.dateOfBirth)}</td>
                        <td>{getGenderText(student.gender)}</td>
                        <td>{student.email}</td>
                        <td>
                          {student.healthProfile ? (
                            <Badge bg="success">Đã có</Badge>
                          ) : (
                            <Badge bg="warning">Chưa có</Badge>
                          )}
                        </td>
                        <td>
                          <div className="d-flex gap-2">
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleViewHealthProfile(student.id)}
                            >
                              <i className="fas fa-eye me-1"></i>
                              Xem hồ sơ
                            </Button>
                            <Button
                              variant={student.healthProfile ? "outline-warning" : "outline-success"}
                              size="sm"
                              onClick={() => navigate(`/new-health-profile/${student.id}`)}
                            >
                              <i className={`fas ${student.healthProfile ? 'fa-edit' : 'fa-plus'} me-1`}></i>
                              {student.healthProfile ? 'Cập nhật hồ sơ' : 'Tạo hồ sơ'}
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default StudentListView; 