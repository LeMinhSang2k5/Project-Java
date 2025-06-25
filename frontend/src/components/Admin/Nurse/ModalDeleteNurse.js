import React from "react";
import { Modal, Button } from "react-bootstrap";

const ModalDeleteNurse = ({ show, onClose, nurse, onNurseDeleted }) => {
  const handleDelete = () => {
    // Gọi API xóa nurse ở đây nếu cần
    onNurseDeleted && onNurseDeleted(nurse);
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Xóa y tế</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {nurse ? (
          <p>Bạn có chắc chắn muốn xóa y tế <strong>{nurse.fullName}</strong>?</p>
        ) : (
          <p>Không tìm thấy thông tin y tế.</p>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>Hủy</Button>
        <Button variant="danger" onClick={handleDelete} disabled={!nurse}>Xóa</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteNurse;