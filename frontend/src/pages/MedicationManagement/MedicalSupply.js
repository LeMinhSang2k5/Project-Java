import React, { useState } from "react";

const MedicalSupply = () => {
    const [form, setForm] = useState({
        name: "",
        quantity: "",
        unit: "",
        category: "Thuốc"
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
            const response = await fetch("http://localhost:8080/api/medical-supplies", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: form.name,
                    quantity: Number(form.quantity),
                    unit: form.unit,
                    category: form.category
                })
            });
            if (response.ok) {
                setMessage("Thêm thành công!");
                setForm({ name: "", quantity: "", unit: "", category: "Thuốc" });
            } else {
                setMessage("Có lỗi xảy ra khi thêm!");
            }
        } catch (error) {
            setMessage("Không thể kết nối tới server!");
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "0 auto", padding: 24, background: "#f8f9fa", borderRadius: 8 }}>
            <h2>Thêm thuốc / vật tư y tế</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Tên</label>
                    <input
                        type="text"
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
                        type="number"
                        className="form-control"
                        name="quantity"
                        value={form.quantity}
                        onChange={handleChange}
                        min="1"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Đơn vị</label>
                    <input
                        type="text"
                        className="form-control"
                        name="unit"
                        value={form.unit}
                        onChange={handleChange}
                        placeholder="viên, hộp, ml..."
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
                <button type="submit" className="btn btn-success w-100">Thêm</button>
            </form>
            {message && <div className="alert alert-info mt-3">{message}</div>}
        </div>
    );
};

export default MedicalSupply;
