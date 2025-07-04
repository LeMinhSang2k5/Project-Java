import React, { useState } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../../../config/api';

const initialState = {
  studentId: '',
  reportedBy: '',
  incidentType: '',
  description: '',
  actionTaken: '',
  incidentTime: '',
  status: 'REPORTED',
};

const statusOptions = [
  { value: 'REPORTED', label: 'Đã báo cáo' },
  { value: 'MONITORING', label: 'Đang theo dõi' },
  { value: 'PARENT_NOTIFIED', label: 'Đã báo phụ huynh' },
  { value: 'RESOLVED', label: 'Đã xử lý' },
];

const CreateIncidentModal = ({ show, onClose, onIncidentCreated }) => {
  const [form, setForm] = useState(initialState);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/health-incidents', {
        student: { id: form.studentId },
        reportedBy: { id: form.reportedBy },
        incidentType: form.incidentType,
        description: form.description,
        actionTaken: form.actionTaken,
        incidentTime: form.incidentTime ? new Date(form.incidentTime).toISOString() : new Date().toISOString(),
        status: form.status,
      });
      toast.success('Tạo sự kiện y tế thành công!');
      setForm(initialState);
      onIncidentCreated && onIncidentCreated();
      onClose();
    } catch (error) {
      console.error(error);
      toast.error(error.response?.data?.message || 'Không thể tạo sự kiện y tế');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Báo cáo sự kiện y tế mới</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Học sinh (ID)</Form.Label>
                <Form.Control
                  type="text"
                  name="studentId"
                  value={form.studentId}
                  onChange={handleChange}
                  required
                  placeholder="Nhập ID học sinh"
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Người báo cáo (ID)</Form.Label>
                <Form.Control
                  type="text"
                  name="reportedBy"
                  value={form.reportedBy}
                  onChange={handleChange}
                  required
                  placeholder="Nhập ID người báo cáo"
                />
              </Form.Group>
            </Col>
          </Row>
          <Form.Group className="mb-3">
            <Form.Label>Loại sự kiện</Form.Label>
            <Form.Control
              type="text"
              name="incidentType"
              value={form.incidentType}
              onChange={handleChange}
              required
              placeholder="Nhập loại sự kiện (VD: Sốt, Té ngã, ... )"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Mô tả</Form.Label>
            <Form.Control
              as="textarea"
              name="description"
              value={form.description}
              onChange={handleChange}
              required
              rows={3}
              placeholder="Nhập mô tả chi tiết sự kiện"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Hành động đã xử lý</Form.Label>
            <Form.Control
              as="textarea"
              name="actionTaken"
              value={form.actionTaken}
              onChange={handleChange}
              rows={2}
              placeholder="Nhập hành động đã xử lý (nếu có)"
            />
          </Form.Group>
          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Thời gian sự kiện</Form.Label>
                <Form.Control
                  type="datetime-local"
                  name="incidentTime"
                  value={form.incidentTime}
                  onChange={handleChange}
                  required
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Trạng thái</Form.Label>
                <Form.Select name="status" value={form.status} onChange={handleChange}>
                  {statusOptions.map(opt => (
                    <option key={opt.value} value={opt.value}>{opt.label}</option>
                  ))}
                </Form.Select>
              </Form.Group>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose} disabled={loading}>
            Đóng
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Đang lưu...' : 'Lưu sự kiện'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default CreateIncidentModal; 