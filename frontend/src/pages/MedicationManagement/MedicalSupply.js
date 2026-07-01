import React, { useState } from "react";
import api from "../../config/api";
import { toast } from "react-toastify";

const MedicalSupply = () => {
    const [form, setForm] = useState({
        name: "",
        quantity: "",
        category: "Thuốc",
        expiryDate: ""
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setLoading(true);
        try {
            await api.post("/medical-supplies", {
                name: form.name.trim(),
                quantity: Number(form.quantity),
                category: form.category,
                expiryDate: form.expiryDate || null
            });
            setMessage("Thêm thành công!");
            toast.success("Thêm vật tư y tế thành công!");
            setForm({ name: "", quantity: "", category: "Thuốc", expiryDate: "" });
        } catch (error) {
            const errMsg = error.response?.data?.message || "Có lỗi xảy ra khi thêm!";
            setMessage(errMsg);
            toast.error(errMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "0 auto", padding: 24, background: "#f8f9fa", borderRadius: 8 }}>
            <h2>Thêm thuốc / vật tư y tế</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Tên</label>
                    <input
                        className="form-control"
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Số lượng</label>
                    <input
                        className="form-control"
                        name="quantity"
                        type="number"
                        min="1"
                        value={form.quantity}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Loại</label>
                    <select
                        className="form-select"
                        name="category"
                        value={form.category}
                        onChange={handleChange}
                    >
                        <option value="Thuốc">Thuốc</option>
                        <option value="Vật tư y tế">Vật tư y tế</option>
                    </select>
                </div>
                <div className="mb-3">
                    <label className="form-label">Hạn sử dụng</label>
                    <input
                        className="form-control"
                        name="expiryDate"
                        type="date"
                        value={form.expiryDate}
                        onChange={handleChange}
                    />
                </div>
                <button className="btn btn-primary w-100" type="submit" disabled={loading}>
                    {loading ? "Đang lưu..." : "Thêm mới"}
                </button>
                {message && <p className="mt-3 text-center">{message}</p>}
            </form>
        </div>
    );
};

export default MedicalSupply;
