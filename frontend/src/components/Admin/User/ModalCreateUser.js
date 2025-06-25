import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const defaultForm = {
  email: '',
  password: '',
  fullName: '',
  role: '',
  isActive: true,
  dateOfBirth: '',
  gender: 'MALE',
  studentClass: '',
  phoneNumber: '',
  department: ''
};

function ModalCreateUser({ show, onClose, onUserAdded }) {
  const [form, setForm] = useState({ ...defaultForm });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!show) setForm({ ...defaultForm });
  }, [show]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Lấy endpoint phù hợp với từng role
  const getEndpointByRole = (role) => {
    switch (role) {
      case "STUDENT":
        return "/students";
      case "PARENT":
        return "/parents";
      case "MANAGER":
        return "/managers";
      case "SCHOOL_NURSE":
        return "/nurses";
      default:
        return "/user";
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const endpoint = getEndpointByRole(form.role);
      const response = await api.post(endpoint, form);
      if (response.data) {
        toast.success(`Thêm ${form.email} thành công`);
        setForm({ ...defaultForm });
        onUserAdded && onUserAdded();
        onClose();
      }
    } catch (err) {
      console.error('Error:', err.response?.data);
      toast.error(err.response?.data?.message || 'Lỗi khi thêm user');
    } finally {
      setLoading(false);
    }
  };

  // Trường động theo role
  const renderRoleFields = () => {
    switch (form.role) {
      case "STUDENT":
        return (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Ngày sinh</Form.Label>
              <Form.Control
                type="date"
                name="dateOfBirth"
                value={form.dateOfBirth}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Giới tính</Form.Label>
              <Form.Select name="gender" value={form.gender} onChange={handleChange}>
                <option value="MALE">Nam</option>
                <option value="FEMALE">Nữ</option>
                <option value="OTHER">Khác</option>
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Lớp</Form.Label>
              <Form.Control
                type="text"
                name="studentClass"
                value={form.studentClass}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </>
        );
      case "PARENT":
      case "MANAGER":
        return (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Số điện thoại</Form.Label>
              <Form.Control
                type="text"
                name="phoneNumber"
                value={form.phoneNumber}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Giới tính</Form.Label>
              <Form.Select name="gender" value={form.gender} onChange={handleChange}>
                <option value="MALE">Nam</option>
                <option value="FEMALE">Nữ</option>
                <option value="OTHER">Khác</option>
              </Form.Select>
            </Form.Group>
          </>
        );
      case "SCHOOL_NURSE":
        return (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Số điện thoại</Form.Label>
              <Form.Control
                type="text"
                name="phoneNumber"
                value={form.phoneNumber}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Giới tính</Form.Label>
              <Form.Select name="gender" value={form.gender} onChange={handleChange}>
                <option value="MALE">Nam</option>
                <option value="FEMALE">Nữ</option>
                <option value="OTHER">Khác</option>
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Khoa/Phòng</Form.Label>
              <Form.Control
                type="text"
                name="department"
                value={form.department}
                onChange={handleChange}
              />
            </Form.Group>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>Thêm người dùng</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Vai trò</Form.Label>
            <Form.Select
              name="role"
              value={form.role}
              onChange={handleChange}
              required
            >
              <option value="">-- Chọn vai trò --</option>
              <option value="STUDENT">Học sinh</option>
              <option value="PARENT">Phụ huynh</option>
              <option value="SCHOOL_NURSE">Nhân viên y tế</option>
              <option value="MANAGER">Quản lý</option>
              <option value="ADMIN">Quản trị viên</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Mật khẩu</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control
              type="text"
              name="fullName"
              value={form.fullName}
              onChange={handleChange}
              required
            />
          </Form.Group>
          {renderRoleFields()}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose} disabled={loading}>
            Đóng
          </Button>
          <Button variant="primary" type="submit" disabled={loading || !form.role}>
            {loading ? 'Đang xử lý...' : 'Thêm'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalCreateUser;