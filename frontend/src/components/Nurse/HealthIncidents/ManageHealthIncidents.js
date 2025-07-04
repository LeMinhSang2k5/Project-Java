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

  useEffect(() => {
    fetchIncidents();
  }, []);

  const fetchIncidents = async () => {
    try {
      setLoading(true);
      // Gọi API backend lấy danh sách sự kiện y tế
      const response = await api.get('/health-incidents');
      setIncidents(response.data);
    } catch (error) {
      toast.error('Lỗi khi tải danh sách sự kiện y tế');
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'REPORTED':
        return <Badge bg="warning">Đã báo cáo</Badge>;
      case 'MONITORING':
        return <Badge bg="info">Đang theo dõi</Badge>;
      case 'PARENT_NOTIFIED':
        return <Badge bg="primary">Đã báo phụ huynh</Badge>;
      case 'RESOLVED':
        return <Badge bg="success">Đã xử lý</Badge>;
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

      {/* Bảng danh sách */}
      <Card>
        <Card.Header>
          <h5 className="mb-0">Danh sách sự kiện y tế ({incidents.length})</h5>
        </Card.Header>
        <Card.Body>
          <Table responsive hover>
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>Học sinh (ID)</th>
                <th>Người báo cáo (ID)</th>
                <th>Loại sự kiện</th>
                <th>Mô tả</th>
                <th>Hành động đã xử lý</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {incidents.map((incident) => (
                <tr key={incident.id}>
                  <td>{incident.incidentTime ? new Date(incident.incidentTime).toLocaleString('vi-VN') : ''}</td>
                  <td>{incident.student?.id || ''}</td>
                  <td>{incident.reportedBy?.id || ''}</td>
                  <td>{incident.incidentType}</td>
                  <td>{incident.description}</td>
                  <td>{incident.actionTaken}</td>
                  <td>{getStatusBadge(incident.status)}</td>
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
                      disabled={incident.status === 'RESOLVED'}
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