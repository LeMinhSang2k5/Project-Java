import React from 'react'
import { Link, Outlet } from 'react-router-dom'

export default function AdminLayout() {
    return (
        <div>
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container-fluid">
                    <Link className="navbar-brand" to="/">CRUD</Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <Link className="btn btn-outline-dark" to="/admin/adduser">Add User</Link>
                </div>
            </nav>

            <div className="container mt-4">
                <Outlet />
            </div>
        </div>
    )
}
