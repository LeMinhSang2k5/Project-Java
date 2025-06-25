import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ModalEditStudents = ({ show, onClose, student, onStudentUpdated }) => {
  const [form, setForm] = useState({
    email: "",
    fullName: "",
    dateOfBirth: "",
    gender: "MALE",
    studentClass: ""
  });

  useEffect(() => {
    if (student) {
      setForm({
        email: student.email || "",
        fullName: student.fullName || "",
        dateOfBirth: student.dateOfBirth || "",
        gender: student.gender || "MALE",
        studentClass: student.studentClass || ""
      });
    }
  }, [student]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Gọi API cập nhật student ở đây nếu cần
    onStudentUpdated && onStudentUpdated({ ...student, ...form });
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Sửa thông tin học sinh</Modal.Title>
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
            <Form.Label>Ngày sinh</Form.Label>
            <Form.Control name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={handleChange} required />
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
            <Form.Label>Lớp</Form.Label>
            <Form.Control name="studentClass" value={form.studentClass} onChange={handleChange} required />
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

export default ModalEditStudents;