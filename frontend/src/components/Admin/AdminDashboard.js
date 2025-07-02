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
import './Admin.scss';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 1250,
    totalStudents: 890,
    totalNurses: 45,
    totalParents: 315,
    totalBlogs: 67,
    totalMedicalSupplies: 234,
    pendingRequests: 12,
    completedRequests: 89,
    activeIncidents: 3,
    todayAppointments: 15
  });

  const [recentActivities, setRecentActivities] = useState([
    { id: 1, type: 'user', action: 'Người dùng mới đăng ký', time: '2 phút trước', status: 'success' },
    { id: 2, type: 'blog', action: 'Blog mới được tạo', time: '15 phút trước', status: 'info' },
    { id: 3, type: 'medical', action: 'Vật tư y tế được cập nhật', time: '1 giờ trước', status: 'warning' },
    { id: 4, type: 'health', action: 'Hồ sơ sức khỏe mới', time: '2 giờ trước', status: 'success' },
    { id: 5, type: 'incident', action: 'Sự cố sức khỏe được báo cáo', time: '3 giờ trước', status: 'danger' }
  ]);

  const StatCard = ({ icon: Icon, title, value, change, color, bgColor }) => (
    <div className="stat-card" style={{ backgroundColor: bgColor }}>
      <div className="stat-icon" style={{ color }}>
        <Icon />
      </div>
      <div className="stat-content">
        <h3 className="stat-value">{value.toLocaleString()}</h3>
        <p className="stat-title">{title}</p>
        {change && (
          <span className={`stat-change ${change > 0 ? 'positive' : 'negative'}`}>
            {change > 0 ? '+' : ''}{change}% so với tháng trước
          </span>
        )}
      </div>
    </div>
  );

  const ActivityItem = ({ activity }) => {
    const getIcon = (type) => {
      switch (type) {
        case 'user': return <FaUsers />;
        case 'blog': return <FaBlog />;
        case 'medical': return <FaMedkit />;
        case 'health': return <FaCalendarCheck />;
        case 'incident': return <FaExclamationTriangle />;
        default: return <FaUsers />;
      }
    };

    const getStatusColor = (status) => {
      switch (status) {
        case 'success': return '#10b981';
        case 'info': return '#3b82f6';
        case 'warning': return '#f59e0b';
        case 'danger': return '#ef4444';
        default: return '#6b7280';
      }
    };

    return (
      <div className="activity-item">
        <div className="activity-icon" style={{ color: getStatusColor(activity.status) }}>
          {getIcon(activity.type)}
        </div>
        <div className="activity-content">
          <p className="activity-text">{activity.action}</p>
          <span className="activity-time">{activity.time}</span>
        </div>
      </div>
    );
  };

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
        <StatCard
          icon={FaUsers}
          title="Tổng người dùng"
          value={stats.totalUsers}
          change={12}
          color="#3b82f6"
          bgColor="#eff6ff"
        />
        <StatCard
          icon={FaUserGraduate}
          title="Học sinh"
          value={stats.totalStudents}
          change={8}
          color="#10b981"
          bgColor="#ecfdf5"
        />
        <StatCard
          icon={FaUserNurse}
          title="Y tá"
          value={stats.totalNurses}
          change={-2}
          color="#f59e0b"
          bgColor="#fffbeb"
        />
        <StatCard
          icon={FaUserTie}
          title="Phụ huynh"
          value={stats.totalParents}
          change={15}
          color="#8b5cf6"
          bgColor="#f3f4f6"
        />
        <StatCard
          icon={FaBlog}
          title="Bài viết"
          value={stats.totalBlogs}
          change={25}
          color="#ef4444"
          bgColor="#fef2f2"
        />
        <StatCard
          icon={FaMedkit}
          title="Vật tư y tế"
          value={stats.totalMedicalSupplies}
          change={-5}
          color="#06b6d4"
          bgColor="#ecfeff"
        />
      </div>

      <div className="dashboard-content">
        <div className="content-left">
          <div className="widget">
            <div className="widget-header">
              <h3>Hoạt động gần đây</h3>
              <button className="btn btn-text">Xem tất cả</button>
            </div>
            <div className="activities-list">
              {recentActivities.map(activity => (
                <ActivityItem key={activity.id} activity={activity} />
              ))}
            </div>
          </div>
        </div>

        <div className="content-right">
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
                  <p>Yêu cầu chờ xử lý</p>
                </div>
              </div>
              <div className="quick-stat-item">
                <div className="quick-stat-icon completed">
                  <FaCheckCircle />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.completedRequests}</h4>
                  <p>Yêu cầu đã hoàn thành</p>
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
                <div className="quick-stat-icon appointments">
                  <FaCalendarCheck />
                </div>
                <div className="quick-stat-info">
                  <h4>{stats.todayAppointments}</h4>
                  <p>Lịch hẹn hôm nay</p>
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
