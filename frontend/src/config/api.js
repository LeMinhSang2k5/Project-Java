import axios from 'axios';

// Tạo instance axios với cấu hình mặc định
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Thêm interceptor để xử lý request
api.interceptors.request.use(
    (config) => {
        // Thêm token vào header nếu cần
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Thêm interceptor để xử lý response
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            // Xử lý lỗi từ server
            switch (error.response.status) {
                case 401:
                    // Xử lý lỗi unauthorized
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                    break;
                case 403:
                    // Xử lý lỗi forbidden
                    console.error('Access denied');
                    break;
                default:
                    console.error('Server error:', error.response.data);
            }
        }
        return Promise.reject(error);
    }
);

export default api; 