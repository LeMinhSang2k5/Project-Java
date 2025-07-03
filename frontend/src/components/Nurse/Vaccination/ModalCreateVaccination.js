import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const ModalCreateVaccination = ({ show, onClose, onVaccinationCreated }) => {
  const [formData, setFormData] = useState({
    studentIds: [],
    vaccineName: '',
    scheduledDateTime: '',
    notes: '',
    location: ''
  });
  const [loading, setLoading] = useState(false);
  const [students, setStudents] = useState([]);

  useEffect(() => {
    if (show) {
      fetchStudents();
    }
  }, [show]);

  const fetchStudents = async () => {
    try {
      const response = await api.get('/students');
      setStudents(response.data);
    } catch (error) {
      console.error('Lỗi khi tải danh sách học sinh:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleStudentCheckbox = (studentId) => {
    setFormData(prev => {
      const selected = prev.studentIds.includes(studentId)
        ? prev.studentIds.filter(id => id !== studentId)
        : [...prev.studentIds, studentId];
      return { ...prev, studentIds: selected };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.studentIds || formData.studentIds.length === 0 || !formData.vaccineName || !formData.scheduledDateTime) {
      toast.error('Vui lòng chọn học sinh và điền đầy đủ thông tin bắt buộc');
      return;
    }
    try {
      setLoading(true);
      const vaccinationData = {
        studentIds: formData.studentIds,
        vaccineName: formData.vaccineName,
        scheduledDateTime: formData.scheduledDateTime,
        notes: formData.notes,
        location: formData.location || 'Phòng y tế trường học'
      };
      await api.post('/vaccination-schedules/bulk', vaccinationData);
      toast.success('Tạo lịch tiêm chủng thành công!');
      onVaccinationCreated();
      handleClose();
    } catch (error) {
      console.error('Lỗi khi tạo lịch tiêm chủng:', error);
      toast.error('Lỗi khi tạo lịch tiêm chủng');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setFormData({
      studentIds: [],
      vaccineName: '',
      scheduledDateTime: '',
      notes: '',
      location: ''
    });
    onClose();
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Tạo lịch tiêm chủng mới</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Chọn học sinh <span className="text-danger">*</span></Form.Label>
            <div style={{ maxHeight: 200, overflowY: 'auto', border: '1px solid #eee', padding: 8 }}>
              {students.map((student) => (
                <Form.Check
                  key={student.id}
                  type="checkbox"
                  label={`${student.fullName} - ${student.code}`}
                  value={student.id}
                  checked={formData.studentIds.includes(student.id)}
                  onChange={() => handleStudentCheckbox(student.id)}
                />
              ))}
            </div>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Tên vaccine <span className="text-danger">*</span></Form.Label>
            <Form.Select
              name="vaccineName"
              value={formData.vaccineName}
              onChange={handleInputChange}
              required
            >
              <option value="">Chọn vaccine</option>
              <option value="COVID-19">COVID-19</option>
              <option value="Cúm">Cúm</option>
              <option value="Viêm gan B">Viêm gan B</option>
              <option value="Sởi">Sởi</option>
              <option value="Quai bị">Quai bị</option>
              <option value="Rubella">Rubella</option>
              <option value="Bạch hầu - Ho gà - Uốn ván">Bạch hầu - Ho gà - Uốn ván</option>
              <option value="Bại liệt">Bại liệt</option>
              <option value="Viêm não Nhật Bản">Viêm não Nhật Bản</option>
              <option value="Thủy đậu">Thủy đậu</option>
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Ngày giờ tiêm <span className="text-danger">*</span></Form.Label>
            <Form.Control
              type="datetime-local"
              name="scheduledDateTime"
              value={formData.scheduledDateTime}
              onChange={handleInputChange}
              required
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Ghi chú</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              name="notes"
              value={formData.notes}
              onChange={handleInputChange}
              placeholder="Nhập ghi chú (nếu có)"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Địa điểm tiêm</Form.Label>
            <Form.Control
              type="text"
              name="location"
              value={formData.location}
              onChange={handleInputChange}
              placeholder="Nhập địa điểm tiêm (nếu có)"
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Hủy
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Đang tạo...' : 'Tạo lịch tiêm'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default ModalCreateVaccination;
