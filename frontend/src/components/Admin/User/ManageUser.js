import React, { useEffect, useState } from 'react';
import api from '../../../config/api';
import ModalCreateUser from './ModalCreateUser';
import ModalEditUser from './ModalEditUser';
import ModalDeleteUser from './ModalDeleteUser';
import Button from 'react-bootstrap/Button';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const getRoleName = (role) => {
  const roleMap = {
    'PARENT': 'Phụ huynh',
    'SCHOOL_NURSE': 'Y tá trường học',
    'MANAGER': 'Quản lý',
    'ADMIN': 'Quản trị viên'
  };
  return roleMap[role] || role;
};

const ManageUser = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

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
    toast.success('Thêm người dùng thành công!');
  };

  const handleUserUpdated = () => {
    fetchUsers();
    toast.success('Cập nhật người dùng thành công!');
  };

  const handleUserDeleted = () => {
    fetchUsers();
    toast.success('Xóa người dùng thành công!');
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
      
      <h2>Quản lý người dùng</h2>
      <div className="mb-3">
        <Button variant="success" onClick={() => setShowCreate(true)}>
          <FaPlus className="me-2" /> Thêm người dùng
        </Button>
      </div>
      <table className="table table-bordered table-hover mt-4">
        <thead className="table-light">
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Họ tên</th>
            <th>Vai trò</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.email}</td>
              <td>{user.fullName}</td>
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
    </div>
  );
};

export default ManageUser; 