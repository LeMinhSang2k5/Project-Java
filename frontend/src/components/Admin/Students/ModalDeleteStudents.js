import React from "react";
import { Modal, Button } from "react-bootstrap";

const ModalDeleteStudents = ({ show, onClose, student, onStudentDeleted }) => {
  const handleDelete = () => {
    // Gọi API xóa student ở đây nếu cần
    onStudentDeleted && onStudentDeleted(student);
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Xóa học sinh</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {student ? (
          <p>Bạn có chắc chắn muốn xóa học sinh <strong>{student.fullName}</strong>?</p>
        ) : (
          <p>Không tìm thấy thông tin học sinh.</p>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>Hủy</Button>
        <Button variant="danger" onClick={handleDelete} disabled={!student}>Xóa</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteStudents;