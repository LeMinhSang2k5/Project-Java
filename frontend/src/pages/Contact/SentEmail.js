import { useState } from 'react';
import emailjs from 'emailjs-com';
import './SentEmail.scss';

const SentEmail = () => {
  const [formData, setFormData] = useState({
    user_email: 'linhsubin007@gmail.com',
    user_name: 'Y Tế Học Đường',
    email: '',
    from_name: '',
    phone: '',
    message: ''
  });
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState({ type: '', message: '' });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setStatus({ type: '', message: '' });

    try {
      const serviceID = 'service_pe870ze';
      const templateID = 'template_q5pcv9j';
      const userID = 'w7DRpjiIv_YvgXkX2';

      const emailParams = {
        user_email: formData.user_email,
        user_name: formData.user_name,
        name: formData.name,
        email: formData.email,
        phone: formData.phone,
        time: new Date().toLocaleString(),
        message: formData.message
      };

      const response = await emailjs.send(serviceID, templateID, emailParams, userID);
      console.log('Response:', response);
      
      setStatus({
        type: 'success',
        message: 'Gửi email thành công! Chúng tôi sẽ liên hệ lại với bạn sớm nhất (qua email hoặc sdt).'
      });
      
      setFormData(prev => ({
        ...prev,
        email: '',
        phone: '',
        from_name: '',
        message: ''
      }));

    } catch (error) {
      console.error('Failed to send email:', error);
      setStatus({
        type: 'error',
        message: 'Không thể gửi email. Vui lòng kiểm tra lại thông tin và thử lại.'
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="sent-email-container">
      <div className="email-form-wrapper">
        <h2>Liên hệ với chúng tôi</h2>
        <p className="form-description">
          Hãy điền thông tin bên dưới và chúng tôi sẽ liên hệ lại với bạn.
        </p>

        {status.message && (
          <div className={`status-message ${status.type}`}>
            {status.message}
          </div>
        )}

        <form onSubmit={handleSubmit} className="email-form">
          <div className="form-group">
            <label htmlFor="from_name">Họ và tên</label>
            <input
              type="text"
              id="from_name"
              name="from_name"
              value={formData.from_name}
              onChange={handleChange}
              required
              placeholder="Nhập họ và tên của bạn"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="Nhập email của bạn"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="phone">Số điện thoại</label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
              placeholder="Nhập số điện thoại của bạn"
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="message">Nội dung</label>
            <textarea
              id="message"
              name="message"
              value={formData.message}
              onChange={handleChange}
              required
              placeholder="Nhập nội dung tin nhắn của bạn"
              disabled={loading}
              rows="5"
            />
          </div>

          <button 
            type="submit" 
            className="submit-button"
            disabled={loading}
          >
            {loading ? 'Đang gửi...' : 'Gửi tin nhắn'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default SentEmail;