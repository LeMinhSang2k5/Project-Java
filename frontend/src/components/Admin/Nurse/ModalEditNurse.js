import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalEditNurse = ({ show, onClose, nurse, onNurseUpdated }) => {
  const [form, setForm] = useState({
    email: "",
    fullName: "",
    gender: "MALE",
    phoneNumber: "",
    department: ""
  });

  useEffect(() => {
    if (nurse) {
      setForm({
        email: nurse.email || "",
        fullName: nurse.fullName || "",
        gender: nurse.gender || "MALE",
        phoneNumber: nurse.phoneNumber || "",
        department: nurse.department || ""
      });
    }
  }, [nurse]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Gọi API cập nhật nurse ở đây nếu cần
    onNurseUpdated && onNurseUpdated({ ...nurse, ...form });
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Sửa thông tin y tế</Modal.Title>
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
          <Form.Group className="mb-2">
            <Form.Label>Khoa/Phòng</Form.Label>
            <Form.Control name="department" value={form.department} onChange={handleChange} />
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

export default ModalEditNurse;