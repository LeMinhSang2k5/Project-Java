import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const defaultForm = {
  name: '',
  quantity: '',
  category: 'Thuốc',
  expiryDate: '',
  stockInDate: '',
  stockOutDate: ''
};

function ModalCreateMedicalSupply({ show, onClose, onMedicalSupplyAdded }) {
  const [form, setForm] = useState({ ...defaultForm });
  const [loading, setLoading] = useState(false);
  const [validated, setValidated] = useState(false);

  // Reset form khi đóng modal
  const handleClose = () => {
    setForm({ ...defaultForm });
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
      };
      
      console.log('Sending data:', data); // Debug log
      
      const response = await api.post('/medical-supplies', data);
      if (response.data) {
        toast.success('Thêm vật tư y tế thành công!');
        setForm({ ...defaultForm });
        setValidated(false);
        onMedicalSupplyAdded && onMedicalSupplyAdded();
        handleClose();
      }
    } catch (err) {
      console.error('Error details:', err.response?.data);
      const errorMessage = err.response?.data || 'Lỗi khi thêm vật tư y tế';
      toast.error(typeof errorMessage === 'string' ? errorMessage : 'Lỗi khi thêm vật tư y tế');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} centered size="lg">
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>
            <i className="fas fa-plus-circle me-2 text-success"></i>
            Thêm vật tư y tế mới
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
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
                  min={1}
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
            <div className="col-md-12">
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
          </div>

          <div className="row">
            <div className="col-md-4">
              <Form.Group className="mb-3">
                <Form.Label>Hạn sử dụng</Form.Label>
                <Form.Control
                  type="date"
                  name="expiryDate"
                  value={form.expiryDate}
                  onChange={handleChange}
                />
                <Form.Text className="text-muted">
                  Ngày hết hạn (nếu có)
                </Form.Text>
              </Form.Group>
            </div>
            <div className="col-md-4">
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
            <div className="col-md-4">
              <Form.Group className="mb-3">
                <Form.Label>Ngày xuất kho</Form.Label>
                <Form.Control
                  type="date"
                  name="stockOutDate"
                  value={form.stockOutDate}
                  onChange={handleChange}
                />
                <Form.Text className="text-muted">
                  Ngày xuất khỏi kho (nếu có)
                </Form.Text>
              </Form.Group>
            </div>
          </div>

          <div className="alert alert-info">
            <i className="fas fa-info-circle me-2"></i>
            <strong>Lưu ý:</strong> Các trường có dấu <span className="text-danger">*</span> là bắt buộc. 
            Các trường ngày tháng có thể để trống nếu chưa có thông tin.
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={loading}>
            <i className="fas fa-times me-2"></i>
            Đóng
          </Button>
          <Button variant="success" type="submit" disabled={loading}>
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                Đang xử lý...
              </>
            ) : (
              <>
                <i className="fas fa-save me-2"></i>
                Thêm vật tư
              </>
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalCreateMedicalSupply;
