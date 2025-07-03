import React, { useState, useEffect } from 'react';
import { Card, Button, Table, Badge, Form, Row, Col, Modal } from 'react-bootstrap';
import { FaPlus, FaEdit, FaEye, FaFilter } from 'react-icons/fa';
import { toast } from 'react-toastify';
import CreateIncidentModal from './CreateIncidentModal';
import ViewIncidentModal from './ViewIncidentModal';
import api from '../../../config/api';
const ManageHealthIncidents = () => {
  const [incidents, setIncidents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreate, setShowCreate] = useState(false);
  const [showView, setShowView] = useState(false);
  const [selectedIncident, setSelectedIncident] = useState(null);
  const [filters, setFilters] = useState({
    status: '',
    type: '',
    dateFrom: '',
    dateTo: ''
  });

  useEffect(() => {
    fetchIncidents();
  }, []);

  const fetchIncidents = async () => {
    try {
      setLoading(true);
      // Gọi API backend lấy danh sách sự kiện y tế
      const response = await api.get('/api/health-incidents');
      setIncidents(response.data);
    } catch (error) {
      toast.error('Lỗi khi tải danh sách sự kiện y tế');
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'pending':
        return <Badge bg="warning">Chờ xử lý</Badge>;
      case 'treating':
        return <Badge bg="info">Đang điều trị</Badge>;
      case 'resolved':
        return <Badge bg="success">Đã xử lý</Badge>;
      case 'referred':
        return <Badge bg="primary">Chuyển viện</Badge>;
      default:
        return <Badge bg="secondary">Không xác định</Badge>;
    }
  };

  const getPriorityBadge = (priority) => {
    switch (priority) {
      case 'high':
        return <Badge bg="danger">Cao</Badge>;
      case 'medium':
        return <Badge bg="warning">Trung bình</Badge>;
      case 'low':
        return <Badge bg="success">Thấp</Badge>;
      default:
        return <Badge bg="secondary">Không xác định</Badge>;
    }
  };

  const handleIncidentCreated = () => {
    fetchIncidents();
    toast.success('Tạo sự kiện y tế thành công!');
  };

  const handleViewIncident = (incident) => {
    setSelectedIncident(incident);
    setShowView(true);
  };

  const filteredIncidents = incidents.filter(incident => {
    if (filters.status && incident.status !== filters.status) return false;
    if (filters.type && !incident.type.toLowerCase().includes(filters.type.toLowerCase())) return false;
    return true;
  });

  if (loading) return <div>Đang tải...</div>;

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý sự kiện y tế</h2>
        <Button variant="primary" onClick={() => setShowCreate(true)}>
          <FaPlus className="me-2" />
          Báo cáo sự kiện mới
        </Button>
      </div>

      {/* Bộ lọc */}
      <Card className="mb-4">
        <Card.Header>
          <FaFilter className="me-2" />
          Bộ lọc
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={3}>
              <Form.Group>
                <Form.Label>Trạng thái</Form.Label>
                <Form.Select 
                  value={filters.status} 
                  onChange={(e) => setFilters({...filters, status: e.target.value})}
                >
                  <option value="">Tất cả</option>
                  <option value="pending">Chờ xử lý</option>
                  <option value="treating">Đang điều trị</option>
                  <option value="resolved">Đã xử lý</option>
                  <option value="referred">Chuyển viện</option>
                </Form.Select>
              </Form.Group>
            </Col>
            <Col md={3}>
              <Form.Group>
                <Form.Label>Loại sự kiện</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Nhập loại sự kiện..."
                  value={filters.type}
                  onChange={(e) => setFilters({...filters, type: e.target.value})}
                />
              </Form.Group>
            </Col>
            <Col md={3}>
              <Form.Group>
                <Form.Label>Từ ngày</Form.Label>
                <Form.Control
                  type="date"
                  value={filters.dateFrom}
                  onChange={(e) => setFilters({...filters, dateFrom: e.target.value})}
                />
              </Form.Group>
            </Col>
            <Col md={3}>
              <Form.Group>
                <Form.Label>Đến ngày</Form.Label>
                <Form.Control
                  type="date"
                  value={filters.dateTo}
                  onChange={(e) => setFilters({...filters, dateTo: e.target.value})}
                />
              </Form.Group>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* Bảng danh sách */}
      <Card>
        <Card.Header>
          <h5 className="mb-0">Danh sách sự kiện y tế ({filteredIncidents.length})</h5>
        </Card.Header>
        <Card.Body>
          <Table responsive hover>
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>Học sinh</th>
                <th>Lớp</th>
                <th>Loại sự kiện</th>
                <th>Mức độ</th>
                <th>Trạng thái</th>
                <th>Người báo cáo</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {filteredIncidents.map((incident) => (
                <tr key={incident.id}>
                  <td>{new Date(incident.reportedAt).toLocaleString('vi-VN')}</td>
                  <td>
                    <div>
                      <strong>{incident.studentName}</strong>
                      <br />
                      <small className="text-muted">{incident.studentCode}</small>
                    </div>
                  </td>
                  <td>{incident.studentClass}</td>
                  <td>{incident.type}</td>
                  <td>{getPriorityBadge(incident.priority)}</td>
                  <td>{getStatusBadge(incident.status)}</td>
                  <td>{incident.reportedBy}</td>
                  <td>
                    <Button
                      variant="outline-primary"
                      size="sm"
                      className="me-2"
                      onClick={() => handleViewIncident(incident)}
                    >
                      <FaEye />
                    </Button>
                    <Button
                      variant="outline-warning"
                      size="sm"
                      disabled={incident.status === 'resolved'}
                    >
                      <FaEdit />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Modals */}
      <CreateIncidentModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onIncidentCreated={handleIncidentCreated}
      />

      <ViewIncidentModal
        show={showView}
        onClose={() => setShowView(false)}
        incident={selectedIncident}
        onUpdate={fetchIncidents}
      />
    </div>
  );
};

export default ManageHealthIncidents; 