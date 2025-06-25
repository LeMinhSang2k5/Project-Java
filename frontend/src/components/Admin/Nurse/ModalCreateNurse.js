import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import api from "../../../config/api";
import { toast } from "react-toastify";


const ModalCreateNurse = ({ show, onClose, onNurseAdded }) => {
  const [nurse, setNurse] = useState({
    email: "",
    password: "",
    fullName: "",
    role: "SCHOOL_NURSE",
    gender: "MALE",
    phoneNumber: "",
    department: ""
  });

  const handleChange = (e) => {
    setNurse({ ...nurse, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/nurses", nurse);
      toast.success("Thêm y tế thành công!");
      onNurseAdded && onNurseAdded();
      onClose();
      // Reset form nếu muốn
      setNurse({
        email: "",
        password: "",
        fullName: "",
        role: "SCHOOL_NURSE",
        gender: "MALE",
        phoneNumber: "",
        department: ""
      });
    } catch (err) {
      toast.error("Lỗi khi thêm y tế!");
    }
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Thêm y tế</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-2">
            <Form.Label>Email</Form.Label>
            <Form.Control name="email" value={nurse.email} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Mật khẩu</Form.Label>
            <Form.Control name="password" type="password" value={nurse.password} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control name="fullName" value={nurse.fullName} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Giới tính</Form.Label>
            <Form.Select name="gender" value={nurse.gender} onChange={handleChange}>
              <option value="MALE">Nam</option>
              <option value="FEMALE">Nữ</option>
              <option value="OTHER">Khác</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Số điện thoại</Form.Label>
            <Form.Control name="phoneNumber" value={nurse.phoneNumber} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Khoa/Phòng</Form.Label>
            <Form.Control name="department" value={nurse.department} onChange={handleChange} />
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

export default ModalCreateNurse;