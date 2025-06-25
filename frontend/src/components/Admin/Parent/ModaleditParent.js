import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import api from "../../../config/api";
import { toast } from "react-toastify";

const ModalEditParent = ({ show, onClose, parentData, onParentUpdated }) => {
  const [parent, setParent] = useState({ ...parentData });

  useEffect(() => {
    setParent({ ...parentData });
  }, [parentData, show]);

  const handleChange = (e) => {
    setParent({ ...parent, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/parents/${parent.id}`, parent);
      toast.success("Cập nhật phụ huynh thành công!");
      onParentUpdated && onParentUpdated();
      onClose();
    } catch (err) {
      toast.error("Lỗi khi cập nhật phụ huynh!");
    }
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật phụ huynh</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-2">
            <Form.Label>Email</Form.Label>
            <Form.Control name="email" value={parent.email || ""} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control name="fullName" value={parent.fullName || ""} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Giới tính</Form.Label>
            <Form.Select name="gender" value={parent.gender || "MALE"} onChange={handleChange}>
              <option value="MALE">Nam</option>
              <option value="FEMALE">Nữ</option>
              <option value="OTHER">Khác</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Số điện thoại</Form.Label>
            <Form.Control name="phoneNumber" value={parent.phoneNumber || ""} onChange={handleChange} required />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>Hủy</Button>
          <Button variant="primary" type="submit">Lưu</Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default ModalEditParent;