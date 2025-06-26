import React, { useState } from "react";
import { Form, Button, Container, Row, Col, Card } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.scss";
import { toast } from "react-toastify";
const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [activeTab, setActiveTab] = useState("login");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        {
          email,
          password,
        }
      );

      console.log("Login successful:", response.data);
      
      // Lưu token vào localStorage
      localStorage.setItem("token", response.data.token || "dummy-token");
      localStorage.setItem("user", JSON.stringify(response.data));
        if (response.data.role === "STUDENT") {
  localStorage.setItem("studentId", response.data.id);
}
        // Chuyển hướng dựa vào role
        const role = response.data.role;
        if (role === "ADMIN") {
          navigate("/admin");
        } else if (role === "SCHOOL_NURSE") {
          navigate("/school-nurse/dashboard");
        } else if (role === "MANAGER") {
          navigate("/manager/dashboard");
        } else if (role === "PARENT") {
          navigate("/parent/dashboard");
        } else {
          navigate("/");
        }

        toast.success(`Đăng nhập ${email} thành công`);
      } catch (err) {
        setError("Email hoặc mật khẩu không đúng");
      }
  };

  return (
    <>
      <div className="login-container"></div>
      <div className="login-content">
        <Row className="w-100 justify-content-end">
          <Col md={6} lg={5} xl={4}>
            <Card className="login-card">
              <Card.Body>
                {/* Tabs */}
                <h1 className="login-tabs">
                    Đăng nhập
                </h1>
                {activeTab === "login" && (
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
                            type={showPassword ? "text" : "password"}
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
                            {showPassword ? "Ẩn" : "Hiện"}
                          </span>
                        </div>
                      </Form.Group>

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
      </div>
    </>
  );
};

export default Login;
