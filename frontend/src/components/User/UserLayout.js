import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../../pages/Header/Header';
import Footer from '../../pages/Header/Footer';

const UserLayout = () => (
    <div className="app-container">
        <Header />
        <div className="main-container">
            <div className="app-content">
                <Outlet />
            </div>
        </div>
        <Footer />
    </div>
);

export default UserLayout;
