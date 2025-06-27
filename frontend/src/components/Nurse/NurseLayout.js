import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../../pages/Header/Header';
import Footer from '../../pages/Header/Footer';
import NurseSidebar from './NurseSidebar';

const NurseLayout = () => {
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