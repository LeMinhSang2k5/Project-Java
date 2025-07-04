import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../../pages/Header/Header';
import Footer from '../../pages/Header/Footer';
import NurseSidebar from './NurseSidebar';

const NurseLayout = () => {
  const getUserRole = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.role) {
        return user.role;
    }
  };
  const role = getUserRole();

  if (role !== 'SCHOOL_NURSE' && role !== 'ADMIN') {
      return (
          <div className="unauthorized">
              <h1>Unauthorized Access</h1>
              <p>You do not have permission to access this page.</p>
          </div>
      );
  }
  return (
    <div className="d-flex">
      <NurseSidebar />
      <div className="flex-grow-1">
      
        <main className="container-fluid py-4" style={{ minHeight: 'calc(100vh - 120px)' }}>
          <Outlet />
        </main>
   
      </div>
    </div>
  );
};

export default NurseLayout; 