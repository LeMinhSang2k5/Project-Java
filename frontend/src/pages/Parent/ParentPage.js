import React, { useState, useEffect } from 'react';
import {Card, Button, Container, Table, Alert, Spinner, Row, Col, ListGroup, Badge } from 'react-bootstrap';
import NewHealthProfile from '../HealthProfile/NewHealthProfile';
import StudentListView from './StudentListView';
import SendMedicine from './SendMedicine';
import api from '../../config/api';
import { toast } from 'react-toastify';
import './ParentPage.scss';

const ParentPage = () => {
  // State cho các tab
  const [key, setKey] = useState('students');
  // State cho phiếu xác nhận
  const [confirmForms, setConfirmForms] = useState([]);
  const [loadingConfirm, setLoadingConfirm] = useState(false);
  // State cho kết quả tiêm chủng và lịch tư vấn
  const [results, setResults] = useState([]);
  const [loadingResults, setLoadingResults] = useState(false);
  // State cho thông báo
  const [alert, setAlert] = useState(null);
  // State cho thông tin phụ huynh
  const [parentInfo, setParentInfo] = useState(null);
  // State cho học sinh được chọn
  const [selectedStudentId, setSelectedStudentId] = useState(null);
  // State cho lịch tiêm chủng
  const [vaccinationSchedules, setVaccinationSchedules] = useState([]);
  const [loadingVaccination, setLoadingVaccination] = useState(false);
  const [medicalCheckupNotifications, setMedicalCheckupNotifications] = useState([]);
  const [loadingMedicalCheckup, setLoadingMedicalCheckup] = useState(false);

  useEffect(() => {
    const parentId = localStorage.getItem('parentId');
    if (parentId) {
      api.get(`/parent/${parentId}`)
        .then(res => setParentInfo(res.data))
        .catch(() => setParentInfo(null));
    }
  }, []);

  // Lấy danh sách phiếu xác nhận từ API
  useEffect(() => {
    if (key === 'confirmVaccination') {
      setLoadingConfirm(true);
      api.get('/parent/confirm-forms')
        .then(res => setConfirmForms(res.data))
        .catch(() => setAlert('Không thể tải danh sách phiếu xác nhận!'))
        .finally(() => setLoadingConfirm(false));
    }
  }, [key]);

  // Lấy kết quả tiêm chủng và lịch tư vấn
  useEffect(() => {
    if (key === 'results') {
      setLoadingResults(true);
      api.get('/parent/vaccination-results')
        .then(res => setResults(res.data))
        .catch(() => setAlert('Không thể tải kết quả tiêm chủng!'))
        .finally(() => setLoadingResults(false));
    }
  }, [key]);

  // Lấy lịch tiêm chủng cần xác nhận
  useEffect(() => {
    if (key === 'vaccinationSchedules') {
      setLoadingVaccination(true);
      const parentId = localStorage.getItem('parentId');
      if (parentId) {
        api.get(`/vaccination-schedules/pending-parent-consent/${parentId}`)
          .then(res => setVaccinationSchedules(res.data))
          .catch(() => setAlert('Không thể tải lịch tiêm chủng!'))
          .finally(() => setLoadingVaccination(false));
      } else {
        setVaccinationSchedules([]);
        setLoadingVaccination(false);
      }
    }
  }, [key]);

  useEffect(() => {
    if (key === 'medicalCheckupNotify') {
      setLoadingMedicalCheckup(true);
      const parentId = localStorage.getItem('parentId');
      if (parentId) {
        api.get(`/medical-checkup/notifications?parentId=${parentId}&status=PENDING`)
          .then(res => setMedicalCheckupNotifications(res.data))
          .catch(() => setAlert('Không thể tải phiếu kiểm tra y tế!'))
          .finally(() => setLoadingMedicalCheckup(false));
      } else {
        setMedicalCheckupNotifications([]);
        setLoadingMedicalCheckup(false);
      }
    }
  }, [key]);

  useEffect(() => {
    if (key === 'medicalCheckupResults') {
      setLoadingResults(true);
      const parentId = localStorage.getItem('parentId');
      api.get(`/medical-checkup/results?parentId=${parentId}`)
        .then(res => setResults(res.data))
        .catch(() => setAlert('Không thể tải kết quả kiểm tra y tế!'))
        .finally(() => setLoadingResults(false));
    }
  }, [key]);

  // Xác nhận phiếu tiêm chủng/kiểm tra y tế
  const handleConfirm = (formId) => {
    api.post(`/parent/confirm/${formId}`)
      .then(() => {
        setAlert('Xác nhận thành công!');
        setConfirmForms(forms => forms.filter(f => f.id !== formId));
      })
      .catch(() => setAlert('Xác nhận thất bại!'));
  };

  // Xác nhận lịch tiêm chủng
  const handleVaccinationConsent = async (scheduleId, consent) => {
    try {
      await api.put(`/vaccination-schedules/${scheduleId}/parent-consent`, {
        consent: consent
      });
      toast.success(consent === 'APPROVED' ? 'Đã đồng ý tiêm chủng!' : 'Đã từ chối tiêm chủng!');
      // Cập nhật lại danh sách
      const updatedSchedules = vaccinationSchedules.filter(schedule => schedule.id !== scheduleId);
      setVaccinationSchedules(updatedSchedules);
    } catch (error) {
      toast.error('Có lỗi xảy ra khi xác nhận!');
    }
  };

  const handleMedicalCheckupConsent = async (notificationId, status) => {
    try {
      await api.put(`/medical-checkup/notification/${notificationId}/consent`, status, {
        headers: { 'Content-Type': 'text/plain' }
      });
      toast.success(status === 'APPROVED' ? 'Đã đồng ý kiểm tra y tế!' : 'Đã từ chối kiểm tra y tế!');
      setMedicalCheckupNotifications(list => list.filter(n => n.id !== notificationId));
    } catch {
      toast.error('Có lỗi xảy ra khi xác nhận!');
    }
  };

  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return 'Chưa có';
    const date = new Date(dateTimeString);
    return date.toLocaleString('vi-VN');
  };

  const getConsentBadge = (consent) => {
    switch (consent) {
      case 'APPROVED':
        return <Badge bg="success">Đồng ý</Badge>;
      case 'REJECTED':
        return <Badge bg="danger">Từ chối</Badge>;
      case 'PENDING':
        return <Badge bg="warning">Chờ xác nhận</Badge>;
      default:
        return <Badge bg="secondary">Không xác định</Badge>;
    }
  };

  return (
    <Container fluid className="parent-page mt-4">
      <Row>
        {/* Sidebar trái */}
        <Col md={3} className="parent-sidebar">
          <Card>
            <Card.Body>
              {parentInfo ? (
                <div className="mb-3">
                  <div><strong>Họ tên:</strong> {parentInfo.fullName}</div>
                  <div><strong>Email:</strong> {parentInfo.email}</div>
                  <div><strong>SĐT:</strong> {parentInfo.phoneNumber}</div>
                  <div><strong>Giới tính:</strong> {parentInfo.gender === 'MALE' ? 'Nam' : parentInfo.gender === 'FEMALE' ? 'Nữ' : 'Khác'}</div>
                </div>
              ) : <div>Không có thông tin phụ huynh</div>}
              <ListGroup variant="flush">
                <ListGroup.Item action active={key==='students'} onClick={()=>setKey('students')}>
                  Hồ sơ sức khỏe học sinh</ListGroup.Item>
                <ListGroup.Item action active={key==='sendMedicine'} onClick={()=>setKey('sendMedicine')}>
                  Gửi thuốc cho học sinh</ListGroup.Item>
                <ListGroup.Item action active={key==='vaccinationSchedules'} onClick={()=>setKey('vaccinationSchedules')}>
                Xác nhận lịch tiêm chủng</ListGroup.Item>
                <ListGroup.Item action active={key==='medicalCheckupNotify'} onClick={()=>setKey('medicalCheckupNotify')}>
                  Xác nhận kiểm tra y tế
                </ListGroup.Item>
                <ListGroup.Item action active={key==='medicalCheckupResults'} onClick={()=>setKey('medicalCheckupResults')}>
                  Kết quả kiểm tra y tế
                </ListGroup.Item>
              </ListGroup>
            </Card.Body>
          </Card>
        </Col>
        {/* Nội dung dashboard phải */}
        <Col md={9}>
          {alert && <Alert variant="info" onClose={() => setAlert(null)} dismissible>{alert}</Alert>}
          <div className="parent-content">
            {key === 'students' && <StudentListView onStudentSelect={setSelectedStudentId} />}
            {key === 'sendMedicine' && <SendMedicine />}
            {key === 'vaccinationSchedules' && (
              <Card>
                <Card.Header>
                  <h5 className="mb-0">Lịch tiêm chủng cần xác nhận</h5>
                </Card.Header>
                <Card.Body>
                  {loadingVaccination ? (
                    <div className="text-center py-4">
                      <Spinner animation="border" />
                      <p className="mt-2">Đang tải lịch tiêm chủng...</p>
                    </div>
                  ) : vaccinationSchedules.length === 0 ? (
                    <Alert variant="info">
                      <Alert.Heading>Thông báo</Alert.Heading>
                      <p>Hiện tại không có lịch tiêm chủng nào cần xác nhận.</p>
                    </Alert>
                  ) : (
                    <Table striped bordered hover responsive>
                      <thead>
                        <tr>
                          <th>Học sinh</th>
                          <th>Vaccine</th>
                          <th>Ngày giờ tiêm</th>
                          <th>Ghi chú</th>
                          <th>Trạng thái</th>
                          <th>Hành động</th>
                        </tr>
                      </thead>
                      <tbody>
                        {vaccinationSchedules.map(schedule => (
                          <tr key={schedule.id}>
                            <td>
                              <div>
                                <strong>{schedule.studentName}</strong>
                                <br />
                                <small className="text-muted">{schedule.studentCode}</small>
                              </div>
                            </td>
                            <td>{schedule.vaccineName}</td>
                            <td>{formatDateTime(schedule.scheduledDateTime)}</td>
                            <td>{schedule.notes || 'Không có'}</td>
                            <td>{getConsentBadge(schedule.parentConsent)}</td>
                            <td>
                              <div className="d-flex gap-1">
                                <Button 
                                  variant="success" 
                                  size="sm" 
                                  onClick={() => handleVaccinationConsent(schedule.id, 'APPROVED')}
                                >
                                  Đồng ý
                                </Button>
                                <Button 
                                  variant="danger" 
                                  size="sm" 
                                  onClick={() => handleVaccinationConsent(schedule.id, 'REJECTED')}
                                >
                                  Từ chối
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
            )}
            {key === 'confirmVaccination' && (
              <Card>
                <Card.Body>
                  {loadingConfirm ? <Spinner animation="border" /> : (
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Học sinh</th>
                          <th>Loại phiếu</th>
                          <th>Nội dung</th>
                          <th>Ngày gửi</th>
                          <th>Hành động</th>
                        </tr>
                      </thead>
                      <tbody>
                        {confirmForms.length === 0 ? (
                          <tr><td colSpan={5} className="text-center">Không có phiếu cần xác nhận</td></tr>
                        ) : confirmForms.map(form => (
                          <tr key={form.id}>
                            <td>{form.studentName}</td>
                            <td>{form.type}</td>
                            <td>{form.content}</td>
                            <td>{form.sentDate}</td>
                            <td>
                              <Button variant="success" size="sm" onClick={() => handleConfirm(form.id)}>Xác nhận</Button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  )}
                </Card.Body>
              </Card>
            )}
            {key === 'results' && (
              <Card>
                <Card.Body>
                  {loadingResults ? <Spinner animation="border" /> : (
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Học sinh</th>
                          <th>Kết quả tiêm chủng</th>
                          <th>Ngày tiêm</th>
                          <th>Ghi chú</th>
                          <th>Lịch hẹn tư vấn</th>
                        </tr>
                      </thead>
                      <tbody>
                        {results.length === 0 ? (
                          <tr><td colSpan={5} className="text-center">Chưa có kết quả nào</td></tr>
                        ) : results.map(result => (
                          <tr key={result.id}>
                            <td>{result.studentName}</td>
                            <td>{result.vaccinationResult}</td>
                            <td>{result.vaccinationDate}</td>
                            <td>{result.notes}</td>
                            <td>{result.consultationDate ? result.consultationDate : 'Không có'}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  )}
                </Card.Body>
              </Card>
            )}
            {key === 'medicalCheckupNotify' && (
              <Card>
                <Card.Header>
                  <h5 className="mb-0">Phiếu kiểm tra y tế cần xác nhận</h5>
                </Card.Header>
                <Card.Body>
                  {loadingMedicalCheckup ? (
                    <div className="text-center py-4">
                      <Spinner animation="border" />
                      <p className="mt-2">Đang tải phiếu kiểm tra y tế...</p>
                    </div>
                  ) : medicalCheckupNotifications.length === 0 ? (
                    <Alert variant="info">
                      <Alert.Heading>Thông báo</Alert.Heading>
                      <p>Hiện tại không có phiếu kiểm tra y tế nào cần xác nhận.</p>
                    </Alert>
                  ) : (
                    <Table striped bordered hover responsive>
                      <thead>
                        <tr>
                          <th>Học sinh</th>
                          <th>Nội dung</th>
                          <th>Ngày kiểm tra</th>
                          <th>Trạng thái</th>
                          <th>Hành động</th>
                        </tr>
                      </thead>
                      <tbody>
                        {medicalCheckupNotifications.map((n, idx) => (
                          <tr key={n.id || idx}>
                            <td>{n.studentName || n.studentId}</td>
                            <td>{n.content}</td>
                            <td>{n.scheduledDate ? new Date(n.scheduledDate).toLocaleString('vi-VN') : ''}</td>
                            <td>{n.status === 'PENDING' ? 'Chờ xác nhận' : n.status}</td>
                            <td>
                              {n.id ? (
                                <>
                                  <Button
                                    variant="success"
                                    size="sm"
                                    onClick={() => handleMedicalCheckupConsent(n.id, 'APPROVED')}
                                  >Đồng ý</Button>
                                  {' '}
                                  <Button
                                    variant="danger"
                                    size="sm"
                                    onClick={() => handleMedicalCheckupConsent(n.id, 'REJECTED')}
                                  >Từ chối</Button>
                                </>
                              ) : (
                                <span className="text-danger">Không xác định ID</span>
                              )}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  )}
                </Card.Body>
              </Card>
            )}
            {key === 'medicalCheckupResults' && (
              <Card>
                <Card.Header>
                  <h5 className="mb-0">Kết quả kiểm tra y tế của học sinh</h5>
                </Card.Header>
                <Card.Body>
                  {loadingResults ? (
                    <Spinner animation="border" />
                  ) : results.length === 0 ? (
                    <Alert variant="info">Chưa có kết quả kiểm tra y tế nào.</Alert>
                  ) : (
                    <Table striped bordered hover>
                      <thead>
                        <tr>
                          <th>Học sinh</th>
                          <th>Ngày kiểm tra</th>
                          <th>Kết quả</th>
                          <th>Ghi chú</th>
                          <th>Bất thường</th>
                          <th>Xác nhận học sinh</th>
                        </tr>
                      </thead>
                      <tbody>
                        {results.map(r => (
                          <tr key={r.id}>
                            <td>{r.studentName || r.studentId}</td>
                            <td>{r.checkupDate ? new Date(r.checkupDate).toLocaleString('vi-VN') : ''}</td>
                            <td>{r.result}</td>
                            <td>{r.notes}</td>
                            <td>{r.abnormal ? 'Có' : 'Không'}</td>
                            <td>{r.studentConfirmation ? 'Đã xác nhận' : 'Chưa xác nhận'}</td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  )}
                </Card.Body>
              </Card>
            )}
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default ParentPage; 