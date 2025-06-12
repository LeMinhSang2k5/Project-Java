import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CreateBlog.scss";

const CreateBlog = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: "",
        content: "",
        author: "",
        category: "",
        thumbnail: null,
    });

    const handleChange = (e) => {
        const { name, value, files } = e.target;
        if (name === "thumbnail") {
            setFormData({ ...formData, thumbnail: files[0] });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const data = new FormData();
        data.append("title", formData.title);
        data.append("content", formData.content);
        data.append("author", formData.author);
        data.append("category", formData.category);
        if (formData.thumbnail) {
            data.append("thumbnail", formData.thumbnail);
        }

        try {
            const response = await fetch("http://localhost:8080/api/blogs", {
                method: "POST",
                body: data,
            });
            if (response.ok) {
                alert("Đăng bài thành công!");
                navigate("/blog"); // Chuyển hướng về trang blog sau khi đăng thành công
            } else {
                alert("Đăng bài thất bại!");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Có lỗi xảy ra!");
        }
    };

    return (
        <div className="create-blog-container">
            <h1>Tạo Bài Viết Mới</h1>
            <form onSubmit={handleSubmit} className="blog-form">
                <div className="form-group">
                    <label htmlFor="title">Tiêu đề</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        required
                        value={formData.title}
                        onChange={handleChange}
                        placeholder="Nhập tiêu đề bài viết"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="content">Nội dung</label>
                    <textarea
                        id="content"
                        name="content"
                        required
                        value={formData.content}
                        onChange={handleChange}
                        placeholder="Nhập nội dung bài viết"
                        rows="10"
                    ></textarea>
                </div>

                <div className="form-group">
                    <label htmlFor="author">Tác giả</label>
                    <input
                        type="text"
                        id="author"
                        name="author"
                        required
                        value={formData.author}
                        onChange={handleChange}
                        placeholder="Nhập tên tác giả"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="category">Danh mục</label>
                    <select
                        id="category"
                        name="category"
                        required
                        value={formData.category}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn danh mục --</option>
                        <option value="Giáo dục">Giáo dục</option>
                        <option value="Sức khỏe">Sức khỏe</option>
                        <option value="Cộng đồng">Cộng đồng</option>
                        <option value="Công nghệ">Công nghệ</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="thumbnail">Ảnh Thumbnail</label>
                    <input
                        type="file"
                        id="thumbnail"
                        name="thumbnail"
                        accept="image/*"
                        onChange={handleChange}
                        className="file-input"
                    />
                </div>

                <div className="form-actions">
                    <button type="submit" className="submit-btn">Đăng bài</button>
                    <button type="button" className="cancel-btn" onClick={() => navigate("/blog")}>
                        Hủy
                    </button>
                </div>
            </form>
        </div>
    );
};

export default CreateBlog;