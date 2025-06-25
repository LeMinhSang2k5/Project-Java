import React from "react";
import { Modal, Button } from "react-bootstrap";
import api from "../../../config/api";
import { toast } from "react-toastify";

const ModalDeleteParent = ({ show, onClose, parentId, onParentDeleted }) => {
  const handleDelete = async () => {
    try {
      await api.delete(`/parents/${parentId}`);
      toast.success("Xóa phụ huynh thành công!");
      onParentDeleted && onParentDeleted();
      onClose();
    } catch (err) {
      toast.error("Lỗi khi xóa phụ huynh!");
    }
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Xóa phụ huynh</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Bạn có chắc chắn muốn xóa phụ huynh này không?
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>Hủy</Button>
        <Button variant="danger" onClick={handleDelete}>Xóa</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteParent;