import React, { useState, useEffect } from 'react';
import { Tabs, Tab, Card, Button, Container, Table, Alert, Spinner, Row, Col, ListGroup } from 'react-bootstrap';
import NewHealthProfile from '../HealthProfile/NewHealthProfile';
import api from '../../config/api';
import './ParentPage.scss';

const ParentPage = () => {
  // State cho các tab
  const [key, setKey] = useState('newProfile');
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

  // Xác nhận phiếu tiêm chủng/kiểm tra y tế
  const handleConfirm = (formId) => {
    api.post(`/parent/confirm/${formId}`)
      .then(() => {
        setAlert('Xác nhận thành công!');
        setConfirmForms(forms => forms.filter(f => f.id !== formId));
      })
      .catch(() => setAlert('Xác nhận thất bại!'));
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
                <ListGroup.Item action active={key==='newProfile'} onClick={()=>setKey('newProfile')}>Tạo mới hồ sơ sức khỏe</ListGroup.Item>
                <ListGroup.Item action active={key==='confirmVaccination'} onClick={()=>setKey('confirmVaccination')}>Xác nhận tiêm chủng/kiểm tra y tế</ListGroup.Item>
                <ListGroup.Item action active={key==='results'} onClick={()=>setKey('results')}>Kết quả tiêm chủng & Lịch tư vấn</ListGroup.Item>
              </ListGroup>
            </Card.Body>
          </Card>
        </Col>
        {/* Nội dung dashboard phải */}
        <Col md={9}>
          <h2 className="mb-4">Trang dành cho Phụ huynh</h2>
          {alert && <Alert variant="info" onClose={() => setAlert(null)} dismissible>{alert}</Alert>}
          <div className="parent-content">
            {key === 'newProfile' && (
              <Card className="mb-3">
                <Card.Body>
                  <NewHealthProfile />
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
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default ParentPage; 