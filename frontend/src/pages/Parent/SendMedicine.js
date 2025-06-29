import React, { useState, useEffect } from 'react';
import { Card, Button, Form, Table, Alert, Spinner, Row, Col, Badge, Modal } from 'react-bootstrap';
import { FaPlus, FaEdit, FaTrash, FaEye } from 'react-icons/fa';
import api from '../../config/api';
import './SendMedicine.scss';

const SendMedicine = () => {
    const [students, setStudents] = useState([]);
    const [medicalRequests, setMedicalRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [modalType, setModalType] = useState('create'); // 'create' | 'edit' | 'view'
    const [selectedRequest, setSelectedRequest] = useState(null);
    const [formData, setFormData] = useState({
        studentId: '',
        medicalName: '',
        dosage: '',
        note: ''
    });
    const [alert, setAlert] = useState(null);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            const parentId = localStorage.getItem('parentId');
            
            // Lấy danh sách học sinh của phụ huynh
            const studentsResponse = await api.get(`/students/by-parent/${parentId}`);
            setStudents(studentsResponse.data);
            
            // Lấy danh sách yêu cầu thuốc của phụ huynh
            const medicalResponse = await api.get(`/medical/parent/${parentId}`);
            setMedicalRequests(medicalResponse.data);
            
        } catch (error) {
            console.error('Error loading data:', error);
            setAlert('Có lỗi khi tải dữ liệu');
        } finally {
            setLoading(false);
        }
    };

    const handleShowCreate = () => {
        setFormData({
            studentId: '',
            medicalName: '',
            dosage: '',
            note: ''
        });
        setModalType('create');
        setShowModal(true);
    };

    const handleShowEdit = (request) => {
        setFormData({
            studentId: request.student.id,
            medicalName: request.medicalName,
            dosage: request.dosage,
            note: request.note
        });
        setSelectedRequest(request);
        setModalType('edit');
        setShowModal(true);
    };

    const handleShowView = (request) => {
        setSelectedRequest(request);
        setModalType('view');
        setShowModal(true);
    };

    const handleSubmit = async () => {
        // Validation
        if (!formData.studentId || !formData.medicalName || !formData.dosage) {
            setAlert('Vui lòng điền đầy đủ thông tin bắt buộc!');
            return;
        }

        try {
            const parentId = localStorage.getItem('parentId');
            
            if (modalType === 'create') {
                await api.post('/medical', {
                    ...formData,
                    parentId: parentId
                });
                setAlert('Gửi yêu cầu thuốc thành công!');
            } else if (modalType === 'edit') {
                await api.put(`/medical/${selectedRequest.id}`, formData);
                setAlert('Cập nhật yêu cầu thuốc thành công!');
            }
            
            setShowModal(false);
            loadData();
        } catch (error) {
            console.error('Error submitting form:', error);
            setAlert('Có lỗi xảy ra khi gửi yêu cầu');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Bạn có chắc chắn muốn xóa yêu cầu này?')) {
            try {
                await api.delete(`/medical/${id}`);
                setAlert('Xóa yêu cầu thuốc thành công!');
                loadData();
            } catch (error) {
                console.error('Error deleting request:', error);
                setAlert('Có lỗi xảy ra khi xóa yêu cầu');
            }
        }
    };

    const getStatusBadge = (status) => {
        const statusConfig = {
            'PENDING': { variant: 'warning', text: 'Chờ duyệt' },
            'APPROVED': { variant: 'success', text: 'Đã duyệt' },
            'REJECTED': { variant: 'danger', text: 'Từ chối' },
            'COMPLETED': { variant: 'info', text: 'Đã hoàn thành' }
        };
        
        const config = statusConfig[status] || { variant: 'secondary', text: status };
        return <Badge bg={config.variant}>{config.text}</Badge>;
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('vi-VN');
    };

    if (loading) {
        return (
            <div className="text-center py-4">
                <Spinner animation="border" />
                <p className="mt-2">Đang tải dữ liệu...</p>
            </div>
        );
    }

    return (
        <div className="send-medicine">
            <Card>
                <Card.Header className="d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Gửi thuốc cho học sinh</h5>
                    <Button variant="primary" onClick={handleShowCreate}>
                        <FaPlus className="me-2" /> Gửi yêu cầu thuốc mới
                    </Button>
                </Card.Header>
                <Card.Body>
                    {alert && <Alert variant="info" onClose={() => setAlert(null)} dismissible>{alert}</Alert>}
                    
                    {students.length === 0 ? (
                        <Alert variant="warning">
                            Bạn chưa có học sinh nào được liên kết. Vui lòng liên hệ admin để liên kết học sinh.
                        </Alert>
                    ) : (
                        <>
                            <h6 className="mb-3">Danh sách yêu cầu thuốc</h6>
                            <Table striped bordered hover responsive>
                                <thead>
                                    <tr>
                                        <th>Học sinh</th>
                                        <th>Tên thuốc</th>
                                        <th>Liều lượng</th>
                                        <th>Ghi chú</th>
                                        <th>Ngày gửi</th>
                                        <th>Trạng thái</th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {medicalRequests.length === 0 ? (
                                        <tr>
                                            <td colSpan={7} className="text-center">
                                                Chưa có yêu cầu thuốc nào
                                            </td>
                                        </tr>
                                    ) : (
                                        medicalRequests.map((request) => (
                                            <tr key={request.id}>
                                                <td>{request.student?.fullName || 'N/A'}</td>
                                                <td>{request.medicalName}</td>
                                                <td>{request.dosage}</td>
                                                <td>{request.note || 'Không có'}</td>
                                                <td>{formatDate(request.requestDate)}</td>
                                                <td>{getStatusBadge(request.status)}</td>
                                                <td>
                                                    <Button
                                                        variant="info"
                                                        size="sm"
                                                        className="me-2"
                                                        onClick={() => handleShowView(request)}
                                                    >
                                                        <FaEye />
                                                    </Button>
                                                    {request.status === 'PENDING' && (
                                                        <>
                                                            <Button
                                                                variant="warning"
                                                                size="sm"
                                                                className="me-2"
                                                                onClick={() => handleShowEdit(request)}
                                                            >
                                                                <FaEdit />
                                                            </Button>
                                                            <Button
                                                                variant="danger"
                                                                size="sm"
                                                                onClick={() => handleDelete(request.id)}
                                                            >
                                                                <FaTrash />
                                                            </Button>
                                                        </>
                                                    )}
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </Table>
                        </>
                    )}
                </Card.Body>
            </Card>

            {/* Modal */}
            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>
                        {modalType === 'create' ? 'Gửi yêu cầu thuốc mới' :
                         modalType === 'edit' ? 'Cập nhật yêu cầu thuốc' :
                         'Chi tiết yêu cầu thuốc'}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {modalType === 'view' ? (
                        <div>
                            <Row>
                                <Col md={6}>
                                    <p><strong>Học sinh:</strong> {selectedRequest?.student?.fullName}</p>
                                    <p><strong>Tên thuốc:</strong> {selectedRequest?.medicalName}</p>
                                    <p><strong>Liều lượng:</strong> {selectedRequest?.dosage}</p>
                                </Col>
                                <Col md={6}>
                                    <p><strong>Trạng thái:</strong> {getStatusBadge(selectedRequest?.status)}</p>
                                    <p><strong>Ngày gửi:</strong> {formatDate(selectedRequest?.requestDate)}</p>
                                    {selectedRequest?.approvedDate && (
                                        <p><strong>Ngày duyệt:</strong> {formatDate(selectedRequest.approvedDate)}</p>
                                    )}
                                </Col>
                            </Row>
                            <p><strong>Ghi chú:</strong> {selectedRequest?.note || 'Không có'}</p>
                        </div>
                    ) : (
                        <Form>
                            <Form.Group className="mb-3">
                                <Form.Label>Chọn học sinh *</Form.Label>
                                <Form.Select
                                    value={formData.studentId}
                                    onChange={(e) => setFormData({...formData, studentId: e.target.value})}
                                    required
                                >
                                    <option value="">-- Chọn học sinh --</option>
                                    {students.map((student) => (
                                        <option key={student.id} value={student.id}>
                                            {student.fullName} - {student.studentClass}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Tên thuốc *</Form.Label>
                                <Form.Control
                                    type="text"
                                    value={formData.medicalName}
                                    onChange={(e) => setFormData({...formData, medicalName: e.target.value})}
                                    placeholder="Nhập tên thuốc"
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Liều lượng *</Form.Label>
                                <Form.Control
                                    type="text"
                                    value={formData.dosage}
                                    onChange={(e) => setFormData({...formData, dosage: e.target.value})}
                                    placeholder="Ví dụ: 1 viên/ngày, 2 lần/ngày"
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Ghi chú</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    rows={3}
                                    value={formData.note}
                                    onChange={(e) => setFormData({...formData, note: e.target.value})}
                                    placeholder="Nhập ghi chú (nếu có)"
                                />
                            </Form.Group>
                        </Form>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Đóng
                    </Button>
                    {modalType !== 'view' && (
                        <Button variant="primary" onClick={handleSubmit}>
                            {modalType === 'create' ? 'Gửi yêu cầu' : 'Cập nhật'}
                        </Button>
                    )}
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default SendMedicine; 