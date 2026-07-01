import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Badge, Table, Spinner } from 'react-bootstrap';
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
    upcomingVaccinations: 0,
    scheduledHealthChecks: 0,
    recentIncidents: [],
    lowStockItems: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [incidentsRes, vaccinationsRes, checkupsRes, lowStockRes] = await Promise.all([
        api.get('/health-incidents').catch(() => ({ data: [] })),
        api.get('/vaccination-schedules').catch(() => ({ data: [] })),
        api.get('/medical-checkup/notifications').catch(() => ({ data: [] })),
        api.get('/medical-supplies/low-stock/20').catch(() => ({ data: [] }))
      ]);

      const incidents = Array.isArray(incidentsRes.data) ? incidentsRes.data : [];
      const vaccinations = Array.isArray(vaccinationsRes.data) ? vaccinationsRes.data : [];
      const checkups = Array.isArray(checkupsRes.data) ? checkupsRes.data : [];
      const lowStock = Array.isArray(lowStockRes.data) ? lowStockRes.data : [];

      const pendingIncidents = incidents.filter((item) => item.status === 'PENDING' || item.status === 'IN_PROGRESS');
      const upcomingVaccinations = vaccinations.filter((item) => !item.isVaccinated);
      const scheduledHealthChecks = checkups.filter((item) => item.status === 'PENDING' || item.status === 'APPROVED');

      const recentIncidents = [...incidents]
        .sort((a, b) => new Date(b.incidentTime || 0) - new Date(a.incidentTime || 0))
        .slice(0, 5)
        .map((item) => ({
          id: item.id,
          type: item.incidentType || 'Không xác định',
          student: item.studentName || item.student?.fullName || 'N/A',
          class: item.className || 'N/A',
          time: item.incidentTime
            ? new Date(item.incidentTime).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
            : '--:--',
          status: (item.status || 'pending').toLowerCase()
        }));

      setDashboardData({
        totalIncidents: incidents.length,
        pendingIncidents: pendingIncidents.length,
        upcomingVaccinations: upcomingVaccinations.length,
        scheduledHealthChecks: scheduledHealthChecks.length,
        recentIncidents,
        lowStockItems: lowStock.map((item) => ({
          name: item.name,
          current: item.quantity,
          minimum: 20,
          unit: 'đơn vị'
        }))
      });
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'pending':
        return <Badge bg="warning">Chờ xử lý</Badge>;
      case 'in_progress':
      case 'treating':
        return <Badge bg="info">Đang điều trị</Badge>;
      case 'resolved':
        return <Badge bg="success">Đã xử lý</Badge>;
      default:
        return <Badge bg="secondary">Không xác định</Badge>;
    }
  };

  if (loading) {
    return (
      <div className="nurse-dashboard text-center py-5">
        <Spinner animation="border" />
        <p className="mt-2">Đang tải dashboard...</p>
      </div>
    );
  }

  return (
    <div className="nurse-dashboard">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Dashboard Y tế</h2>
        <small className="text-muted">Cập nhật lần cuối: {new Date().toLocaleString('vi-VN')}</small>
      </div>

      <Row className="mb-4">
        <Col md={3}>
          <Card className="stat-card border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="stat-icon bg-danger text-white me-3">
                <FaHeart />
              </div>
              <div>
                <h4 className="mb-0">{dashboardData.totalIncidents}</h4>
                <small className="text-muted">Tổng sự cố y tế</small>
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
                <small className="text-muted">Lịch tiêm sắp tới</small>
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
                <small className="text-muted">Thông báo khám SK</small>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={8}>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-white border-bottom">
              <h5 className="mb-0">
                <FaHeart className="me-2 text-danger" />
                Sự cố y tế gần đây
              </h5>
            </Card.Header>
            <Card.Body>
              {dashboardData.recentIncidents.length === 0 ? (
                <p className="text-muted mb-0">Chưa có sự cố y tế nào.</p>
              ) : (
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
              )}
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-white border-bottom">
              <h5 className="mb-0">
                <FaPills className="me-2 text-warning" />
                Thuốc/Vật tư sắp hết
              </h5>
            </Card.Header>
            <Card.Body>
              {dashboardData.lowStockItems.length === 0 ? (
                <p className="text-muted mb-0">Không có vật tư nào sắp hết.</p>
              ) : (
                dashboardData.lowStockItems.map((item, index) => (
                  <div key={index} className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                      <div className="fw-bold">{item.name}</div>
                      <small className="text-muted">
                        Còn {item.current} {item.unit}
                      </small>
                    </div>
                    <Badge bg="danger">Cần bổ sung</Badge>
                  </div>
                ))
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

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
