import React, { useState } from 'react';
import { Modal, Button, Form, Alert, Table } from 'react-bootstrap';
import { FaFileExcel, FaDownload, FaUpload } from 'react-icons/fa';
import * as XLSX from 'xlsx';
import { toast } from 'react-toastify';
import api from '../../../config/api';

const ModalImportExcel = ({ show, onClose, onUsersImported }) => {
    const [file, setFile] = useState(null);
    const [previewData, setPreviewData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState([]);

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            if (!selectedFile.name.match(/\.(xlsx|xls)$/)) {
                toast.error('Vui lòng chọn file Excel (.xlsx hoặc .xls)');
                return;
            }
            setFile(selectedFile);
            readExcelFile(selectedFile);
        }
    };

    const readExcelFile = (file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            try {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, { type: 'array' });
                const sheetName = workbook.SheetNames[0];
                const worksheet = workbook.Sheets[sheetName];
                
                // Chuyển đổi sang JSON với header
                const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
                
                if (jsonData.length < 2) {
                    toast.error('File Excel phải có ít nhất 2 dòng (header + data)');
                    return;
                }

                // Lấy header (dòng đầu tiên)
                const headers = jsonData[0];
                
                // Kiểm tra các cột bắt buộc cho Student
                const requiredColumns = ['email', 'fullName', 'password', 'code', 'studentClass'];
                const missingColumns = requiredColumns.filter(col => 
                    !headers.some(header => 
                        header && header.toString().toLowerCase().includes(col.toLowerCase())
                    )
                );

                if (missingColumns.length > 0) {
                    toast.error(`File thiếu các cột bắt buộc: ${missingColumns.join(', ')}`);
                    return;
                }

                // Chuyển đổi dữ liệu thành object Student
                const students = [];
                for (let i = 1; i < jsonData.length; i++) {
                    const row = jsonData[i];
                    if (row.some(cell => cell)) { // Bỏ qua dòng trống
                        const student = {};
                        headers.forEach((header, index) => {
                            if (header && row[index] !== undefined) {
                                const key = header.toString().toLowerCase();
                                if (key.includes('email')) student.email = row[index];
                                else if (key.includes('fullname') || key.includes('name')) student.fullName = row[index];
                                else if (key.includes('password')) student.password = row[index];
                                else if (key.includes('code') || key.includes('maso')) student.code = row[index];
                                else if (key.includes('class') || key.includes('lop')) student.studentClass = row[index];
                                else if (key.includes('gender') || key.includes('gioitinh')) {
                                    const genderValue = row[index]?.toString().toLowerCase();
                                    if (genderValue === 'nam' || genderValue === 'male' || genderValue === 'm') {
                                        student.gender = 'MALE';
                                    } else if (genderValue === 'nữ' || genderValue === 'nu' || genderValue === 'female' || genderValue === 'f') {
                                        student.gender = 'FEMALE';
                                    }
                                }
                                else if (key.includes('birthday') || key.includes('dateofbirth') || key.includes('ngaysinh')) {
                                    // Xử lý ngày sinh
                                    if (row[index]) {
                                        const date = new Date(row[index]);
                                        if (!isNaN(date.getTime())) {
                                            student.dateOfBirth = date.toISOString().split('T')[0]; // Format YYYY-MM-DD
                                        }
                                    }
                                }
                            }
                        });
                        
                        // Đặt giá trị mặc định nếu không có
                        if (!student.gender) student.gender = 'MALE';
                        if (!student.dateOfBirth) student.dateOfBirth = '2010-01-01'; // Ngày sinh mặc định
                        
                        if (student.email && student.fullName && student.password && student.code && student.studentClass) {
                            students.push(student);
                        }
                    }
                }

                setPreviewData(students);
                setErrors([]);
            } catch (error) {
                toast.error('Lỗi khi đọc file Excel: ' + error.message);
            }
        };
        reader.readAsArrayBuffer(file);
    };

    const validateData = (students) => {
        const errors = [];
        students.forEach((student, index) => {
            const rowNum = index + 2; // +2 vì bắt đầu từ dòng 2 trong Excel
            
            if (!student.email || !student.email.includes('@')) {
                errors.push(`Dòng ${rowNum}: Email không hợp lệ`);
            }
            
            if (!student.fullName || student.fullName.trim().length < 2) {
                errors.push(`Dòng ${rowNum}: Họ tên không hợp lệ`);
            }
            
            if (!student.password || student.password.length < 6) {
                errors.push(`Dòng ${rowNum}: Mật khẩu phải có ít nhất 6 ký tự`);
            }

            if (!student.code || student.code.trim().length === 0) {
                errors.push(`Dòng ${rowNum}: Mã số sinh viên không được để trống`);
            }

            if (!student.studentClass || student.studentClass.trim().length === 0) {
                errors.push(`Dòng ${rowNum}: Lớp học không được để trống`);
            }
        });
        return errors;
    };

    const handleImport = async () => {
        if (!previewData.length) {
            toast.error('❌ Không có dữ liệu để nhập', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
            return;
        }

        const validationErrors = validateData(previewData);
        if (validationErrors.length > 0) {
            setErrors(validationErrors);
            toast.error(`❌ Có ${validationErrors.length} lỗi dữ liệu cần sửa`, {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
            return;
        }

        setLoading(true);
        try {
            // Gửi từng học sinh một cách tuần tự để tránh lỗi
            let successCount = 0;
            let errorCount = 0;
            const errorDetails = [];
            
            for (let i = 0; i < previewData.length; i++) {
                try {
                    await api.post('/students', previewData[i]); // Sử dụng endpoint students
                    successCount++;
                } catch (error) {
                    errorCount++;
                    errorDetails.push(`${previewData[i].fullName} (${previewData[i].email})`);
                    console.error('Lỗi khi tạo học sinh:', error);
                }
            }

            // Toast thành công chi tiết
            if (successCount > 0) {
                toast.success(
                    `🎉 Đã nhập thành công ${successCount}/${previewData.length} học sinh!`, 
                    {
                        position: "top-right",
                        autoClose: 4000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                    }
                );
                onUsersImported && onUsersImported();
                onClose();
            }
            
            // Toast cảnh báo nếu có lỗi
            if (errorCount > 0) {
                toast.warning(
                    `⚠️ ${errorCount} học sinh không thể nhập (có thể đã tồn tại hoặc lỗi dữ liệu)`, 
                    {
                        position: "top-right",
                        autoClose: 6000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                    }
                );
            }
            
        } catch (error) {
            toast.error(
                `❌ Lỗi hệ thống: ${error.message}`, 
                {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                }
            );
        } finally {
            setLoading(false);
        }
    };

    const downloadTemplate = () => {
        const templateData = [
            ['email', 'fullName', 'password', 'code', 'studentClass', 'gender', 'dateOfBirth'],
            ['hocsinh1@example.com', 'Nguyễn Văn A', 'password123', 'SV2024001', '10A1', 'MALE', '2010-01-15'],
            ['hocsinh2@example.com', 'Trần Thị B', 'password123', 'SV2024002', '10A2', 'FEMALE', '2010-03-20'],
        ];
        
        const ws = XLSX.utils.aoa_to_sheet(templateData);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'DanhSachHocSinh');
        XLSX.writeFile(wb, 'mau_danh_sach_hoc_sinh.xlsx');
    };

    const handleClose = () => {
        setFile(null);
        setPreviewData([]);
        setErrors([]);
        onClose();
    };

    return (
        <Modal show={show} onHide={handleClose} size="lg" centered>
            <Modal.Header closeButton>
                <Modal.Title>
                    <FaFileExcel className="me-2 text-success" />
                    Nhập danh sách học sinh từ Excel
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="mb-3">
                    <Button 
                        variant="outline-info" 
                        onClick={downloadTemplate}
                        className="mb-3"
                    >
                        <FaDownload className="me-2" />
                        Tải mẫu file Excel
                    </Button>
                </div>

                <Form.Group className="mb-3">
                    <Form.Label>Chọn file Excel</Form.Label>
                    <Form.Control
                        type="file"
                        accept=".xlsx,.xls"
                        onChange={handleFileChange}
                    />
                    <Form.Text className="text-muted">
                        File phải chứa các cột: email, fullName, password, code, studentClass, gender (tùy chọn), dateOfBirth (tùy chọn)
                    </Form.Text>
                </Form.Group>

                {errors.length > 0 && (
                    <Alert variant="danger">
                        <strong>Có lỗi trong dữ liệu:</strong>
                        <ul className="mb-0 mt-2">
                            {errors.slice(0, 10).map((error, index) => (
                                <li key={index}>{error}</li>
                            ))}
                            {errors.length > 10 && <li>... và {errors.length - 10} lỗi khác</li>}
                        </ul>
                    </Alert>
                )}

                {previewData.length > 0 && (
                    <div>
                        <h6>Xem trước dữ liệu ({previewData.length} học sinh):</h6>
                        <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                            <Table striped bordered hover size="sm">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Email</th>
                                        <th>Họ tên</th>
                                        <th>Mã số SV</th>
                                        <th>Lớp</th>
                                        <th>Giới tính</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {previewData.slice(0, 5).map((student, index) => (
                                        <tr key={index}>
                                            <td>{index + 1}</td>
                                            <td>{student.email}</td>
                                            <td>{student.fullName}</td>
                                            <td>{student.code}</td>
                                            <td>{student.studentClass}</td>
                                            <td>{student.gender === 'MALE' ? 'Nam' : 'Nữ'}</td>
                                        </tr>
                                    ))}
                                    {previewData.length > 5 && (
                                        <tr>
                                            <td colSpan="6" className="text-center text-muted">
                                                ... và {previewData.length - 5} học sinh khác
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </Table>
                        </div>
                    </div>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose} disabled={loading}>
                    Đóng
                </Button>
                <Button 
                    variant="success" 
                    onClick={handleImport} 
                    disabled={loading || previewData.length === 0 || errors.length > 0}
                >
                    {loading ? (
                        <>Đang nhập...</>
                    ) : (
                        <>
                            <FaUpload className="me-2" />
                            Nhập {previewData.length} học sinh
                        </>
                    )}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalImportExcel; 