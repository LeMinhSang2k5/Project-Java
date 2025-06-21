import React, { useState } from "react";
import './Chatbot.scss';

const ChatBot = () => {
  const [question, setQuestion] = useState("");
  const [responses, setResponses] = useState([
    {
      question: "Chào bạn! Tôi có thể giúp gì cho bạn?",
      answer: "Xin chào! Tôi là trợ lý AI y tế. Bạn có thể hỏi tôi về các triệu chứng, bệnh lý, hoặc tư vấn sức khỏe cơ bản.",
      isBot: true
    }
  ]);
  const [isLoading, setIsLoading] = useState(false);

  // Danh sách câu trả lời mẫu cho các câu hỏi phổ biến
  const getAutoResponse = (question) => {
    const q = question.toLowerCase();
    
    if (q.includes("đau đầu") || q.includes("nhức đầu")) {
      return "Đau đầu có thể do nhiều nguyên nhân: căng thẳng, mất ngủ, thiếu nước, hoặc các vấn đề nghiêm trọng hơn. Bạn nên: uống đủ nước, nghỉ ngơi, massage nhẹ. Nếu đau đầu kéo dài hoặc nghiêm trọng, hãy đến gặp bác sĩ.";
    }
    
    if (q.includes("sốt") || q.includes("nóng sốt")) {
      return "Sốt là dấu hiệu cơ thể đang chống lại nhiễm trùng. Bạn nên: nghỉ ngơi, uống nhiều nước, giữ ấm nhưng không quá nóng. Nếu sốt trên 38.5°C hoặc kéo dài quá 3 ngày, hãy đến bệnh viện.";
    }
    
    if (q.includes("ho") || q.includes("khàn tiếng")) {
      return "Ho có thể do cảm lạnh, dị ứng, hoặc nhiễm trùng. Bạn có thể: uống nước ấm với mật ong, giữ ẩm cho họng, tránh khói thuốc. Nếu ho có máu hoặc kéo dài quá 2 tuần, hãy khám bác sĩ.";
    }
    
    if (q.includes("đau bụng") || q.includes("đau dạ dày")) {
      return "Đau bụng có thể do ăn uống, stress, hoặc bệnh lý. Bạn nên: ăn nhẹ, tránh thức ăn cay nóng, uống nước ấm. Nếu đau dữ dội, buồn nôn, hoặc có máu trong phân, hãy đến bệnh viện ngay.";
    }

    if (q.includes("chào") || q.includes("hello") || q.includes("hi")) {
      return "Xin chào! Tôi rất vui được hỗ trợ bạn. Bạn có câu hỏi gì về sức khỏe không?";
    }

    if (q.includes("cảm ơn") || q.includes("thank")) {
      return "Rất vui được giúp đỡ bạn! Hãy chăm sóc sức khỏe thật tốt nhé!";
    }

    // Câu trả lời mặc định
    return "Cảm ơn câu hỏi của bạn! Tôi khuyên bạn nên tham khảo ý kiến bác sĩ chuyên khoa để được tư vấn chính xác nhất. Trong thời gian chờ đợi, hãy nghỉ ngơi đầy đủ và uống nhiều nước.";
  };

  const handleAsk = async () => {
    if (!question.trim()) return;

    setIsLoading(true);
    
    // Thêm câu hỏi của người dùng
    const userMessage = { question, answer: "", isBot: false };
    setResponses(prev => [...prev, userMessage]);

    // Simulate API delay
    setTimeout(() => {
      const aiResponse = getAutoResponse(question);
      const botMessage = { 
        question: "", 
        answer: aiResponse, 
        isBot: true 
      };
      
      setResponses(prev => [...prev, botMessage]);
      setQuestion("");
      setIsLoading(false);
    }, 1000);
  };

  return (
    <div className="chatbot-container">
      <div className="chatbot-header">
        <h2>🩺 Trợ Lý Y Tế AI</h2>
        <p>Tư vấn sức khỏe cơ bản - Không cần đăng nhập</p>
      </div>

      <div className="chatbot-messages">
        {responses.map((res, i) => (
          <div key={i}>
            {!res.isBot && res.question && (
              <div className="message user-message">
                <p><strong>Bạn:</strong> {res.question}</p>
              </div>
            )}
            {res.isBot && res.answer && (
              <div className="message ai-message">
                <p><strong>AI:</strong> {res.answer}</p>
              </div>
            )}
            {!res.isBot && res.answer && (
              <div className="message ai-message">
                <p><strong>AI:</strong> {res.answer}</p>
              </div>
            )}
          </div>
        ))}
        {isLoading && (
          <div className="message ai-message loading">
            <p><strong>AI:</strong> Đang suy nghĩ...</p>
          </div>
        )}
      </div>

      <div className="chatbot-input">
        <input
          type="text"
          placeholder="Hỏi về triệu chứng, bệnh lý... (VD: đau đầu, sốt, ho)"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && !isLoading && handleAsk()}
        />
        <button onClick={handleAsk} disabled={isLoading || !question.trim()}>
          {isLoading ? 'Đang xử lý...' : 'Gửi'}
        </button>
      </div>

      <div className="chatbot-disclaimer">
        <small>
          ⚠️ Thông tin chỉ mang tính tham khảo. Hãy tham khảo ý kiến bác sĩ để được chẩn đoán chính xác.
        </small>
      </div>
    </div>
  );
};

export default ChatBot; 