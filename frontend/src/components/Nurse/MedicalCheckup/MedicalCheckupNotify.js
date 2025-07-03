import React, { useState, useEffect } from 'react';
import { Form, Button, Spinner } from 'react-bootstrap';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const MedicalCheckupNotify = () => {
  const [students, setStudents] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [content, setContent] = useState('');
  const [scheduledDate, setScheduledDate] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.get('/students')
      .then(res => setStudents(res.data))
      .catch(() => toast.error('Không thể tải danh sách học sinh!'));
  }, []);

  const handleCheck = (id) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (selectedIds.length === 0 || !content || !scheduledDate) {
      toast.error('Vui lòng chọn học sinh, nhập nội dung và ngày kiểm tra!');
      return;
    }
    setLoading(true);
    try {
      await api.post('/medical-checkup/notify/bulk', {
        studentIds: selectedIds,
        content,
        scheduledDate
      });
      toast.success('Đã gửi phiếu thông báo kiểm tra y tế!');
      setSelectedIds([]);
      setContent('');
      setScheduledDate('');
    } catch {
      toast.error('Gửi phiếu thông báo thất bại!');
    }
    setLoading(false);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group className="mb-3">
        <Form.Label>Chọn học sinh</Form.Label>
        <div style={{ maxHeight: 200, overflowY: 'auto', border: '1px solid #eee', padding: 8 }}>
          {students.map(student => (
            <Form.Check
              key={student.id}
              type="checkbox"
              label={`${student.fullName} - ${student.code}`}
              checked={selectedIds.includes(student.id)}
              onChange={() => handleCheck(student.id)}
            />
          ))}
        </div>
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Nội dung thông báo</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          value={content}
          onChange={e => setContent(e.target.value)}
          required
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Ngày kiểm tra</Form.Label>
        <Form.Control
          type="datetime-local"
          value={scheduledDate}
          onChange={e => setScheduledDate(e.target.value)}
          required
        />
      </Form.Group>
      <Button type="submit" disabled={loading}>
        {loading ? <Spinner size="sm" /> : 'Gửi phiếu thông báo'}
      </Button>
    </Form>
  );
};

export default MedicalCheckupNotify; 