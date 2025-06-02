import React, { useState } from 'react';
import './Medical.scss'

const Medical = () => {
    const [formData, setFormData] = useState({
        childName: '',
        parentName: '',
        medicineName: '',
        dosage: '',
        notes: '',
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Form data submitted:", formData);
        alert("Gửi đơn thuốc thành công!");
    };

    return (
        <div className="medical-form-container">
            <h2>Gửi thuốc cho con</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Tên trẻ:
                    <input type="text" name="childName" value={formData.childName} onChange={handleChange} required />
                </label>
                <label>
                    Tên phụ huynh:
                    <input type="text" name="parentName" value={formData.parentName} onChange={handleChange} required />
                </label>
                <label>
                    Tên thuốc:
                    <input type="text" name="medicineName" value={formData.medicineName} onChange={handleChange} required />
                </label>
                <label>
                    Liều lượng:
                    <input type="text" name="dosage" value={formData.dosage} onChange={handleChange} required />
                </label>
                <label>
                    Ghi chú:
                    <textarea name="notes" value={formData.notes} onChange={handleChange} />
                </label>
                <button type="submit">Gửi</button>
            </form>
        </div>
    );
};

export default Medical;
