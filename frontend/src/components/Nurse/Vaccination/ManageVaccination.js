import React, { useState, useEffect } from 'react';
import { Table, Button, Badge, Card, Row, Col, Form, Alert, Spinner } from 'react-bootstrap';
import { FaPlus, FaEdit, FaTrash, FaEye, FaCheck, FaTimes } from 'react-icons/fa';
import { toast } from 'react-toastify';
import api from '../../../config/api';
import ModalCreateVaccination from './ModalCreateVaccination';
import ModalEditVaccination from './ModalEditVaccination';
import ModalDeleteVaccination from './ModalDeleteVaccination';

const ManageVaccination = () => {
  const [vaccinations, setVaccinations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  const [selectedVaccination, setSelectedVaccination] = useState(null);
  const [filterStatus, setFilterStatus] = useState('all');

  useEffect(() => {
    fetchVaccinations();
  }, []);

  const fetchVaccinations = async () => {
    try {
      setLoading(true);
      const response = await api.get('/vaccination-schedules');
      setVaccinations(response.data);
    } catch (error) {
      console.error('Lỗi khi tải danh sách lịch tiêm chủng:', error);
      toast.error('Lỗi khi tải danh sách lịch tiêm chủng');
    } finally {
      setLoading(false);
    }
  };

  const handleVaccinationCreated = () => {
    fetchVaccinations();
  };

  const handleVaccinationUpdated = () => {
    fetchVaccinations();
  };

  const handleVaccinationDeleted = () => {
    fetchVaccinations();
  };

  const getStatusBadge = (vaccination) => {
    if (vaccination.isVaccinated) {
      return <Badge bg="success">Đã tiêm</Badge>;
    }
    
    if (vaccination.parentConsent === 'REJECTED') {
      return <Badge bg="danger">Phụ huynh từ chối</Badge>;
    }
    
    if (vaccination.parentConsent === 'APPROVED') {
      return <Badge bg="warning">Chờ học sinh xác nhận</Badge>;
    }
    
    if (vaccination.parentConsent === 'PENDING') {
      return <Badge bg="info">Chờ phụ huynh xác nhận</Badge>;
    }
    
    return <Badge bg="secondary">Không xác định</Badge>;
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

  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return 'Chưa có';
    const date = new Date(dateTimeString);
    return date.toLocaleString('vi-VN');
  };

  const filteredVaccinations = vaccinations.filter(vaccination => {
    if (filterStatus === 'all') return true;
    if (filterStatus === 'pending') return vaccination.parentConsent === 'PENDING';
    if (filterStatus === 'approved') return vaccination.parentConsent === 'APPROVED';
    if (filterStatus === 'rejected') return vaccination.parentConsent === 'REJECTED';
    if (filterStatus === 'vaccinated') return vaccination.isVaccinated;
    if (filterStatus === 'not-vaccinated') return !vaccination.isVaccinated;
    return true;
  });

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý lịch tiêm chủng</h2>
        <Button variant="success" onClick={() => setShowCreate(true)}>
          <FaPlus className="me-2" /> Tạo lịch tiêm mới
        </Button>
      </div>

      <Row className="mb-3">
        <Col md={6}>
          <Form.Select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
          >
            <option value="all">Tất cả</option>
            <option value="pending">Chờ phụ huynh xác nhận</option>
            <option value="approved">Phụ huynh đã đồng ý</option>
            <option value="rejected">Phụ huynh từ chối</option>
            <option value="vaccinated">Đã tiêm</option>
            <option value="not-vaccinated">Chưa tiêm</option>
          </Form.Select>
        </Col>
        <Col md={6} className="text-end">
          <Badge bg="info" className="me-2">
            Tổng: {vaccinations.length}
          </Badge>
          <Badge bg="warning" className="me-2">
            Chờ xác nhận: {vaccinations.filter(v => v.parentConsent === 'PENDING').length}
          </Badge>
          <Badge bg="success">
            Đã tiêm: {vaccinations.filter(v => v.isVaccinated).length}
          </Badge>
        </Col>
      </Row>

      <Card>
        <Card.Body>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Học sinh</th>
                <th>Vaccine</th>
                <th>Ngày giờ tiêm</th>
                <th>Trạng thái</th>
                <th>Xác nhận phụ huynh</th>
                <th>Xác nhận học sinh</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {filteredVaccinations.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center">
                    Không có lịch tiêm chủng nào
                  </td>
                </tr>
              ) : (
                filteredVaccinations.map((vaccination) => (
                  <tr key={vaccination.id}>
                    <td>
                      <div>
                        <strong>{vaccination.studentName}</strong>
                        <br />
                        <small className="text-muted">{vaccination.studentCode}</small>
                      </div>
                    </td>
                    <td>{vaccination.vaccineName}</td>
                    <td>{formatDateTime(vaccination.scheduledDateTime)}</td>
                    <td>{getStatusBadge(vaccination)}</td>
                    <td>
                      {getConsentBadge(vaccination.parentConsent)}
                      {vaccination.parentConsentDate && (
                        <>
                          <br />
                          <small className="text-muted">
                            {formatDateTime(vaccination.parentConsentDate)}
                          </small>
                        </>
                      )}
                    </td>
                    <td>
                      {vaccination.studentConfirmation ? (
                        <Badge bg="success">Đã xác nhận</Badge>
                      ) : (
                        <Badge bg="secondary">Chưa xác nhận</Badge>
                      )}
                      {vaccination.studentConfirmationDate && (
                        <>
                          <br />
                          <small className="text-muted">
                            {formatDateTime(vaccination.studentConfirmationDate)}
                          </small>
                        </>
                      )}
                    </td>
                    <td>
                      <div className="d-flex gap-1">
                        <Button
                          variant="info"
                          size="sm"
                          onClick={() => {
                            setSelectedVaccination(vaccination);
                            setShowEdit(true);
                          }}
                          title="Chỉnh sửa"
                        >
                          <FaEdit />
                        </Button>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => {
                            setSelectedVaccination(vaccination);
                            setShowDelete(true);
                          }}
                          title="Xóa"
                        >
                          <FaTrash />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Modals */}
      <ModalCreateVaccination
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onVaccinationCreated={handleVaccinationCreated}
      />

      <ModalEditVaccination
        show={showEdit}
        onClose={() => setShowEdit(false)}
        vaccination={selectedVaccination}
        onVaccinationUpdated={handleVaccinationUpdated}
      />

      <ModalDeleteVaccination
        show={showDelete}
        onClose={() => setShowDelete(false)}
        vaccination={selectedVaccination}
        onVaccinationDeleted={handleVaccinationDeleted}
      />
    </div>
  );
};

export default ManageVaccination; 