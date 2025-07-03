import React, { useState, useEffect } from 'react';
import { Form, Button, Spinner } from 'react-bootstrap';
import api from '../../../config/api';
import { toast } from 'react-toastify';

const MedicalCheckupResult = () => {
  const [approvedList, setApprovedList] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState('');
  const [result, setResult] = useState('');
  const [notes, setNotes] = useState('');
  const [abnormal, setAbnormal] = useState(false);
  const [checkupDate, setCheckupDate] = useState('');
  const [loading, setLoading] = useState(false);
  const [dates, setDates] = useState([]);
  const [selectedDate, setSelectedDate] = useState('');

  useEffect(() => {
    api.get('/medical-checkup/notifications?status=APPROVED')
      .then(res => {
        setApprovedList(res.data);
        // Lấy danh sách ngày kiểm tra duy nhất
        const uniqueDates = Array.from(new Set(res.data.map(n => n.scheduledDate)));
        setDates(uniqueDates);
      })
      .catch(() => toast.error('Không thể tải danh sách học sinh đã xác nhận!'));
  }, []);

  // Lọc học sinh theo ngày kiểm tra đã chọn
  const filteredStudents = approvedList.filter(n => n.scheduledDate === selectedDate);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedStudent || !result || !checkupDate) {
      toast.error('Vui lòng chọn học sinh, nhập kết quả và ngày kiểm tra!');
      return;
    }
    setLoading(true);
    try {
      await api.post('/medical-checkup/result', {
        studentId: selectedStudent,
        result,
        notes,
        abnormal,
        checkupDate
      });
      toast.success('Đã lưu kết quả kiểm tra!');
      setResult('');
      setNotes('');
      setAbnormal(false);
      setCheckupDate('');
    } catch {
      toast.error('Lưu kết quả thất bại!');
    }
    setLoading(false);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group className="mb-3">
        <Form.Label>Chọn đợt kiểm tra (ngày kiểm tra)</Form.Label>
        <Form.Select
          value={selectedDate}
          onChange={e => setSelectedDate(e.target.value)}
          required
        >
          <option value="">Chọn ngày kiểm tra</option>
          {dates.map(date => (
            <option key={date} value={date}>
              {date ? new Date(date).toLocaleString('vi-VN') : ''}
            </option>
          ))}
        </Form.Select>
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Chọn học sinh</Form.Label>
        <Form.Select
          value={selectedStudent}
          onChange={e => setSelectedStudent(e.target.value)}
          required
          disabled={!selectedDate}
        >
          <option value="">Chọn học sinh</option>
          {filteredStudents.map(n => (
            <option key={n.studentId} value={n.studentId}>
              {n.studentName || n.studentId}
            </option>
          ))}
        </Form.Select>
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Kết quả kiểm tra</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          value={result}
          onChange={e => setResult(e.target.value)}
          required
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Ghi chú</Form.Label>
        <Form.Control
          as="textarea"
          rows={2}
          value={notes}
          onChange={e => setNotes(e.target.value)}
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Check
          type="checkbox"
          label="Có dấu hiệu bất thường"
          checked={abnormal}
          onChange={e => setAbnormal(e.target.checked)}
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Ngày kiểm tra</Form.Label>
        <Form.Control
          type="datetime-local"
          value={checkupDate}
          onChange={e => setCheckupDate(e.target.value)}
          required
        />
      </Form.Group>
      <Button type="submit" disabled={loading}>
        {loading ? <Spinner size="sm" /> : 'Lưu kết quả kiểm tra'}
      </Button>
    </Form>
  );
};

export default MedicalCheckupResult; 