import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalCreateUser({ show, onClose, onUserAdded }) {
  const [form, setForm] = useState({ 
    email: '', 
    password: '', 
    fullName: '', 
    role: '',
    isActive: true 
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await api.post('/user', form);
      if (response.data) {
        toast.success(`Thêm ${form.email} thành công`);
        setForm({ email: '', password: '', fullName: '', role: '', isActive: true });
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

  return (
    <Modal show={show} onHide={onClose} centered>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>Thêm người dùng</Modal.Title>
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
              <option value="ADMIN">Quản trị viên</option>
            </Form.Select>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose} disabled={loading}>
            Đóng
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Đang xử lý...' : 'Thêm'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalCreateUser;