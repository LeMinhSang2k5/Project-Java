import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalEditMedicalSupply({ show, onClose, medicalSupply, onMedicalSupplyUpdated }) {
  const [form, setForm] = useState({
    name: '',
    quantity: '',
    category: 'Thuốc',
    expiryDate: '',
    stockInDate: ''
  });
  const [loading, setLoading] = useState(false);
  const [validated, setValidated] = useState(false);

  // Cập nhật form khi medicalSupply thay đổi
  useEffect(() => {
    if (medicalSupply) {
      setForm({
        name: medicalSupply.name || '',
        quantity: medicalSupply.quantity || '',
        category: medicalSupply.category || 'Thuốc',
        expiryDate: medicalSupply.expiryDate ? medicalSupply.expiryDate.split('T')[0] : '',
        stockInDate: medicalSupply.stockInDate ? medicalSupply.stockInDate.split('T')[0] : ''
      });
    }
  }, [medicalSupply]);

  // Reset form khi đóng modal
  const handleClose = () => {
    setValidated(false);
    onClose && onClose();
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formElement = e.currentTarget;
    if (!formElement.checkValidity()) {
      e.stopPropagation();
      setValidated(true);
      return;
    }

    setLoading(true);
    try {
      const data = {
        name: form.name.trim(),
        quantity: Number(form.quantity),
        category: form.category,
        expiryDate: form.expiryDate || null,
        stockInDate: form.stockInDate || null,
      };
      
      console.log('Updating medical supply:', medicalSupply.id, 'with data:', data);
      
      const response = await api.put(`/medical-supplies/${medicalSupply.id}`, data);
      if (response.data) {
        toast.success('Cập nhật vật tư y tế thành công!');
        setValidated(false);
        onMedicalSupplyUpdated && onMedicalSupplyUpdated();
        handleClose();
      }
    } catch (err) {
      console.error('Error updating medical supply:', err.response?.data);
      const errorMessage = err.response?.data?.message || err.response?.data || 'Lỗi khi cập nhật vật tư y tế';
      toast.error(typeof errorMessage === 'string' ? errorMessage : 'Lỗi khi cập nhật vật tư y tế');
    } finally {
      setLoading(false);
    }
  };

  if (!medicalSupply) {
    return null;
  }

  return (
    <Modal show={show} onHide={handleClose} centered size="lg">
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>
            <i className="fas fa-edit me-2 text-primary"></i>
            Chỉnh sửa vật tư y tế
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="alert alert-info mb-4">
            <i className="fas fa-info-circle me-2"></i>
            <strong>Thông tin hiện tại:</strong> {medicalSupply.name} (ID: #{medicalSupply.id})
          </div>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>
                  Tên vật tư/thuốc <span className="text-danger">*</span>
                </Form.Label>
                <Form.Control
                  type="text"
                  name="name"
                  value={form.name}
                  onChange={handleChange}
                  required
                  minLength={2}
                  maxLength={100}
                  placeholder="Nhập tên vật tư hoặc thuốc"
                />
                <Form.Control.Feedback type="invalid">
                  Vui lòng nhập tên (2-100 ký tự)
                </Form.Control.Feedback>
                <Form.Text className="text-muted">
                  Tên đầy đủ của vật tư hoặc thuốc
                </Form.Text>
              </Form.Group>
            </div>
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>
                  Số lượng <span className="text-danger">*</span>
                </Form.Label>
                <Form.Control
                  type="number"
                  name="quantity"
                  value={form.quantity}
                  onChange={handleChange}
                  min={0}
                  required
                  placeholder="Nhập số lượng"
                />
                <Form.Control.Feedback type="invalid">
                  Vui lòng nhập số lượng hợp lệ
                </Form.Control.Feedback>
              </Form.Group>
            </div>
          </div>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>
                  Danh mục <span className="text-danger">*</span>
                </Form.Label>
                <Form.Select
                  name="category"
                  value={form.category}
                  onChange={handleChange}
                  required
                >
                  <option value="Thuốc">Thuốc</option>
                  <option value="Vật tư y tế">Vật tư y tế</option>
                </Form.Select>
                <Form.Text className="text-muted">
                  Phân loại vật tư
                </Form.Text>
              </Form.Group>
            </div>
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Hạn sử dụng</Form.Label>
                <Form.Control
                  type="date"
                  name="expiryDate"
                  value={form.expiryDate}
                  onChange={handleChange}
                />
                <Form.Text className="text-muted">
                  Ngày hết hạn (có thể để trống nếu không có hạn sử dụng)
                </Form.Text>
              </Form.Group>
            </div>
          </div>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Ngày nhập kho</Form.Label>
                <Form.Control
                  type="date"
                  name="stockInDate"
                  value={form.stockInDate}
                  onChange={handleChange}
                />
                <Form.Text className="text-muted">
                  Ngày nhập vào kho
                </Form.Text>
              </Form.Group>
            </div>
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Ngày xuất kho</Form.Label>
                <Form.Control
                  type="date"
                  name="stockOutDate"
                  value={medicalSupply.stockOutDate ? medicalSupply.stockOutDate.split('T')[0] : ''}
                  disabled
                />
                <Form.Text className="text-muted">
                  Ngày xuất khỏi kho (được quản lý tự động)
                </Form.Text>
              </Form.Group>
            </div>
          </div>
    
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={loading}>
            <i className="fas fa-times me-2"></i>
            Hủy bỏ
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                Đang cập nhật...
              </>
            ) : (
              <>
                <i className="fas fa-save me-2"></i>
                Cập nhật
              </>
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalEditMedicalSupply;
