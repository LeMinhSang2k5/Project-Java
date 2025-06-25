import React from "react";
import { Modal, Button } from "react-bootstrap";

const ModalDeleteManager = ({ show, onClose, manager, onManagerDeleted }) => {
  const handleDelete = () => {
    // Gọi API xóa manager ở đây nếu cần
    onManagerDeleted && onManagerDeleted(manager);
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Xóa quản lý</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {manager ? (
          <p>Bạn có chắc chắn muốn xóa quản lý <strong>{manager.fullName}</strong>?</p>
        ) : (
          <p>Không tìm thấy thông tin quản lý.</p>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>Hủy</Button>
        <Button variant="danger" onClick={handleDelete} disabled={!manager}>Xóa</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteManager;