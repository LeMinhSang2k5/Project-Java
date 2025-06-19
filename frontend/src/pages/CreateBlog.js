import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreateBlog = () => {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        title: "",
        content: "",
        author: "",
        category: "",
        thumbnail: "",
        publishDate: ""
    });
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            const response = await fetch("http://localhost:8080/api/blogs", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    ...form,
                    publishDate: form.publishDate // yyyy-MM-dd
                })
            });
            if (response.ok) {
                setMessage("Đăng bài thành công!");
                setTimeout(() => navigate("/blog"), 1200);
            } else {
                setMessage("Đăng bài thất bại!");
            }
        } catch (error) {
            setMessage("Có lỗi xảy ra!");
        }
    };

    return (
        <div style={{ maxWidth: 600, margin: "0 auto", padding: 24, background: "#f8f9fa", borderRadius: 8 }}>
            <h2>Tạo Bài Viết Mới</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Tiêu đề</label>
                    <input
                        type="text"
                        className="form-control"
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Nội dung</label>
                    <textarea
                        className="form-control"
                        name="content"
                        value={form.content}
                        onChange={handleChange}
                        rows="8"
                        required
                    ></textarea>
                </div>
                <div className="mb-3">
                    <label className="form-label">Tác giả</label>
                    <input
                        type="text"
                        className="form-control"
                        name="author"
                        value={form.author}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Danh mục</label>
                    <select
                        className="form-select"
                        name="category"
                        value={form.category}
                        onChange={handleChange}
                        required
                    >
                        <option value="">-- Chọn danh mục --</option>
                        <option value="Giáo dục">Giáo dục</option>
                        <option value="Sức khỏe">Sức khỏe</option>
                        <option value="Cộng đồng">Cộng đồng</option>
                        <option value="Công nghệ">Công nghệ</option>
                    </select>
                </div>
                <div className="mb-3">
                    <label className="form-label">Ảnh Thumbnail (URL)</label>
                    <input
                        type="text"
                        className="form-control"
                        name="thumbnail"
                        value={form.thumbnail}
                        onChange={handleChange}
                        placeholder="Nhập URL ảnh đại diện"
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Ngày xuất bản</label>
                    <input
                        type="date"
                        className="form-control"
                        name="publishDate"
                        value={form.publishDate}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-success w-100">Đăng bài</button>
            </form>
            {message && <div className="alert alert-info mt-3">{message}</div>}
        </div>
    );
};

export default CreateBlog;
