import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalDeleteMedicalSupply({ show, onClose, medicalSupply, onMedicalSupplyDeleted }) {
  const [loading, setLoading] = useState(false);

  const handleDelete = async () => {
    if (!medicalSupply || !medicalSupply.id) {
      toast.error('Không tìm thấy thông tin vật tư y tế');
      return;
    }

    setLoading(true);
    try {
      const response = await api.delete(`/medical-supplies/${medicalSupply.id}`);
      if (response.status === 200) {
        toast.success('Xóa vật tư y tế thành công!');
        onMedicalSupplyDeleted && onMedicalSupplyDeleted();
        onClose();
      }
    } catch (err) {
      console.error('Error details:', err.response?.data);
      const errorMessage = err.response?.data || 'Lỗi khi xóa vật tư y tế';
      toast.error(typeof errorMessage === 'string' ? errorMessage : 'Lỗi khi xóa vật tư y tế');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          <i className="fas fa-exclamation-triangle me-2 text-danger"></i>
          Xác nhận xóa
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {medicalSupply ? (
          <div>
            <div className="alert alert-warning">
              <i className="fas fa-exclamation-triangle me-2"></i>
              <strong>Cảnh báo:</strong> Hành động này không thể hoàn tác!
            </div>
            
            <p>Bạn có chắc chắn muốn xóa vật tư y tế sau đây?</p>
            
            <div className="card border-danger">
              <div className="card-body">
                <h6 className="card-title text-danger">
                  <i className="fas fa-pills me-2"></i>
                  {medicalSupply.name}
                </h6>
                <div className="row">
                  <div className="col-md-6">
                    <p className="mb-1">
                      <strong>ID:</strong> #{medicalSupply.id}
                    </p>
                    <p className="mb-1">
                      <strong>Số lượng:</strong> {medicalSupply.quantity}
                    </p>
                    <p className="mb-1">
                      <strong>Danh mục:</strong> {medicalSupply.category}
                    </p>
                  </div>
                  <div className="col-md-6">
                    <p className="mb-1">
                      <strong>Hạn sử dụng:</strong> {medicalSupply.expiryDate || 'Không có'}
                    </p>
                    <p className="mb-1">
                      <strong>Ngày nhập kho:</strong> {medicalSupply.stockInDate ? new Date(medicalSupply.stockInDate).toLocaleDateString('vi-VN') : 'Không có'}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="mt-3">
              <small className="text-muted">
                <i className="fas fa-info-circle me-1"></i>
                Tất cả dữ liệu liên quan đến vật tư y tế này sẽ bị xóa vĩnh viễn khỏi hệ thống.
              </small>
            </div>
          </div>
        ) : (
          <div className="text-center">
            <i className="fas fa-question-circle fa-2x text-muted mb-3"></i>
            <p>Không tìm thấy thông tin vật tư y tế</p>
          </div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={loading}>
          <i className="fas fa-times me-2"></i>
          Hủy
        </Button>
        <Button 
          variant="danger" 
          onClick={handleDelete} 
          disabled={loading || !medicalSupply}
        >
          {loading ? (
            <>
              <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
              Đang xóa...
            </>
          ) : (
            <>
              <i className="fas fa-trash me-2"></i>
              Xóa vật tư
            </>
          )}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ModalDeleteMedicalSupply;
