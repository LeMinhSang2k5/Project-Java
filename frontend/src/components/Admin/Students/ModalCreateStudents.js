import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import api from "../../../config/api";
import { toast } from "react-toastify";

const ModalCreateStudents = ({ show, onClose, onStudentAdded }) => {
  const [student, setStudent] = useState({
    email: "",
    password: "",
    fullName: "",
    role: "STUDENT",
    dateOfBirth: "",
    gender: "",
    studentClass: ""
  });

  const handleChange = (e) => {
    setStudent({ ...student, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/students", student);
      toast.success("Thêm học sinh thành công!");
      onStudentAdded && onStudentAdded();
      onClose();
      // Reset form nếu muốn
      setStudent({
        email: "",
        password: "",
        fullName: "",
        role: "STUDENT",
        dateOfBirth: "",
        gender: "",
        studentClass: ""
      });
    } catch (err) {
      toast.error("Lỗi khi thêm học sinh!");
    }
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Thêm học sinh</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-2">
            <Form.Label>Email</Form.Label>
            <Form.Control name="email" value={student.email} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Mật khẩu</Form.Label>
            <Form.Control name="password" type="password" value={student.password} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Họ tên</Form.Label>
            <Form.Control name="fullName" value={student.fullName} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Ngày sinh</Form.Label>
            <Form.Control name="dateOfBirth" type="date" value={student.dateOfBirth} onChange={handleChange} required />
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Giới tính</Form.Label>
            <Form.Select name="gender" value={student.gender} onChange={handleChange}>
              <option value="MALE">Nam</option>
              <option value="FEMALE">Nữ</option>
              <option value="OTHER">Khác</option>
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-2">
            <Form.Label>Lớp</Form.Label>
            <Form.Control name="studentClass" value={student.studentClass} onChange={handleChange} required />
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

export default ModalCreateStudents;