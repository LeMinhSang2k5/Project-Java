import React, { useState, useEffect } from 'react';
import api from '../config/api';
import './General.scss';

const General = () => {
    const [docs, setDocs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState('Sức khỏe học đường');
    const [filteredDocs, setFilteredDocs] = useState([]);

    const categories = [
        { id: 'suc-khoe-hoc-duong', name: 'Sức khỏe học đường', description: 'Tài liệu về chăm sóc sức khỏe cho học sinh' },
        { id: 'dinh-duong-hoc-duong', name: 'Dinh dưỡng học đường', description: 'Hướng dẫn về dinh dưỡng và bữa ăn học đường' },
        { id: 'phong-chong-dich-benh', name: 'Phòng chống dịch bệnh', description: 'Thông tin về phòng chống dịch bệnh trong trường học' }
    ];

    useEffect(() => {
        const fetchDocs = async () => {
            try {
                const response = await api.get('/school-health-docs');
                setDocs(response.data);
            } catch (error) {
                console.error('Error fetching school health docs:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchDocs();
    }, []);

    useEffect(() => {
        let filtered = [];
        switch (selectedCategory) {
            case 'Sức khỏe học đường':
                filtered = docs.filter(doc => doc.title && doc.title.toLowerCase().includes('sức khỏe'));
                break;
            case 'Dinh dưỡng học đường':
                filtered = docs.filter(doc => {
                    const title = (doc.title || '').toLowerCase();
                    return (
                        (title.includes('dinh dưỡng') ||
                        title.includes('ăn uống') ||
                        title.includes('thực phẩm') ||
                        title.includes('bữa ăn') ||
                        title.includes('chế độ ăn')) &&
                        !title.includes('sức khỏe')
                    );
                });
                break;
            case 'Phòng chống dịch bệnh':
                filtered = docs.filter(doc =>
                    doc.title && (doc.title.toLowerCase().includes('dịch bệnh') || doc.title.toLowerCase().includes('phòng chống'))
                );
                break;
            default:
                filtered = docs;
        }
        setFilteredDocs(filtered);
    }, [docs, selectedCategory]);

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN');
    };

    if (loading) {
        return (
            <div className="general-page">
                <div className="loading-container">
                    <div className="spinner"></div>
                    <p>Đang tải tài liệu...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="general-page">
            <div className="general-container">
                <div className="general-header">
                    <h1>Tài liệu học đường</h1>
                    <p>Thư viện tài liệu về sức khỏe, dinh dưỡng và phòng chống dịch bệnh trong môi trường học đường</p>
                </div>
                <div className="general-content">
                    <div className="general-sidebar">
                        <div className="sidebar-header">
                            <h3>Danh mục tài liệu</h3>
                        </div>
                        <div className="category-list">
                            {categories.map((category) => (
                                <div
                                    key={category.id}
                                    className={`category-item ${selectedCategory === category.name ? 'active' : ''}`}
                                    onClick={() => setSelectedCategory(category.name)}
                                >
                                    <div className="category-info">
                                        <h4>{category.name}</h4>
                                        <p>{category.description}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="general-main">
                        <div className="content-header">
                            <h2>{selectedCategory}</h2>
                            <p>Tìm thấy {filteredDocs.length} tài liệu</p>
                        </div>
                        {filteredDocs.length === 0 ? (
                            <div className="no-content">
                                <div className="no-content-icon">📄</div>
                                <h3>Chưa có tài liệu</h3>
                                <p>Hiện tại chưa có tài liệu nào trong danh mục "{selectedCategory}". Vui lòng quay lại sau.</p>
                            </div>
                        ) : (
                            <div className="documents-grid">
                                {filteredDocs.map((doc) => (
                                    <div key={doc.id} className="document-card">
                                        <div className="document-content">
                                            <h3>{doc.title}</h3>
                                            <div className="document-meta">
                                                <span className="date">📅 {formatDate(doc.createdDate || doc.created_at)}</span>
                                            </div>
                                            <div className="document-full-content" style={{ margin: '16px 0' }}>
                                                <span dangerouslySetInnerHTML={{ __html: doc.content }} />
                                            </div>
                                            {doc.url && (
                                                <a href={doc.url} target="_blank" rel="noopener noreferrer" className="read-more-btn">
                                                    Xem tài liệu
                                                </a>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default General;
