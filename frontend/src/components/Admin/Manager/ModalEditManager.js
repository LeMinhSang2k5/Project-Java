import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalEditManager = ({ show, onClose, manager, onManagerUpdated }) => {
  const [form, setForm] = useState({
    email: "",
    fullName: "",
    gender: "MALE",
    phoneNumber: ""
  });

  useEffect(() => {
    if (manager) {
      setForm({
        email: manager.email || "",
        fullName: manager.fullName || "",
        gender: manager.gender || "MALE",
        phoneNumber: manager.phoneNumber || ""
      });
    }
  }, [manager]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Gọi API cập nhật manager ở đây nếu cần
    onManagerUpdated && onManagerUpdated({ ...manager, ...form });
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Sửa thông tin quản lý</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-2">
            <Form.Label>Email</Form.Label>
            <Form.Control name="email" value={form.email} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control name="fullName" value={form.fullName} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Giới tính</Form.Label>
            <Form.Select name="gender" value={form.gender} onChange={handleChange}>
              <option value="MALE">Nam</option>
              <option value="FEMALE">Nữ</option>
              <option value="OTHER">Khác</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Số điện thoại</Form.Label>
            <Form.Control name="phoneNumber" value={form.phoneNumber} onChange={handleChange} required />
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

export default ModalEditManager;