import React, { useState, useEffect } from 'react';
import { 
  FaUsers, 
  FaBlog, 
  FaMedkit, 
  FaChartLine, 
  FaCalendarCheck,
  FaUserGraduate,
  FaUserTie,
  FaUserNurse,
  FaExclamationTriangle,
  FaCheckCircle,
  FaClock
} from 'react-icons/fa';
import api from '../../config/api';
import './Admin.scss';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalStudents: 0,
    totalNurses: 0,
    totalParents: 0,
    totalBlogs: 0,
    totalMedicalSupplies: 0,
    pendingRequests: 0,
    completedRequests: 0,
    activeIncidents: 0,
    todayAppointments: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [usersRes, studentsRes, blogsRes, suppliesRes, incidentsRes, pendingMedicalRes] =
          await Promise.all([
            api.get('/users?page=0').catch(() => ({ data: { totalItems: 0, content: [] } })),
            api.get('/students').catch(() => ({ data: [] })),
            api.get('/blogs').catch(() => ({ data: [] })),
            api.get('/medical-supplies').catch(() => ({ data: [] })),
            api.get('/health-incidents').catch(() => ({ data: [] })),
            api.get('/medical/status/PENDING').catch(() => ({ data: [] }))
          ]);

        const users = usersRes.data?.content || [];
        const students = Array.isArray(studentsRes.data) ? studentsRes.data : [];
        const blogs = Array.isArray(blogsRes.data) ? blogsRes.data : [];
        const supplies = Array.isArray(suppliesRes.data) ? suppliesRes.data : [];
        const incidents = Array.isArray(incidentsRes.data) ? incidentsRes.data : [];
        const pendingMedical = Array.isArray(pendingMedicalRes.data) ? pendingMedicalRes.data : [];

        setStats({
          totalUsers: usersRes.data?.totalItems ?? users.length,
          totalStudents: students.length,
          totalNurses: users.filter((u) => u.role === 'SCHOOL_NURSE').length,
          totalParents: users.filter((u) => u.role === 'PARENT').length,
          totalBlogs: blogs.length,
          totalMedicalSupplies: supplies.length,
          pendingRequests: pendingMedical.length,
          completedRequests: 0,
          activeIncidents: incidents.filter((i) => i.status !== 'RESOLVED').length,
          todayAppointments: 0
        });
      } catch (error) {
        console.error('Error loading dashboard stats:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  const StatCard = ({ icon: Icon, title, value, color, bgColor }) => (
    <div className="stat-card" style={{ backgroundColor: bgColor }}>
      <div className="stat-icon" style={{ color }}>
        <Icon />
      </div>
      <div className="stat-content">
        <h3 className="stat-value">{Number(value).toLocaleString()}</h3>
        <p className="stat-title">{title}</p>
      </div>
    </div>
  );

  if (loading) {
    return <div className="admin-dashboard p-4">Đang tải thống kê...</div>;
  }

  return (
    <div className="admin-dashboard">
      <div className="dashboard-header">
        <div className="header-content">
          <h1>Dashboard</h1>
          <p>Chào mừng bạn đến với trang quản trị hệ thống y tế học đường</p>
        </div>
        <div className="header-actions">
          <button className="btn btn-primary">
            <FaChartLine /> Báo cáo
          </button>
        </div>
      </div>

      <div className="stats-grid">
        <StatCard icon={FaUsers} title="Tổng người dùng" value={stats.totalUsers} color="#3b82f6" bgColor="#eff6ff" />
        <StatCard icon={FaUserGraduate} title="Học sinh" value={stats.totalStudents} color="#10b981" bgColor="#ecfdf5" />
        <StatCard icon={FaUserNurse} title="Y tá" value={stats.totalNurses} color="#f59e0b" bgColor="#fffbeb" />
        <StatCard icon={FaUserTie} title="Phụ huynh" value={stats.totalParents} color="#8b5cf6" bgColor="#f3f4f6" />
        <StatCard icon={FaBlog} title="Bài viết" value={stats.totalBlogs} color="#ef4444" bgColor="#fef2f2" />
        <StatCard icon={FaMedkit} title="Vật tư y tế" value={stats.totalMedicalSupplies} color="#06b6d4" bgColor="#ecfeff" />
      </div>

      <div className="dashboard-content">
        <div className="content-right" style={{ width: '100%' }}>
          <div className="widget">
            <div className="widget-header">
              <h3>Thống kê nhanh</h3>
            </div>
            <div className="quick-stats">
              <div className="quick-stat-item">
                <div className="quick-stat-icon pending">
                  <FaClock />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.pendingRequests}</h4>
                  <p>Yêu cầu thuốc chờ xử lý</p>
                </div>
              </div>
              <div className="quick-stat-item">
                <div className="quick-stat-icon incidents">
                  <FaExclamationTriangle />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.activeIncidents}</h4>
                  <p>Sự cố đang xử lý</p>
                </div>
              </div>
              <div className="quick-stat-item">
                <div className="quick-stat-icon completed">
                  <FaCheckCircle />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.totalMedicalSupplies}</h4>
                  <p>Loại vật tư y tế</p>
                </div>
              </div>
              <div className="quick-stat-item">
                <div className="quick-stat-icon appointments">
                  <FaCalendarCheck />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.totalStudents}</h4>
                  <p>Học sinh trong hệ thống</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
