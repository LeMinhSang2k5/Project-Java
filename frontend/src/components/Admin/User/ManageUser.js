import React, { useEffect, useState } from 'react';
import api from '../../../config/api';
import ModalCreateUser from './ModalCreateUser';
import ModalEditUser from './ModalEditUser';
import ModalDeleteUser from './ModalDeleteUser';
import ModalImportExcel from './ModalImportExcel';
import Button from 'react-bootstrap/Button';
import { FaPlus, FaEdit, FaTrash, FaFileExcel } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Form from 'react-bootstrap/Form';

const getRoleName = (role) => {
  const roleMap = {
    'STUDENT': 'Học sinh',
    'PARENT': 'Phụ huynh',
    'SCHOOL_NURSE': 'Y tá trường học',
    'MANAGER': 'Quản lý',
    'ADMIN': 'Quản trị viên'
  };
  return roleMap[role] || role;
};

const roleOptions = [
  { value: '', label: 'Tất cả' },
  { value: 'STUDENT', label: 'Học sinh' },
  { value: 'PARENT', label: 'Phụ huynh' },
  { value: 'SCHOOL_NURSE', label: 'Y tá trường học' },
  { value: 'MANAGER', label: 'Quản lý' },
  { value: 'ADMIN', label: 'Quản trị viên' }
];

const ManageUser = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  const [showImportExcel, setShowImportExcel] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedRole, setSelectedRole] = useState('');

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await api.get('/users');
      setUsers(response.data);
    } catch (error) {
      toast.error('Lỗi khi tải danh sách người dùng');
      setError('Lỗi khi tải danh sách người dùng');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleUserAdded = () => {
    fetchUsers();
  };

  const handleUserUpdated = () => {
    fetchUsers();
    toast.success('Cập nhật người dùng thành công!');
  };

  const handleUserDeleted = () => {
    fetchUsers();
    toast.success('Xóa người dùng thành công!');
  };

  const handleUserImported = () => {
    fetchUsers();
  };

  // Lọc users theo role được chọn
  const filteredUsers = selectedRole
    ? users.filter((user) => user.role === selectedRole)
    : users;

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2>Quản lý người dùng</h2>
      <div className="mb-3 d-flex align-items-center justify-content-between">
        <div className="d-flex align-items-center">
          <Button variant="success" onClick={() => setShowCreate(true)} className="me-2">
            <FaPlus className="me-2" /> Thêm người dùng
          </Button>
          <Button 
            variant="info" 
            onClick={() => setShowImportExcel(true)} 
            className="me-3"
            title="Nhập danh sách học sinh từ file Excel"
          >
            <FaFileExcel className="me-2" /> Nhập từ Excel
          </Button>
        </div>
        <Form.Select
          style={{ maxWidth: 220 }}
          value={selectedRole}
          onChange={(e) => setSelectedRole(e.target.value)}
        >
          {roleOptions.map((role) => (
            <option key={role.value} value={role.value}>{role.label}</option>
          ))}
        </Form.Select>
      </div>
      <table className="table table-bordered table-hover mt-4">
        <thead className="table-light">
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Họ tên</th>
            {(selectedRole === 'STUDENT' || !selectedRole) && <th>Mã số SV</th>}
            <th>Vai trò</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.email}</td>
              <td>{user.fullName}</td>
              {(selectedRole === 'STUDENT' || !selectedRole) && (
                <td>{user.role === 'STUDENT' ? (user.code || '-') : '-'}</td>
              )}
              <td>{getRoleName(user.role)}</td>
              <td>
                <Button
                  variant="warning"
                  size="sm"
                  className="me-2 btn-edit"
                  onClick={() => { setSelectedUser(user); setShowEdit(true); }}
                >
                  <FaEdit /> Sửa
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="btn-delete"
                  onClick={() => { setSelectedUser(user); setShowDelete(true); }}
                >
                  <FaTrash /> Xóa
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal Thêm */}
      <ModalCreateUser
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onUserAdded={handleUserAdded}
        selectedRole={selectedRole}
      />

      {/* Modal Sửa */}
      <ModalEditUser
        show={showEdit}
        onClose={() => setShowEdit(false)}
        user={selectedUser}
        onUserUpdated={handleUserUpdated}
      />

      {/* Modal Xóa */}
      <ModalDeleteUser
        show={showDelete}
        onClose={() => setShowDelete(false)}
        user={selectedUser}
        onUserDeleted={handleUserDeleted}
      />

      {/* Modal Nhập Excel */}
      <ModalImportExcel
        show={showImportExcel}
        onClose={() => setShowImportExcel(false)}
        onUsersImported={handleUserImported}
      />
    </div>
  );
};

export default ManageUser;