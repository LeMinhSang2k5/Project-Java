import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom';

export default function ViewUser() {

    const [user, setUser] = useState({
        fullName: "",
        email: "",
        role: ""
    });

    const { id } = useParams();

    useEffect(() => {
        loadUser();
    }, []);

    const loadUser = async () => {
        const result = await axios.get(`http://localhost:8080/user/${id}`);
        setUser(result.data);
    }

    return (
        <div className="container" >
            <div className="row">
                <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
                    <h3 className="text-center">User Details</h3>
                    <div className="card">
                        <div className="card-header">
                            Details of User id : {id}
                            <ul className="list-group list-group-flush">
                                <li className="list-group-item">
                                    <b>Full Name:</b> {user.fullName}
                                </li>
                                <li className="list-group-item">
                                    <b>Email:</b> {user.email}
                                </li>
                                <li className="list-group-item">
                                    <b>Role:</b> {user.role}
                                </li>
                            </ul>
                        </div>
                    </div>
                    <Link to={"/"} className="btn btn-primary my-2">Back to Home</Link>
                </div>
            </div>
        </div >
    );
}