import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Table from 'react-bootstrap/Table';
import Alert from 'react-bootstrap/Alert';
import Spinner from 'react-bootstrap/Spinner';
import Badge from 'react-bootstrap/Badge';
import ModalCreateMedicalSupply from './ModalCreateMedicalSupply';
import ModalEditMedicalSupply from './ModalEditMedicalSupply';
import ModalDeleteMedicalSupply from './ModalDeleteMedicalSupply';
import api from '../../../config/api';
import { toast } from 'react-toastify';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ManageMedicalSupply = () => {
  const [supplies, setSupplies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedSupply, setSelectedSupply] = useState(null);

  // Lấy danh sách thuốc/vật tư y tế
  const fetchSupplies = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/medical-supplies');
      setSupplies(res.data);
    } catch (err) {
      setError('Không thể tải dữ liệu!');
      toast.error('Lỗi khi tải danh sách vật tư y tế');
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchSupplies();
  }, []);

  const handleMedicalSupplyAdded = () => {
    fetchSupplies(); // Refresh danh sách sau khi thêm thành công
  };

  const handleMedicalSupplyUpdated = () => {
    fetchSupplies(); // Refresh danh sách sau khi cập nhật thành công
  };

  const handleMedicalSupplyDeleted = () => {
    fetchSupplies(); // Refresh danh sách sau khi xóa thành công
  };

  const handleEditClick = (supply) => {
    setSelectedSupply(supply);
    setShowEditModal(true);
  };

  const handleDeleteClick = (supply) => {
    setSelectedSupply(supply);
    setShowDeleteModal(true);
  };

  // Format ngày tháng
  const formatDate = (dateString) => {
    if (!dateString) return '-';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('vi-VN');
    } catch (error) {
      return dateString;
    }
  };

  // Kiểm tra hạn sử dụng
  const isExpired = (expiryDate) => {
    if (!expiryDate) return false;
    const today = new Date();
    const expiry = new Date(expiryDate);
    return expiry < today;
  };

  // Kiểm tra sắp hết hạn (trong vòng 30 ngày)
  const isExpiringSoon = (expiryDate) => {
    if (!expiryDate) return false;
    const today = new Date();
    const expiry = new Date(expiryDate);
    const thirtyDaysFromNow = new Date(today.getTime() + (30 * 24 * 60 * 60 * 1000));
    return expiry <= thirtyDaysFromNow && expiry >= today;
  };

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />
      <div className="mb-3 d-flex align-items-center">
        <Button 
          variant="success" 
          onClick={() => setShowCreateModal(true)} 
          className="me-3"
        >
          <FaPlus className="me-2" />
          Thêm vật tư y tế
        </Button>
      </div>

      <table className="table table-bordered table-hover mt-4">
        <thead className="table-light">
          <tr>
            <th>ID</th>
            <th>Tên vật tư/thuốc</th>
            <th>Số lượng</th>
            <th>Danh mục</th>
            <th>Hạn sử dụng</th>
            <th>Ngày nhập kho</th>
            <th>Ngày xuất kho</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {supplies.length === 0 ? (
            <tr>
              <td colSpan="8" className="text-center text-muted py-5">
                <div className="d-flex flex-column align-items-center">
                  <i className="fas fa-inbox fa-3x mb-3 text-muted"></i>
                  <h5 className="text-muted">Không có dữ liệu vật tư y tế</h5>
                  <p className="text-muted mb-3">Hãy thêm vật tư y tế đầu tiên để bắt đầu quản lý</p>
                  <Button 
                    variant="outline-primary" 
                    size="sm"
                    onClick={() => setShowCreateModal(true)}
                  >
                    <i className="fas fa-plus me-2"></i>
                    Thêm vật tư đầu tiên
                  </Button>
                </div>
              </td>
            </tr>
          ) : supplies.map(supply => (
            <tr key={supply.id}>
              <td>{supply.id}</td>
              <td>
                <div className="fw-semibold">{supply.name}</div>
              </td>
              <td className="text-center">
                <Badge 
                  bg={supply.quantity < 10 ? 'danger' : supply.quantity < 50 ? 'warning' : 'success'}
                  className="fs-6 px-3 py-2"
                >
                  {supply.quantity}
                </Badge>
              </td>
              <td className="text-center">
                <Badge 
                  bg={supply.category === 'Thuốc' ? 'success' : 'info'}
                  className="px-3 py-2"
                >
                  <i className={`fas fa-${supply.category === 'Thuốc' ? 'pills' : 'band-aid'} me-1`}></i>
                  {supply.category}
                </Badge>
              </td>
              <td className="text-center">
                {supply.expiryDate ? (
                  <div>
                    <div className={`fw-semibold ${
                      isExpired(supply.expiryDate) ? 'text-danger' : 
                      isExpiringSoon(supply.expiryDate) ? 'text-warning' : 'text-success'
                    }`}>
                      {formatDate(supply.expiryDate)}
                    </div>
                    {isExpired(supply.expiryDate) && (
                      <Badge bg="danger" className="mt-1">Hết hạn</Badge>
                    )}
                    {isExpiringSoon(supply.expiryDate) && (
                      <Badge bg="warning" className="mt-1">Sắp hết hạn</Badge>
                    )}
                  </div>
                ) : (
                  <span className="text-muted">-</span>
                )}
              </td>
              <td className="text-center">
                {supply.stockInDate ? (
                  <span className="text-success fw-semibold">
                    {formatDate(supply.stockInDate)}
                  </span>
                ) : (
                  <span className="text-muted">-</span>
                )}
              </td>
              <td className="text-center">
                {supply.stockOutDate ? (
                  <span className="text-info fw-semibold">
                    {formatDate(supply.stockOutDate)}
                  </span>
                ) : (
                  <span className="text-muted">-</span>
                )}
              </td>
              <td>
                <Button
                  variant="warning"
                  size="sm"
                  className="me-2 btn-edit"
                  onClick={() => handleEditClick(supply)}
                >
                  <FaEdit /> Sửa
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="btn-delete"
                  onClick={() => handleDeleteClick(supply)}
                >
                  <FaTrash /> Xóa
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal tạo mới vật tư y tế */}
      <ModalCreateMedicalSupply
        show={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        onMedicalSupplyAdded={handleMedicalSupplyAdded}
      />

      {/* Modal chỉnh sửa vật tư y tế */}
      <ModalEditMedicalSupply
        show={showEditModal}
        onClose={() => {
          setShowEditModal(false);
          setSelectedSupply(null);
        }}
        medicalSupply={selectedSupply}
        onMedicalSupplyUpdated={handleMedicalSupplyUpdated}
      />

      {/* Modal xóa vật tư y tế */}
      <ModalDeleteMedicalSupply
        show={showDeleteModal}
        onClose={() => {
          setShowDeleteModal(false);
          setSelectedSupply(null);
        }}
        medicalSupply={selectedSupply}
        onMedicalSupplyDeleted={handleMedicalSupplyDeleted}
      />
    </div>
  );
};

export default ManageMedicalSupply;
