import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import './Admin.scss';

const AdminLayout = () => {
    return (
        <div className="admin-layout">
            <Sidebar />
            <div className="admin-content">
                <div className="content-header">
                    <h2>Đây là header</h2>
                </div>
                <Outlet />
            </div>
        </div>
    );
};

export default AdminLayout;
