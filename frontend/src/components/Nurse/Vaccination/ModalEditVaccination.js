import React, { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const ModalEditVaccination = ({ show, onClose, vaccination, onVaccinationUpdated }) => {
  const [formData, setFormData] = useState({
    personName: '',
    vaccineName: '',
    scheduledDateTime: '',
    isVaccinated: false,
    notes: '',
    location: ''
  });
  const [loading, setLoading] = useState(false);
  const [students, setStudents] = useState([]);

  useEffect(() => {
    if (show && vaccination) {
      fetchStudents();
      // Format datetime for input
      const scheduledDate = new Date(vaccination.scheduledDateTime);
      const formattedDateTime = scheduledDate.toISOString().slice(0, 16);
      
      setFormData({
        personName: vaccination.personName || '',
        vaccineName: vaccination.vaccineName || '',
        scheduledDateTime: formattedDateTime,
        isVaccinated: vaccination.isVaccinated || false,
        notes: vaccination.notes || '',
        location: vaccination.location || 'Phòng y tế trường học'
      });
    }
  }, [show, vaccination]);

  const fetchStudents = async () => {
    try {
      const response = await api.get('/students');
      setStudents(response.data);
    } catch (error) {
      console.error('Lỗi khi tải danh sách học sinh:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.personName || !formData.vaccineName || !formData.scheduledDateTime) {
      toast.error('Vui lòng điền đầy đủ thông tin bắt buộc');
      return;
    }

    try {
      setLoading(true);
      
      // Tìm student ID từ danh sách students
      const selectedStudent = students.find(student => student.fullName === formData.personName);
      if (!selectedStudent) {
        toast.error('Không tìm thấy thông tin học sinh');
        return;
      }

      const vaccinationData = {
        studentId: selectedStudent.id,
        studentName: formData.personName,
        studentCode: selectedStudent.code,
        vaccineName: formData.vaccineName,
        scheduledDateTime: formData.scheduledDateTime,
        isVaccinated: formData.isVaccinated,
        notes: formData.notes,
        location: formData.location || 'Phòng y tế trường học'
      };

      await api.put(`/vaccination-schedules/${vaccination.id}`, vaccinationData);
      toast.success('Cập nhật lịch tiêm chủng thành công!');
      onVaccinationUpdated();
      handleClose();
    } catch (error) {
      console.error('Lỗi khi cập nhật lịch tiêm chủng:', error);
      toast.error('Lỗi khi cập nhật lịch tiêm chủng');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setFormData({
      personName: '',
      vaccineName: '',
      scheduledDateTime: '',
      isVaccinated: false,
      notes: '',
      location: ''
    });
    onClose();
  };

  if (!vaccination) return null;

  return (
    <Modal show={show} onHide={handleClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Chỉnh sửa lịch tiêm chủng</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Học sinh <span className="text-danger">*</span></Form.Label>
            <Form.Select
              name="personName"
              value={formData.personName}
              onChange={handleInputChange}
              required
            >
              <option value="">Chọn học sinh</option>
              {students.map((student) => (
                <option key={student.id} value={student.fullName}>
                  {student.fullName} - {student.code}
                </option>
              ))}
            </Form.Select>
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
            <Form.Check
              type="checkbox"
              name="isVaccinated"
              checked={formData.isVaccinated}
              onChange={handleInputChange}
              label="Đã tiêm chủng"
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
            {loading ? 'Đang cập nhật...' : 'Cập nhật'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default ModalEditVaccination;
