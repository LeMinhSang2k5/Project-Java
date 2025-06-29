import React, { useState } from 'react';
import { Modal, Button, Alert } from 'react-bootstrap';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const ModalDeleteVaccination = ({ show, onClose, vaccination, onVaccinationDeleted }) => {
  const [loading, setLoading] = useState(false);

  const handleDelete = async () => {
    if (!vaccination) return;

    try {
      setLoading(true);
      await api.delete(`/vaccination-schedules/${vaccination.id}`);
      toast.success('Xóa lịch tiêm chủng thành công!');
      onVaccinationDeleted();
      onClose();
    } catch (error) {
      console.error('Lỗi khi xóa lịch tiêm chủng:', error);
      toast.error('Lỗi khi xóa lịch tiêm chủng');
    } finally {
      setLoading(false);
    }
  };

  if (!vaccination) return null;

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Xác nhận xóa lịch tiêm chủng</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Alert variant="warning">
          <Alert.Heading>⚠️ Cảnh báo!</Alert.Heading>
          <p>
            Bạn có chắc chắn muốn xóa lịch tiêm chủng này không? Hành động này không thể hoàn tác.
          </p>
        </Alert>
        
        <div className="mt-3">
          <h6>Thông tin lịch tiêm chủng:</h6>
          <div className="row">
            <div className="col-md-6">
              <strong>Học sinh:</strong>
              <p>{vaccination.personName}</p>
            </div>
            <div className="col-md-6">
              <strong>Vaccine:</strong>
              <p>{vaccination.vaccineName}</p>
            </div>
          </div>
          <div className="row">
            <div className="col-md-6">
              <strong>Ngày giờ tiêm:</strong>
              <p>{formatDateTime(vaccination.scheduledDateTime)}</p>
            </div>
            <div className="col-md-6">
              <strong>Trạng thái:</strong>
              <p>
                <span className={`badge ${vaccination.isVaccinated ? 'bg-success' : 'bg-warning'}`}>
                  {vaccination.isVaccinated ? 'Đã tiêm' : 'Chưa tiêm'}
                </span>
              </p>
            </div>
          </div>
          {vaccination.notes && (
            <div className="row">
              <div className="col-12">
                <strong>Ghi chú:</strong>
                <p>{vaccination.notes}</p>
              </div>
            </div>
          )}
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Hủy
        </Button>
        <Button variant="danger" onClick={handleDelete} disabled={loading}>
          {loading ? 'Đang xóa...' : 'Xóa'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteVaccination;
