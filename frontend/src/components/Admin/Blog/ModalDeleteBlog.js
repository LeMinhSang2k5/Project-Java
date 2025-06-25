import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalDeleteBlog({ show, onClose, onBlogDeleted, blog }) {
  const [loading, setLoading] = useState(false);

  const handleDelete = async () => {
    setLoading(true);
    try {
      await api.delete(`/blogs/${blog.id}`);
      toast.success(`Xóa bài viết "${blog.title}" thành công`);
      onBlogDeleted && onBlogDeleted();
      onClose();
    } catch (err) {
      console.error('Error:', err.response?.data);
      toast.error(err.response?.data?.message || 'Lỗi khi xóa bài viết');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Xác nhận xóa</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Bạn có chắc chắn muốn xóa bài viết "{blog?.title}" không?
        <br />
        <small className="text-danger">Hành động này không thể hoàn tác.</small>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={loading}>
          Hủy
        </Button>
        <Button variant="danger" onClick={handleDelete} disabled={loading}>
          {loading ? 'Đang xử lý...' : 'Xóa'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ModalDeleteBlog; 