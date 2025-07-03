import React, { useState } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../../../config/api';

const initialState = {
  studentId: '',
  type: '',
  description: '',
  priority: 'medium',
  status: 'pending',
  reportedBy: '',
};

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
      // Gửi dữ liệu lên backend
      await api.post('/api/health-incidents', {
        student: { id: form.studentId },
        incidentType: form.type,
        description: form.description,
        priority: form.priority,
        status: form.status,
        reportedBy: { id: form.reportedBy },
        incidentTime: new Date().toISOString(),
      });
      toast.success('Tạo sự kiện y tế thành công!');
      setForm(initialState);
      onIncidentCreated && onIncidentCreated();
      onClose();
    } catch (error) {
      toast.error('Không thể tạo sự kiện y tế');
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
              name="type"
              value={form.type}
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
          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Mức độ</Form.Label>
                <Form.Select name="priority" value={form.priority} onChange={handleChange}>
                  <option value="high">Cao</option>
                  <option value="medium">Trung bình</option>
                  <option value="low">Thấp</option>
                </Form.Select>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label>Trạng thái</Form.Label>
                <Form.Select name="status" value={form.status} onChange={handleChange}>
                  <option value="pending">Chờ xử lý</option>
                  <option value="treating">Đang điều trị</option>
                  <option value="resolved">Đã xử lý</option>
                  <option value="referred">Chuyển viện</option>
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