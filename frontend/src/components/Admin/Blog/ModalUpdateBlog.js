import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import api from '../../../config/api';
import { toast } from 'react-toastify';

function ModalUpdateBlog({ show, onClose, onBlogUpdated, blog }) {
  const [form, setForm] = useState({ 
    title: '', 
    content: '', 
    author: '', 
    category: '',
    thumbnail: null,
    publishDate: '',
    createdAt: '',
    updatedAt: ''
  });
  const [loading, setLoading] = useState(false);
  const [validated, setValidated] = useState(false);

  useEffect(() => {
    if (blog) {
      setForm({
        title: blog.title || '',
        content: blog.content || '',
        author: blog.author || '',
        category: blog.category || '',
        thumbnail: blog.thumbnail || null,
        publishDate: blog.publishDate || '',
        createdAt: blog.createdAt || '',
        updatedAt: blog.updatedAt || ''
      });
    }
  }, [blog]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value === '' ? (name === 'thumbnail' ? null : '') : value });
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
      // Chỉ gửi các trường cần cập nhật
      const blogData = {
        id: blog.id,
        title: form.title.trim(),
        content: form.content.trim(),
        author: form.author.trim(),
        category: form.category.trim(),
        thumbnail: form.thumbnail?.trim() || null,
        // Giữ nguyên các trường thời gian
        publishDate: form.publishDate,
        createdAt: form.createdAt,
        updatedAt: new Date().toISOString() // Chỉ cập nhật updatedAt
      };

      const response = await api.put(`/blogs/${blog.id}`, blogData);
      if (response.data) {
        toast.success(`Cập nhật bài viết "${form.title}" thành công`);
        setValidated(false);
        onBlogUpdated && onBlogUpdated();
        onClose();
      }
    } catch (err) {
      console.error('Error:', err.response?.data);
      if (err.response?.status === 413) {
        toast.error('Nội dung bài viết quá lớn. Vui lòng giảm kích thước nội dung.');
      } else if (err.response?.data?.message) {
        toast.error(`Lỗi: ${err.response.data.message}`);
      } else if (err.message === 'Network Error') {
        toast.error('Lỗi kết nối đến server. Vui lòng kiểm tra kết nối mạng và thử lại.');
      } else {
        toast.error('Có lỗi xảy ra khi cập nhật bài viết. Vui lòng thử lại sau.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered size="lg">
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>Chỉnh sửa bài viết</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Tiêu đề <span className="text-danger">*</span></Form.Label>
            <Form.Control 
              type="text" 
              name="title" 
              value={form.title} 
              onChange={handleChange} 
              required 
              minLength={5}
              maxLength={200}
            />
            <Form.Control.Feedback type="invalid">
              Vui lòng nhập tiêu đề (5-200 ký tự)
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Nội dung <span className="text-danger">*</span></Form.Label>
            <Form.Control 
              as="textarea" 
              rows={15}
              name="content" 
              value={form.content} 
              onChange={handleChange} 
              required 
              style={{ minHeight: '300px', fontSize: '1rem' }}
              placeholder="Nhập nội dung bài viết..."
            />
            <Form.Text className="text-muted">
              Nhập nội dung chi tiết của bài viết
            </Form.Text>
            <Form.Control.Feedback type="invalid">
              Vui lòng nhập nội dung
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Tác giả <span className="text-danger">*</span></Form.Label>
            <Form.Control 
              type="text" 
              name="author" 
              value={form.author} 
              onChange={handleChange} 
              required 
            />
            <Form.Control.Feedback type="invalid">
              Vui lòng nhập tên tác giả
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Danh mục <span className="text-danger">*</span></Form.Label>
            <Form.Select 
              name="category" 
              value={form.category} 
              onChange={handleChange} 
              required
            >
              <option value="">-- Chọn danh mục --</option>
              <option value="Giáo dục">Giáo dục</option>
              <option value="Sức khỏe">Sức khỏe</option>
              <option value="Tin tức">Tin tức</option>
              <option value="Hoạt động">Hoạt động</option>
            </Form.Select>
            <Form.Control.Feedback type="invalid">
              Vui lòng chọn danh mục
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>URL Ảnh đại diện</Form.Label>
            <Form.Control 
              type="text" 
              name="thumbnail" 
              value={form.thumbnail || ''} 
              onChange={handleChange} 
              placeholder="https://example.com/image.jpg"
            />
            <Form.Text className="text-muted">
              Có thể để trống
            </Form.Text>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose} disabled={loading}>
            Đóng
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Đang xử lý...' : 'Cập nhật'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default ModalUpdateBlog; 