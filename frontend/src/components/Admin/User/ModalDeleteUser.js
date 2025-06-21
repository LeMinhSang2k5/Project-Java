import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalDeleteUser({ show, onClose, user, onUserDeleted }) {
  const [loading, setLoading] = useState(false);

  const handleDelete = async () => {
    if (!user) return;
    
    setLoading(true);
    try {
      await api.delete(`/user/${user.id}`);
      onUserDeleted();
      onClose();
    } catch (err) {
      console.error('Error:', err.response?.data);
      toast.error(err.response?.data?.message || 'Lỗi khi xóa người dùng');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Xác nhận xóa người dùng</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {user && (
          <p>
            Bạn có chắc chắn muốn xóa người dùng <strong>{user.email}</strong>?<br />
            Hành động này không thể hoàn tác.
          </p>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={loading}>
          Hủy
        </Button>
        <Button variant="danger" onClick={handleDelete} disabled={loading}>
          {loading ? 'Đang xóa...' : 'Xóa'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ModalDeleteUser; 