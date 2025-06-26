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
      
      // Toast thành công
      toast.success(
        `✅ Đã xóa người dùng "${user.fullName || user.email}" thành công!`, 
        {
          position: "top-right",
          autoClose: 3000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        }
      );
      
      onUserDeleted();
      onClose();
    } catch (err) {
      console.error('Error:', err.response?.data);
      
      // Toast lỗi chi tiết
      const errorMessage = err.response?.data?.message || 'Có lỗi xảy ra khi xóa người dùng';
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