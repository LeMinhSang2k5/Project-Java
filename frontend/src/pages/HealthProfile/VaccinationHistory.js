import React, { useState } from 'react';
import { Form, Button, Card, Container, Table } from 'react-bootstrap';
import './HealthProfile.scss';

const VaccinationHistory = () => {
    const [vaccinations, setVaccinations] = useState([]);
    const [newVaccination, setNewVaccination] = useState({
        vaccineName: '',
        date: '',
        location: '',
        batchNumber: '',
        notes: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNewVaccination(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleAddVaccination = (e) => {
        e.preventDefault();
        setVaccinations(prevState => [...prevState, newVaccination]);
        setNewVaccination({
            vaccineName: '',
            date: '',
            location: '',
            batchNumber: '',
            notes: ''
        });
    };

    return (
        <div className="health-profile">
            <div className="health-profile__header">
                <Container>
                    <h2>Lịch sử tiêm chủng</h2>
                    <p>Quản lý và theo dõi lịch sử tiêm chủng của học sinh</p>
                </Container>
            </div>

            <Container>
                <Card className="health-profile__card">
                    <Card.Body>
                        <Form onSubmit={handleAddVaccination} className="health-profile__form mb-4">
                            <div className="health-profile__section">
                                <h3>Thêm mũi tiêm mới</h3>
                                <Form.Group className="mb-3">
                                    <Form.Label>Tên vaccine</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="vaccineName"
                                        value={newVaccination.vaccineName}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Ngày tiêm</Form.Label>
                                    <Form.Control
                                        type="date"
                                        name="date"
                                        value={newVaccination.date}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Nơi tiêm</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="location"
                                        value={newVaccination.location}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Số lô vaccine</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="batchNumber"
                                        value={newVaccination.batchNumber}
                                        onChange={handleChange}
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Ghi chú</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={2}
                                        name="notes"
                                        value={newVaccination.notes}
                                        onChange={handleChange}
                                    />
                                </Form.Group>

                                <div className="text-center">
                                    <Button variant="primary" type="submit" className="health-profile__button">
                                        Thêm mũi tiêm
                                    </Button>
                                </div>
                            </div>
                        </Form>

                        <div className="health-profile__section">
                            <h3>Danh sách tiêm chủng</h3>
                            <Table className="health-profile__table" striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>Tên vaccine</th>
                                        <th>Ngày tiêm</th>
                                        <th>Nơi tiêm</th>
                                        <th>Số lô</th>
                                        <th>Ghi chú</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {vaccinations.map((vaccination, index) => (
                                        <tr key={index}>
                                            <td>{vaccination.vaccineName}</td>
                                            <td>{vaccination.date}</td>
                                            <td>{vaccination.location}</td>
                                            <td>{vaccination.batchNumber}</td>
                                            <td>{vaccination.notes}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                    </Card.Body>
                </Card>
            </Container>
        </div>
    );
};

export default VaccinationHistory; 