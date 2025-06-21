import React, { useEffect, useState } from 'react';
import api from '../../../config/api';
import Button from 'react-bootstrap/Button';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

const initialForm = {
  studentName: '',
  className: '',
  grade: '',
  gender: '',
  dateOfBirth: '',
  city: '',
  district: '',
  allergies: '',
  chronicDiseases: '',
  medicalHistory: '',
  vaccinationHistory: '',
  visionDetails: '',
  hearingDetails: '',
};

function ManageHealthProfile() {
  const [profiles, setProfiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('create'); // 'create' | 'edit' | 'delete'
  const [selectedProfile, setSelectedProfile] = useState(null);
  const [form, setForm] = useState(initialForm);

  const fetchProfiles = () => {
    setLoading(true);
    api.get('/health-profiles')
      .then(res => {
        setProfiles(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError('Lỗi khi tải danh sách hồ sơ sức khỏe');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchProfiles();
  }, []);

  const handleShowCreate = () => {
    setForm(initialForm);
    setModalType('create');
    setShowModal(true);
  };

  const handleShowEdit = (profile) => {
    setForm({
      studentName: profile.studentName,
      className: profile.className,
      grade: profile.grade,
      gender: profile.gender,
      dateOfBirth: profile.dateOfBirth,
      city: profile.city,
      district: profile.district,
      allergies: profile.allergies,
      chronicDiseases: profile.chronicDiseases,
      medicalHistory: profile.medicalHistory,
      vaccinationHistory: profile.vaccinationHistory,
      visionDetails: profile.visionDetails,
      hearingDetails: profile.hearingDetails,
    });
    setSelectedProfile(profile);
    setModalType('edit');
    setShowModal(true);
  };

  const handleShowDelete = (profile) => {
    setSelectedProfile(profile);
    setModalType('delete');
    setShowModal(true);
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (modalType === 'create') {
      await api.post('/health-profiles', form);
    } else if (modalType === 'edit' && selectedProfile) {
      await api.put(`/health-profiles/${selectedProfile.id}`, form);
    }
    setShowModal(false);
    fetchProfiles();
  };

  const handleDelete = async () => {
    if (selectedProfile) {
      await api.delete(`/health-profiles/${selectedProfile.id}`);
      setShowModal(false);
      fetchProfiles();
    }
  };

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2>Quản lý hồ sơ sức khỏe</h2>
      <div className="mb-3 d-flex justify-content-between align-items-center gap-2">
        <div>
          <Button variant="success" onClick={handleShowCreate} className="me-2">
            <FaPlus className="me-2" /> Thêm hồ sơ
          </Button>
          <Button variant="outline-primary">
            Xuất Excel
          </Button>
        </div>
      </div>
      <div style={{overflowX: 'auto'}}>
      <table className="table table-bordered table-hover mt-4">
        <thead className="table-light">
          <tr>
            <th>Tên học sinh</th>
            <th>Lớp</th>
            <th>Khối</th>
            <th>Giới tính</th>
            <th>Ngày sinh</th>
            <th>Thành phố</th>
            <th>Quận/Huyện</th>
            <th>Dị ứng</th>
            <th>Bệnh mãn tính</th>
            <th>Tiền sử bệnh</th>
            <th>Lịch sử tiêm chủng</th>
            <th>Thị lực</th>
            <th>Thính lực</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {profiles.map((profile, idx) => (
            <tr key={idx}>
              <td>{profile.studentName}</td>
              <td>{profile.className}</td>
              <td>{profile.grade}</td>
              <td>{profile.gender}</td>
              <td>{profile.dateOfBirth}</td>
              <td>{profile.city}</td>
              <td>{profile.district}</td>
              <td>{profile.allergies}</td>
              <td>{profile.chronicDiseases}</td>
              <td>{profile.medicalHistory}</td>
              <td>{profile.vaccinationHistory}</td>
              <td>{profile.visionDetails}</td>
              <td>{profile.hearingDetails}</td>
              <td>
                <Button variant="warning" size="sm" className="me-2" onClick={() => handleShowEdit(profile)}><FaEdit /></Button>
                <Button variant="danger" size="sm" onClick={() => handleShowDelete(profile)}><FaTrash /></Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>
      {/* Modal Thêm/Sửa */}
      <Modal show={showModal && (modalType === 'create' || modalType === 'edit')} onHide={() => setShowModal(false)} centered size="lg">
        <Form onSubmit={handleSubmit}>
          <Modal.Header closeButton>
            <Modal.Title>{modalType === 'create' ? 'Thêm hồ sơ sức khỏe' : 'Sửa hồ sơ sức khỏe'}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div className="row">
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Tên học sinh</Form.Label>
                  <Form.Control name="studentName" value={form.studentName} onChange={handleChange} required />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Lớp</Form.Label>
                  <Form.Control name="className" value={form.className} onChange={handleChange} required />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Khối</Form.Label>
                  <Form.Control name="grade" value={form.grade} onChange={handleChange} required />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Giới tính</Form.Label>
                  <Form.Select name="gender" value={form.gender} onChange={handleChange} required>
                    <option value="">-- Chọn giới tính --</option>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                    <option value="Khác">Khác</option>
                  </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Ngày sinh</Form.Label>
                  <Form.Control type="date" name="dateOfBirth" value={form.dateOfBirth} onChange={handleChange} required />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Thành phố</Form.Label>
                  <Form.Control name="city" value={form.city} onChange={handleChange} required />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Quận/Huyện</Form.Label>
                  <Form.Control name="district" value={form.district} onChange={handleChange} required />
                </Form.Group>
              </div>
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Dị ứng</Form.Label>
                  <Form.Control as="textarea" rows={2} name="allergies" value={form.allergies} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Bệnh mãn tính</Form.Label>
                  <Form.Control as="textarea" rows={2} name="chronicDiseases" value={form.chronicDiseases} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Tiền sử bệnh</Form.Label>
                  <Form.Control as="textarea" rows={2} name="medicalHistory" value={form.medicalHistory} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Lịch sử tiêm chủng</Form.Label>
                  <Form.Control as="textarea" rows={2} name="vaccinationHistory" value={form.vaccinationHistory} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Thị lực</Form.Label>
                  <Form.Control name="visionDetails" value={form.visionDetails} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Thính lực</Form.Label>
                  <Form.Control name="hearingDetails" value={form.hearingDetails} onChange={handleChange} />
                </Form.Group>
              </div>
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>Đóng</Button>
            <Button variant="primary" type="submit">{modalType === 'create' ? 'Thêm' : 'Lưu'}</Button>
          </Modal.Footer>
        </Form>
      </Modal>
      {/* Modal Xóa */}
      <Modal show={showModal && modalType === 'delete'} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Xác nhận xóa</Modal.Title>
        </Modal.Header>
        <Modal.Body>Bạn có chắc muốn xóa hồ sơ của <b>{selectedProfile?.studentName}</b>?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
          <Button variant="danger" onClick={handleDelete}>Xóa</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default ManageHealthProfile;
