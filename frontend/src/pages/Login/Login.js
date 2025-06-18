import React, { useState } from 'react';
import { Form, Button, Container, Row, Col, Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.scss';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [activeTab, setActiveTab] = useState('login');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password
            });
            
            // Lưu token vào localStorage
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));
            
            // Chuyển hướng dựa vào role
            const role = response.data.role;
            if (role === 'ADMIN') {
                navigate('/');
            } else if (role === 'SCHOOL_NURSE') {
                navigate('/school-nurse/dashboard');
            } else if (role === 'MANAGER') {
                navigate('/manager/dashboard');
            } else if (role === 'PARENT') {
                navigate('/parent/dashboard');
            } else {
                navigate('/');
            }
        } catch (err) {
            setError('Email hoặc mật khẩu không đúng');
        }
    };

    return (
        <Container className="login-container">
            <Row className="justify-content-center">
                <Col md={12} className="d-flex justify-content-center">
                    <Card className="login-card">
                        <Card.Body>
                            {/* Tabs */}
                            <div className="login-tabs">
                                <button
                                    className={`login-tab${activeTab === 'login' ? ' active' : ''}`}
                                    onClick={() => setActiveTab('login')}
                                    type="button"
                                >
                                    Đăng nhập
                                </button>
                                <button
                                    className={`login-tab${activeTab === 'register' ? ' active' : ''}`}
                                    type="button"
                                    disabled
                                >
                                    Đăng ký
                                </button>
                            </div>
                            {activeTab === 'login' && (
                                <>
                                    {error && <div className="alert alert-danger">{error}</div>}
                                    <Form onSubmit={handleSubmit} autoComplete="off">
                                        <Form.Group className="mb-3">
                                            <Form.Label>Số điện thoại/Email</Form.Label>
                                            <Form.Control
                                                type="text"
                                                placeholder="Nhập số điện thoại hoặc email"
                                                value={email}
                                                onChange={(e) => setEmail(e.target.value)}
                                                required
                                            />
                                        </Form.Group>

                                        <Form.Group className="mb-1">
                                            <Form.Label>Mật khẩu</Form.Label>
                                            <div className="password-row">
                                                <Form.Control
                                                    type={showPassword ? 'text' : 'password'}
                                                    placeholder="Nhập mật khẩu"
                                                    value={password}
                                                    onChange={(e) => setPassword(e.target.value)}
                                                    required
                                                />
                                                <span
                                                    className="show-password"
                                                    onClick={() => setShowPassword(!showPassword)}
                                                    role="button"
                                                    tabIndex={0}
                                                >
                                                    {showPassword ? 'Ẩn' : 'Hiện'}
                                                </span>
                                            </div>
                                        </Form.Group>

                                        <a href="#" className="forgot-link">Quên mật khẩu?</a>

                                        <Button
                                            className="btn-login"
                                            type="submit"
                                            disabled={!email || !password}
                                        >
                                            Đăng nhập
                                        </Button>
                                    </Form>
                                </>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Login; 