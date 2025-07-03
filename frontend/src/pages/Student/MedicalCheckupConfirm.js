import React, { useState, useEffect } from 'react';
import { Table, Button, Spinner } from 'react-bootstrap';
import api from '../../config/api';
import { toast } from 'react-toastify';

const MedicalCheckupConfirm = ({ studentId }) => {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!studentId) return;
    setLoading(true);
    api.get(`/medical-checkup/results?studentId=${studentId}`)
      .then(res => setResults(res.data))
      .catch(() => toast.error('Không thể tải kết quả kiểm tra!'))
      .finally(() => setLoading(false));
  }, [studentId]);

  const handleConfirm = async (id) => {
    try {
      await api.put(`/medical-checkup/result/${id}/student-confirm`);
      toast.success('Đã xác nhận kết quả kiểm tra!');
      setResults(list => list.map(r => r.id === id ? { ...r, studentConfirmation: true } : r));
    } catch {
      toast.error('Xác nhận thất bại!');
    }
  };

  return (
    <div>
      <h5>Kết quả kiểm tra y tế cần xác nhận</h5>
      {loading ? (
        <Spinner animation="border" />
      ) : (
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Kết quả</th>
              <th>Ngày kiểm tra</th>
              <th>Bất thường</th>
              <th>Ghi chú</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {results.filter(r => !r.studentConfirmation).length === 0 ? (
              <tr>
                <td colSpan={6} className="text-center">Không có kết quả nào cần xác nhận</td>
              </tr>
            ) : results.filter(r => !r.studentConfirmation).map(r => (
              <tr key={r.id}>
                <td>{r.result}</td>
                <td>{r.checkupDate ? new Date(r.checkupDate).toLocaleString('vi-VN') : ''}</td>
                <td>{r.abnormal ? 'Có' : 'Không'}</td>
                <td>{r.notes}</td>
                <td>{r.studentConfirmation ? 'Đã xác nhận' : 'Chờ xác nhận'}</td>
                <td>
                  <Button
                    variant="success"
                    size="sm"
                    onClick={() => handleConfirm(r.id)}
                  >Xác nhận</Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default MedicalCheckupConfirm; 