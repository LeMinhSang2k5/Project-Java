import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Badge, Table } from 'react-bootstrap';
import { 
  FaHeart, 
  FaPills, 
  FaSyringe, 
  FaStethoscope, 
  FaExclamationTriangle,
  FaCheckCircle,
  FaClock
} from 'react-icons/fa';
import api from '../../config/api';
import './NurseDashboard.scss';

const NurseDashboard = () => {
  const [dashboardData, setDashboardData] = useState({
    totalIncidents: 0,
    pendingIncidents: 0,
    medicationRequests: 0,
    upcomingVaccinations: 0,
    scheduledHealthChecks: 0,
    recentIncidents: [],
    lowStockItems: []
  });

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      // Giả lập dữ liệu dashboard
      const mockData = {
        totalIncidents: 12,
        pendingIncidents: 3,
        medicationRequests: 5,
        upcomingVaccinations: 25,
        scheduledHealthChecks: 15,
        recentIncidents: [
          { id: 1, type: 'Sốt', student: 'Nguyễn Văn A', class: '10A1', time: '09:30', status: 'pending' },
          { id: 2, type: 'Té ngã', student: 'Trần Thị B', class: '9B2', time: '14:15', status: 'resolved' },
          { id: 3, type: 'Đau bụng', student: 'Lê Văn C', class: '11A3', time: '11:45', status: 'treating' }
        ],
        lowStockItems: [
          { name: 'Paracetamol', current: 15, minimum: 50, unit: 'viên' },
          { name: 'Băng gạc', current: 8, minimum: 20, unit: 'cuộn' },
          { name: 'Cồn y tế', current: 2, minimum: 10, unit: 'chai' }
        ]
      };
      setDashboardData(mockData);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'pending':
        return <Badge bg="warning">Chờ xử lý</Badge>;
      case 'treating':
        return <Badge bg="info">Đang điều trị</Badge>;
      case 'resolved':
        return <Badge bg="success">Đã xử lý</Badge>;
      default:
        return <Badge bg="secondary">Không xác định</Badge>;
    }
  };

  return (
    <div className="nurse-dashboard">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Dashboard Y tế</h2>
        <small className="text-muted">Cập nhật lần cuối: {new Date().toLocaleString('vi-VN')}</small>
      </div>

      {/* Thống kê tổng quan */}
      <Row className="mb-4">
        <Col md={3}>
          <Card className="stat-card border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="stat-icon bg-danger text-white me-3">
                <FaHeart />
              </div>
              <div>
                <h4 className="mb-0">{dashboardData.totalIncidents}</h4>
                <small className="text-muted">Sự kiện y tế hôm nay</small>
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="stat-card border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="stat-icon bg-warning text-white me-3">
                <FaExclamationTriangle />
              </div>
              <div>
                <h4 className="mb-0">{dashboardData.pendingIncidents}</h4>
                <small className="text-muted">Chờ xử lý</small>
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="stat-card border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="stat-icon bg-primary text-white me-3">
                <FaSyringe />
              </div>
              <div>
                <h4 className="mb-0">{dashboardData.upcomingVaccinations}</h4>
                <small className="text-muted">Tiêm chủng tuần này</small>
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="stat-card border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="stat-icon bg-info text-white me-3">
                <FaStethoscope />
              </div>
              <div>
                <h4 className="mb-0">{dashboardData.scheduledHealthChecks}</h4>
                <small className="text-muted">Kiểm tra y tế tuần này</small>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        {/* Sự kiện y tế gần đây */}
        <Col md={8}>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-white border-bottom">
              <h5 className="mb-0">
                <FaHeart className="me-2 text-danger" />
                Sự kiện y tế gần đây
              </h5>
            </Card.Header>
            <Card.Body>
              <Table responsive hover>
                <thead>
                  <tr>
                    <th>Thời gian</th>
                    <th>Loại sự kiện</th>
                    <th>Học sinh</th>
                    <th>Lớp</th>
                    <th>Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  {dashboardData.recentIncidents.map((incident) => (
                    <tr key={incident.id}>
                      <td>
                        <FaClock className="me-1 text-muted" />
                        {incident.time}
                      </td>
                      <td>{incident.type}</td>
                      <td>{incident.student}</td>
                      <td>{incident.class}</td>
                      <td>{getStatusBadge(incident.status)}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>

        {/* Thuốc sắp hết */}
        <Col md={4}>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-white border-bottom">
              <h5 className="mb-0">
                <FaPills className="me-2 text-warning" />
                Thuốc/Vật tư sắp hết
              </h5>
            </Card.Header>
            <Card.Body>
              {dashboardData.lowStockItems.map((item, index) => (
                <div key={index} className="d-flex justify-content-between align-items-center mb-3">
                  <div>
                    <div className="fw-bold">{item.name}</div>
                    <small className="text-muted">
                      Còn {item.current} {item.unit} / {item.minimum} {item.unit}
                    </small>
                  </div>
                  <Badge bg="danger">Cần bổ sung</Badge>
                </div>
              ))}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Lời khuyên nhanh */}
      <Row className="mt-4">
        <Col>
          <Card className="border-0 shadow-sm bg-light">
            <Card.Body>
              <h6 className="text-primary">
                <FaCheckCircle className="me-2" />
                Lời khuyên hôm nay
              </h6>
              <ul className="mb-0">
                <li>Kiểm tra nhiệt độ phòng y tế (18-25°C)</li>
                <li>Rửa tay thường xuyên và sử dụng khẩu trang khi cần</li>
                <li>Cập nhật hồ sơ sức khỏe học sinh định kỳ</li>
                <li>Kiểm tra hạn sử dụng thuốc và vật tư y tế</li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default NurseDashboard; 