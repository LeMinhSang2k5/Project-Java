import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';
import './BlogDetail.scss';

const BlogDetail = () => {
  const { id } = useParams();
  const [blog, setBlog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBlogDetail = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`http://localhost:8080/api/blogs/${id}`);
        setBlog(response.data);
        setError(null);
      } catch (error) {
        console.error('Error fetching blog details:', error);
        setError('Không thể tải thông tin bài viết. Vui lòng thử lại sau.');
        toast.error('Có lỗi xảy ra khi tải thông tin bài viết');
      } finally {
        setLoading(false);
      }
    };

    fetchBlogDetail();
  }, [id]);

  if (loading) {
    return (
      <div className="blog-detail-container">
        <div className="loading-spinner">Đang tải bài viết...</div>
      </div>
    );
  }

  if (error || !blog) {
    return (
      <div className="blog-detail-container">
        <div className="error-message">
          {error || 'Không tìm thấy bài viết'}
        </div>
        <button onClick={() => window.history.back()} className="back-button">
          Quay lại
        </button>
      </div>
    );
  }

  return (
    <div className="blog-detail-container">
      <div className="blog-detail-content">
        <div className="blog-detail-header">
          <h1>{blog.title}</h1>
          <div className="blog-meta">
            <span className="author">Tác giả: {blog.author}</span>
            <span className="date">
              Ngày đăng: {new Date(blog.publishDate).toLocaleDateString('vi-VN')}
            </span>
            <span className="category">Danh mục: {blog.category}</span>
          </div>
        </div>

        {blog.thumbnail && (
          <div className="blog-thumbnail">
            <img 
              src={blog.thumbnail} 
              alt={blog.title}
              onError={(e) => {
                e.target.src = '/default-blog-image.jpg';
              }}
            />
          </div>
        )}

        <div className="blog-content">
          {blog.content.split('\n').map((paragraph, index) => (
            paragraph ? <p key={index}>{paragraph}</p> : <br key={index} />
          ))}
        </div>

        <div className="blog-footer">
          <button onClick={() => window.history.back()} className="back-button">
            Quay lại danh sách
          </button>
          <div className="blog-timestamps">
            <p>Ngày tạo: {new Date(blog.createdAt).toLocaleString('vi-VN')}</p>
            <p>Cập nhật lần cuối: {new Date(blog.updatedAt).toLocaleString('vi-VN')}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BlogDetail; 