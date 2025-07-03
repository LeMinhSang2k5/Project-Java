import React from 'react';
import { Modal, Button, Row, Col, Badge } from 'react-bootstrap';

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

const ViewIncidentModal = ({ show, onClose, incident }) => {
  if (!incident) return null;

  return (
    <Modal show={show} onHide={onClose} backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết sự kiện y tế</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row className="mb-2">
          <Col md={6}><strong>Học sinh:</strong></Col>
          <Col md={6}>{incident.studentName || incident.student?.name || '---'}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Lớp:</strong></Col>
          <Col md={6}>{incident.studentClass || incident.student?.className || '---'}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Loại sự kiện:</strong></Col>
          <Col md={6}>{incident.type || incident.incidentType || '---'}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Mức độ:</strong></Col>
          <Col md={6}>{getPriorityBadge(incident.priority)}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Trạng thái:</strong></Col>
          <Col md={6}>{getStatusBadge(incident.status)}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Người báo cáo:</strong></Col>
          <Col md={6}>{incident.reportedByName || incident.reportedBy?.name || '---'}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Thời gian báo cáo:</strong></Col>
          <Col md={6}>{incident.reportedAt ? new Date(incident.reportedAt).toLocaleString('vi-VN') : (incident.incidentTime ? new Date(incident.incidentTime).toLocaleString('vi-VN') : '---')}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Mô tả:</strong></Col>
          <Col md={6}>{incident.description}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Người xử lý:</strong></Col>
          <Col md={6}>{incident.treatedBy || '---'}</Col>
        </Row>
        <Row className="mb-2">
          <Col md={6}><strong>Ghi chú xử lý:</strong></Col>
          <Col md={6}>{incident.treatmentNotes || '---'}</Col>
        </Row>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Đóng
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ViewIncidentModal; 