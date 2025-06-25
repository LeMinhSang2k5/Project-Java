import React, { useState, useEffect } from 'react';
import './BlogPage.scss';
import { Pagination } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';

const BlogPage = () => {
  const [blogs, setBlogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const blogsPerPage = 6; // Hiển thị 6 blogs mỗi trang (2 hàng x 3 cột)

  const fetchBlogs = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/api/blogs');
      setBlogs(response.data);
      setError(null);
    } catch (error) {
      console.error('Error fetching blogs:', error);
      setError('Không thể tải danh sách bài viết. Vui lòng thử lại sau.');
      toast.error('Có lỗi xảy ra khi tải danh sách bài viết');
    } finally {
      setLoading(false);
    }
  };

  // Gọi fetchBlogs khi component được mount
  useEffect(() => {
    fetchBlogs();

    // Thiết lập interval để tự động cập nhật mỗi 30 giây
    const interval = setInterval(fetchBlogs, 30000);

    // Cleanup interval khi component unmount
    return () => clearInterval(interval);
  }, []);

  // Tính toán số trang
  const totalPages = Math.ceil(blogs.length / blogsPerPage);

  // Lấy blogs cho trang hiện tại
  const indexOfLastBlog = currentPage * blogsPerPage;
  const indexOfFirstBlog = indexOfLastBlog - blogsPerPage;
  const currentBlogs = blogs.slice(indexOfFirstBlog, indexOfLastBlog);

  // Xử lý khi chuyển trang
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
    window.scrollTo(0, 0); // Cuộn lên đầu trang khi chuyển trang
  };

  if (loading) {
    return (
      <div className="blogs-page-container">
        <div className="loading-spinner">Đang tải bài viết...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="blogs-page-container">
        <div className="error-message">{error}</div>
        <button onClick={fetchBlogs} className="retry-button">
          Thử lại
        </button>
      </div>
    );
  }

  return (
    <div className="blogs-page-container">
      <div className="blogs-grid">
        {currentBlogs.map((blog) => (
          <div key={blog.id} className="blog-container">
            <div className="blog-card">
              <img 
                src={blog.thumbnail || '/default-blog-image.jpg'} 
                alt={blog.title}
                className="blog-image" 
                onError={(e) => {
                  e.target.src = '/default-blog-image.jpg';
                }}
              />
              <div className="blog-card-content">
                <h3>{blog.title}</h3>
                <p className="blog-meta">
                  Bởi {blog.author} • {new Date(blog.publishDate).toLocaleDateString('vi-VN')}
                </p>
                <p className="blog-excerpt">{blog.content}</p>
                <a href={`/blog/${blog.id}`}>Đọc thêm</a>
              </div>
            </div>
          </div>
        ))}
      </div>
      
      {totalPages > 1 && (
        <div className="pagination-container">
          <Pagination>
            <Pagination.First 
              onClick={() => handlePageChange(1)} 
              disabled={currentPage === 1}
            />
            <Pagination.Prev 
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
            />
            
            {[...Array(totalPages)].map((_, index) => (
              <Pagination.Item
                key={index + 1}
                active={index + 1 === currentPage}
                onClick={() => handlePageChange(index + 1)}
              >
                {index + 1}
              </Pagination.Item>
            ))}
            
            <Pagination.Next 
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
            />
            <Pagination.Last 
              onClick={() => handlePageChange(totalPages)}
              disabled={currentPage === totalPages}
            />
          </Pagination>
        </div>
      )}
    </div>
  );
};

export default BlogPage;

