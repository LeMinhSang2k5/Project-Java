import React, { useState, useEffect } from 'react';
import { Tabs, Tab, Container, Card, Spinner, Alert, Table } from 'react-bootstrap';
import MedicalCheckupNotify from './MedicalCheckupNotify';
import MedicalCheckupResult from './MedicalCheckupResult';
import api from '../../../config/api';

const MedicalCheckup = () => {
  const [key, setKey] = useState('notify');
  const [approvedCheckupList, setApprovedCheckupList] = useState([]);
  const [loadingApprovedCheckup, setLoadingApprovedCheckup] = useState(false);

  useEffect(() => {
    if (key === 'confirmed') {
      setLoadingApprovedCheckup(true);
      api.get('/medical-checkup/notifications?status=APPROVED')
        .then(res => setApprovedCheckupList(res.data))
        .catch(() => alert('Không thể tải danh sách đã xác nhận!'))
        .finally(() => setLoadingApprovedCheckup(false));
    }
  }, [key]);

  return (
    <Container className="mt-4">
      <Card>
        <Card.Header>
          <h4>Quản lý kiểm tra y tế học sinh</h4>
        </Card.Header>
        <Card.Body>
          <Tabs
            id="medical-checkup-tabs"
            activeKey={key}
            onSelect={(k) => setKey(k)}
            className="mb-3"
          >
            <Tab eventKey="notify" title="Gửi thông báo kiểm tra y tế">
              <MedicalCheckupNotify />
            </Tab>
            <Tab eventKey="confirmed" title="Danh sách học sinh đã xác nhận">
              {loadingApprovedCheckup ? (
                <div className="text-center py-4">
                  <Spinner animation="border" />
                  <p className="mt-2">Đang tải danh sách...</p>
                </div>
              ) : approvedCheckupList.length === 0 ? (
                <Alert variant="info">
                  <Alert.Heading>Thông báo</Alert.Heading>
                  <p>Hiện tại không có học sinh nào đã xác nhận kiểm tra y tế.</p>
                </Alert>
              ) : (
                <Table striped bordered hover responsive>
                  <thead>
                    <tr>
                      <th>Học sinh</th>
                      <th>Nội dung</th>
                      <th>Ngày kiểm tra</th>
                      <th>Trạng thái</th>
                    </tr>
                  </thead>
                  <tbody>
                    {approvedCheckupList.map((n, idx) => (
                      <tr key={n.id || idx}>
                        <td>{n.studentName || n.studentId}</td>
                        <td>{n.content}</td>
                        <td>{n.scheduledDate ? new Date(n.scheduledDate).toLocaleString('vi-VN') : ''}</td>
                        <td>Đã phụ huynh đồng ý</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Tab>
            <Tab eventKey="result" title="Ghi nhận kết quả kiểm tra">
              <MedicalCheckupResult />
            </Tab>
            <Tab eventKey="history" title="Lịch sử kiểm tra y tế">
              <MedicalCheckupHistory active={key === 'history'} />
            </Tab>
          </Tabs>
        </Card.Body>
      </Card>
    </Container>
  );
};

function MedicalCheckupHistory({ active }) {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    if (active) {
      setLoading(true);
      api.get('/medical-checkup/notifications')
        .then(res => setHistory(res.data))
        .catch(() => setHistory([]))
        .finally(() => setLoading(false));
    }
  }, [active]);

  function getStatusLabel(status) {
    switch (status) {
      case 'APPROVED':
        return 'Đã đồng ý';
      case 'REJECTED':
        return 'Đã từ chối';
      case 'PENDING':
        return 'Chờ xác nhận';
      default:
        return status;
    }
  }

  function getStudentConfirmationLabel(confirmed) {
    if (confirmed === true) return 'Đã xác nhận';
    if (confirmed === false) return 'Chưa xác nhận';
    return '';
  }

  function getAbnormalLabel(abnormal) {
    if (abnormal === true) return 'Có';
    if (abnormal === false) return 'Không';
    return '';
  }

  return (
    <div>
      {loading ? 'Đang tải...' : history.length === 0 ? 'Chưa có lịch sử kiểm tra y tế.' : (
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>Học sinh</th>
              <th>Ngày kiểm tra</th>
              <th>Trạng thái phụ huynh</th>
              <th>Ghi chú</th>
            </tr>
          </thead>
          <tbody>
            {history.map(n => (
              <tr key={n.id}>
                <td>{n.studentName || n.studentId}</td>
                <td>{n.scheduledDate ? new Date(n.scheduledDate).toLocaleString('vi-VN') : ''}</td>
                <td>{getStatusLabel(n.status)}</td>
                <td>{n.content}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default MedicalCheckup; 