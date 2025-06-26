import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalEditUser({ show, onClose, user, onUserUpdated }) {
  const [form, setForm] = useState({ email: '', password: '', fullName: '', role: '' });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setForm({
        email: user.email || '',
        password: '',  // Không hiển thị mật khẩu cũ
        fullName: user.fullName || '',
        role: user.role || ''
      });
    }
  }, [user]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!user) return;

    setLoading(true);
    try {
      // Chỉ gửi các trường đã được thay đổi
      const updatedFields = {};
      if (form.email !== user.email) updatedFields.email = form.email;
      if (form.password) updatedFields.password = form.password;
      if (form.fullName !== user.fullName) updatedFields.fullName = form.fullName;
      if (form.role !== user.role) updatedFields.role = form.role;

      await api.put(`/user/${user.id}`, updatedFields);
      
      // Toast thành công
      toast.success(
        `✅ Cập nhật thông tin "${form.fullName}" thành công!`, 
        {
          position: "top-right",
          autoClose: 3000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        }
      );
      
      onUserUpdated();
      onClose();
    } catch (err) {
      console.error('Error:', err.response?.data);
      
      // Toast lỗi chi tiết
      const errorMessage = err.response?.data?.message || 'Có lỗi xảy ra khi cập nhật người dùng';
      toast.error(
        `❌ ${errorMessage}`, 
        {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        }
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>Cập nhật người dùng</Modal.Title>
        </Modal.Header>
        <Modal.Body>
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
            <Form.Label>Mật khẩu mới (để trống nếu không đổi)</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Nhập mật khẩu mới nếu muốn thay đổi"
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
          <Form.Group className="mb-3">
            <Form.Label>Vai trò</Form.Label>
            <Form.Select
              name="role"
              value={form.role}
              onChange={handleChange}
              required
            >
              <option value="">-- Chọn vai trò --</option>
              <option value="PARENT">Phụ huynh</option>
              <option value="SCHOOL_NURSE">Y tá trường học</option>
              <option value="MANAGER">Quản lý</option>
              <option value="ADMIN">Quản trị viên</option>
            </Form.Select>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose} disabled={loading}>
            Hủy
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Đang cập nhật...' : 'Cập nhật'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalEditUser; 