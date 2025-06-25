import React from 'react';
import './About.scss';
import logo from '../assets/img/logo_uth.png';

const AboutPage = () => {
    return (
        <div className="about-page">
            <div className="about-header">
                <img src={logo} alt="UTH Logo" className="university-logo" />
                <h1>Trường Đại học Giao thông Vận tải</h1>
                <h2>University of Transport and Communications</h2>
            </div>

            <div className="about-content">
                <section className="about-section">
                    <h3>Giới thiệu chung</h3>
                    <p>
                        Trường Đại học Giao thông vận tải là một trong những trường đại học công lập hàng đầu của Việt Nam 
                        trong lĩnh vực đào tạo, nghiên cứu khoa học và chuyển giao công nghệ về giao thông vận tải. 
                        Trường được thành lập năm 1945 và đã trải qua hơn 75 năm phát triển.
                    </p>
                </section>

                <section className="about-section">
                    <h3>Sứ mệnh</h3>
                    <p>
                        Đào tạo nguồn nhân lực chất lượng cao, nghiên cứu khoa học, chuyển giao công nghệ và cung cấp 
                        các dịch vụ trong lĩnh vực giao thông vận tải, góp phần phát triển bền vững kinh tế - xã hội 
                        của đất nước.
                    </p>
                </section>

                <section className="about-section">
                    <h3>Tầm nhìn</h3>
                    <p>
                        Đến năm 2030, Trường Đại học Giao thông vận tải trở thành trường đại học định hướng nghiên cứu, 
                        đạt tiêu chuẩn quốc tế, là trung tâm đào tạo và nghiên cứu khoa học có uy tín trong khu vực 
                        về lĩnh vực giao thông vận tải.
                    </p>
                </section>

                <section className="about-section">
                    <h3>Giá trị cốt lõi</h3>
                    <ul>
                        <li>Chất lượng và Hiệu quả</li>
                        <li>Sáng tạo và Đổi mới</li>
                        <li>Trách nhiệm với xã hội</li>
                        <li>Hợp tác và Phát triển</li>
                    </ul>
                </section>

                <section className="about-section">
                    <h3>Thành tựu nổi bật</h3>
                    <ul>
                        
                    </ul>
                </section>

                <section className="about-section">
                    <h3>Thông tin liên hệ</h3>
                    <p>
                        <strong>Địa chỉ:</strong> Số 123 Đường ABC, Quận XYZ, TP. HCM<br />
                        <strong>Điện thoại:</strong> (028) 1234 5678<br />
                        <strong>Email:</strong> info@example.com<br />
                        <strong>Website:</strong> www.example.com
                    </p>
                </section>
            </div>
        </div>
    );
};

export default AboutPage;