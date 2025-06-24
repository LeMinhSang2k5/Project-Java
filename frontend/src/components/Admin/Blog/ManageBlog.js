import React, { useState, useEffect }  from 'react';
import { FaPlus, FaEdit, FaTrash } from 'react-icons/fa';
import ModalCreateBlog from './ModalCreateBlog';
import ModalDeleteBlog from './ModalDeleteBlog';
import { Button } from 'react-bootstrap';
import axios from 'axios';
import ModalUpdateBlog from './ModalUpdateBlog';

const ManageBlog = () => {
    const [blogs, setBlogs] = useState([]);
    const [showCreate, setShowCreate] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showDelete, setShowDelete] = useState(false);
    const [selectedBlog, setSelectedBlog] = useState(null);

    // Hàm cắt ngắn nội dung
    const truncateContent = (content, maxLength = 100) => {
        if (content.length <= maxLength) return content;
        return content.substring(0, maxLength) + '...';
    };

    useEffect(() => {
        fetchBlogs();
    }, []);

    const fetchBlogs = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/blogs');
            setBlogs(response.data);
        } catch (error) {
            console.error('Error fetching blogs:', error);
        }
    };

    return (
        <>
            <h2>Quản lý Blog</h2>
            <div className="mb-3">
                <Button variant="success" onClick={() => setShowCreate(true)}>
                    <FaPlus className="me-2" /> Thêm Blog
                </Button>
            </div>

            <table className="table table-bordered table-hover mt-4">
                <thead className="table-light"> 
                    <tr>
                        <th>ID</th>
                        <th>Tiêu đề</th>
                        <th>Nội dung</th>
                        <th>Tác giả</th>
                        <th>Danh mục</th>
                        <th>Ngày tạo</th>
                        <th>Ngày cập nhật</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    {blogs.map((blog) => (
                        <tr key={blog.id}>
                            <td>{blog.id}</td>
                            <td>{blog.title}</td>
                            <td>{truncateContent(blog.content)}</td>
                            <td>{blog.author}</td>
                            <td>{blog.category}</td>
                            <td>{blog.createdAt}</td>
                            <td>{blog.updatedAt}</td>
                            <td>
                                <Button
                                variant="warning"
                                size="sm"
                                className="me-2 btn-edit"
                                onClick={() => { setSelectedBlog(blog); setShowEdit(true); }}
                                >
                                <FaEdit /> Sửa
                                </Button>
                                <Button
                                variant="danger"
                                size="sm"
                                className="btn-delete"
                                onClick={() => { setSelectedBlog(blog); setShowDelete(true); }}
                                >
                                <FaTrash /> Xóa
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <ModalCreateBlog
                show={showCreate}
                onClose={() => setShowCreate(false)}
                onBlogAdded={fetchBlogs}
            />

            <ModalUpdateBlog
                show={showEdit}
                onClose={() => setShowEdit(false)}
                onBlogUpdated={fetchBlogs}
                blog={selectedBlog}
            />

            <ModalDeleteBlog
                show={showDelete}
                onClose={() => setShowDelete(false)}
                onBlogDeleted={fetchBlogs}
                blog={selectedBlog}
            />
        </>
    );
};

export default ManageBlog;