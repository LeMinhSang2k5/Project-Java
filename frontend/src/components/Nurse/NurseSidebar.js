import React from 'react';
import { Nav } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';

import { 
  FaTachometerAlt, 
  FaHeart, 
  FaPills, 
  FaSyringe, 
  FaStethoscope, 
  FaUsers, 
  FaChartBar,
  FaFileAlt 
} from 'react-icons/fa';
import './NurseSidebar.scss';

const NurseSidebar = () => {
  return (
    <div className="nurse-sidebar bg-light border-end" style={{ width: '280px', minHeight: '100vh' }}>
      <div className="p-3 border-bottom">
        <h5 className="text-primary mb-0">
          <FaHeart className="me-2" />
          Y tế trường học
        </h5>
        <small className="text-muted">Nhân viên y tế</small>
      </div>
      
      <Nav className="flex-column p-3">
        <LinkContainer to="/nurse">
          <Nav.Link className="sidebar-link mb-2">
            <FaTachometerAlt className="me-2" />
            Dashboard
          </Nav.Link>
        </LinkContainer>

        <div className="sidebar-section mt-3">
          <h6 className="text-muted small fw-bold">SỰ KIỆN Y TẾ</h6>
          <LinkContainer to="/nurse/health-incidents">
            <Nav.Link className="sidebar-link mb-1">
              <FaHeart className="me-2" />
              Sự kiện y tế
            </Nav.Link>
          </LinkContainer>
        </div>

        <div className="sidebar-section mt-3">
          <h6 className="text-muted small fw-bold">VẬT TƯ Y TẾ</h6>
          <LinkContainer to="/nurse/medical-supplies">
            <Nav.Link className="sidebar-link mb-1">
              <FaPills className="me-2" />
              Quản lý thuốc & vật tư
            </Nav.Link>
          </LinkContainer>
        </div>

        <div className="sidebar-section mt-3">
          <h6 className="text-muted small fw-bold">TIÊM CHỦNG</h6>
          <LinkContainer to="/nurse/vaccination">
            <Nav.Link className="sidebar-link mb-1">
              <FaSyringe className="me-2" />
              Quản lý tiêm chủng
            </Nav.Link>
          </LinkContainer>
          <LinkContainer to="/nurse/vaccination/schedule">
            <Nav.Link className="sidebar-link mb-1 ms-3">
              <FaFileAlt className="me-2" />
              Lịch tiêm chủng
            </Nav.Link>
          </LinkContainer>
        </div>

        <div className="sidebar-section mt-3">
          <h6 className="text-muted small fw-bold">KIỂM TRA Y TẾ</h6>
          <LinkContainer to="/nurse/health-check">
            <Nav.Link className="sidebar-link mb-1">
              <FaStethoscope className="me-2" />
              Kiểm tra định kỳ
            </Nav.Link>
          </LinkContainer>
          <LinkContainer to="/nurse/health-check/results">
            <Nav.Link className="sidebar-link mb-1 ms-3">
              <FaFileAlt className="me-2" />
              Kết quả kiểm tra
            </Nav.Link>
          </LinkContainer>
        </div>

        <div className="sidebar-section mt-3">
          <h6 className="text-muted small fw-bold">HỒ SƠ & BÁO CÁO</h6>
          <LinkContainer to="/nurse/health-profiles">
            <Nav.Link className="sidebar-link mb-1">
              <FaUsers className="me-2" />
              Hồ sơ sức khỏe
            </Nav.Link>
          </LinkContainer>
          <LinkContainer to="/nurse/reports">
            <Nav.Link className="sidebar-link mb-1">
              <FaChartBar className="me-2" />
              Báo cáo & thống kê
            </Nav.Link>
          </LinkContainer>
        </div>
      </Nav>
    </div>
  );
};

export default NurseSidebar; 