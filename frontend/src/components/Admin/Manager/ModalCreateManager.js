import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import api from "../../../config/api";
import { toast } from "react-toastify";

const ModalCreateManager = ({ show, onClose, onManagerAdded }) => {
  const [manager, setManager] = useState({
    email: "",
    password: "",
    fullName: "",
    role: "MANAGER",
    gender: "MALE",
    phoneNumber: ""
  });

  const handleChange = (e) => {
    setManager({ ...manager, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/managers", manager);
      toast.success("Thêm manager thành công!");
      onManagerAdded && onManagerAdded();
      onClose();
      // Reset form nếu muốn
      setManager({
        email: "",
        password: "",
        fullName: "",
        role: "MANAGER",
        gender: "MALE",
        phoneNumber: "",
      });
    } catch (err) {
      toast.error("Lỗi khi thêm manager!");
    }
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Thêm quản lý</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-2">
            <Form.Label>Email</Form.Label>
            <Form.Control name="email" value={manager.email} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Mật khẩu</Form.Label>
            <Form.Control name="password" type="password" value={manager.password} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control name="fullName" value={manager.fullName} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Giới tính</Form.Label>
            <Form.Select name="gender" value={manager.gender} onChange={handleChange}>
              <option value="MALE">Nam</option>
              <option value="FEMALE">Nữ</option>
              <option value="OTHER">Khác</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Số điện thoại</Form.Label>
            <Form.Control name="phoneNumber" value={manager.phoneNumber} onChange={handleChange} required />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>Hủy</Button>
          <Button variant="primary" type="submit">Thêm</Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};
export default ModalCreateManager;